package tim.quest;

import tim.quest.load.QuestFileException;
import tim.quest.load.QuestLoader;
import tim.quest.model.Timer;
import tim.quest.model.*;

import java.util.*;

/**
 * Entry point to Quest tool
 */
public class QuestTool {
    public static void main(String[] args) {
        if (args.length == 2
                && args[0].equals("validate")) {
            validateQuestFile(args[1]);
        }
        else {
            System.out.println("QuestTool:\n"
                    + "A tool to validate Quest files\n"
                    + "Usage: QuestTool validate <filename>: validate the specified file");
        }
    }

    private static void validateQuestFile(String filename) {
        System.out.println("Validating: '" + filename + "'");
        ArrayList<Finding> findings = new ArrayList<>();
        try {
            Quest quest = QuestLoader.fromFile(filename, findings);
            if (quest == null) {
                findings.add(new Finding(Finding.Severity.ERROR, "Quest could not be created"));
            }
            else {
                doFurtherValidation(quest, findings);
                findings.add(new Finding(Finding.Severity.INFO, "Quest is '" + quest.getName() + "' by '" + quest.getAuthor() + "'"));
                HashMap<String, String> descriptions = quest.getDescriptions();
                for (String lang : descriptions.keySet()) {
                    findings.add(new Finding(Finding.Severity.INFO, "Description for language '" + lang + "' is '" + descriptions.get(lang) + "'"));
                }
                findings.add(new Finding(Finding.Severity.INFO, "Quest contains " + quest.getNumVariables() + " variables"));
                findings.add(new Finding(Finding.Severity.INFO, "Quest contains " + quest.getNumTimers() + " timers"));
                findings.add(new Finding(Finding.Severity.INFO, "Quest contains " + quest.getZones().size() + " zones"));
                findings.add(new Finding(Finding.Severity.INFO, "Quest contains " + quest.getNumTriggers() + " triggers"));
                findings.add(new Finding(Finding.Severity.INFO, "Quest contains " + quest.getNumScenes() + " scenes"));
            }
        } catch (QuestFileException e) {
            findings.add(new Finding(Finding.Severity.ERROR, e.getMessage()));
        }

        boolean allOk = true;
        boolean hasErrors = false;
        for (Finding finding : findings) {
            System.out.println(finding.getTypeDesc() + ": " + finding.getText());
            allOk = allOk && finding.getSeverity() == Finding.Severity.INFO;
            hasErrors = hasErrors || finding.getSeverity() == Finding.Severity.ERROR;
        }
        if (allOk) {
            System.out.println("OK");
        }
        else if (hasErrors) {
            System.out.println("Finished with error(s)");
        }
        else {
            System.out.println("Finished with warning(s)");
        }
    }

    /** Do some specific validation here which is not required at runtime, just for validation phase */
    private static void doFurtherValidation(Quest quest, List<Finding> findings) {
        checkForUnusedVariables(quest, findings);
        for (Trigger trigger : quest.getTriggers()) {
            checkConditionConsistency(trigger, findings);
        }
        checkForUnusedTimers(quest, findings);
        checkForUnusedScenes(quest, findings);
        checkForLanguageConsistency(quest, findings);
        // check all resources can be found, maybe also check for resources which are never referred to in scenes
        // check for overlapping zones? convex polygons?
        // if trigger references a combination of conditions on the same variable and they're not compatible => warning
        // prompt with mismatching buttons and actions => error
    }

    private static void checkForUnusedVariables(Quest quest, List<Finding> findings) {
        HashSet<String> readVariables = new HashSet<>();
        HashSet<String> writeVariables = new HashSet<>();
        for (Trigger trigger : quest.getTriggers()) {
            for (Condition condition : trigger.getConditions()) {
                readVariables.add(condition.getVariableName());
            }
            writeVariables.addAll(trigger.getVariableModMap().getVariables());
        }
        for (String definedVariable : quest.getVariableNames()) {
            boolean isRead = readVariables.contains(definedVariable);
            boolean isWritten = writeVariables.contains(definedVariable);
            if (!isRead && !isWritten) {
                findings.add(new Finding(Finding.Severity.WARNING, "Variable '" + definedVariable + "' is never used"));
            }
            else if (!isRead) {
                findings.add(new Finding(Finding.Severity.WARNING, "Variable '" + definedVariable + "' is written but never read"));
            }
            else if (!isWritten) {
                findings.add(new Finding(Finding.Severity.WARNING, "Variable '" + definedVariable
                        + "' is read but the value is always " + quest.getVariableValue(definedVariable)));
            }
        }
    }

    private static void checkForUnusedTimers(Quest quest, List<Finding> findings) {
        HashSet<String> usedTimers = new HashSet<>();
        for (Trigger trigger : quest.getTriggers()) {
            for (Timer timer : trigger.getTimers()) {
                usedTimers.add(timer.getId());
            }
        }
        for (Timer timer : quest.getTimers()) {
            if (!usedTimers.contains(timer.getId())) {
                findings.add(new Finding(Finding.Severity.WARNING, "Timer '" + timer.getId() + "' is never used"));
            }
        }
    }

    private static void checkForUnusedScenes(Quest quest, List<Finding> findings) {
        HashSet<String> usedScenes = new HashSet<>();
        usedScenes.add(quest.getStartScene().getId());
        for (Trigger trigger : quest.getTriggers()) {
            usedScenes.add(trigger.getSceneId());
        }
        for (Scene scene : quest.getScenes()) {
            if (!usedScenes.contains(scene.getId())) {
                findings.add(new Finding(Finding.Severity.WARNING, "Scene '" + scene.getId() + "' is never used"));
            }
        }
    }

    private static void checkConditionConsistency(Trigger trigger, List<Finding> findings) {
        HashMap<String, HashSet<String>> conditions = new HashMap<>();
        for (Condition condition : trigger.getConditions()) {
            String value = condition.getCheckString();
            final String variableName = condition.getVariableName();
            if (conditions.containsKey(variableName)) {
                conditions.get(variableName).add(value);
            } else {
                HashSet<String> variableConditions = new HashSet<>();
                variableConditions.add(value);
                conditions.put(variableName, variableConditions);
            }
        }
        // Go through each variable in turn and check that the conditions are consistent with each other
        for (String varName : conditions.keySet()) {
            checkConditionConsistency(trigger.getId(), varName, conditions.get(varName), findings);
        }
    }

    /** Check whether the conditions of a trigger are internally consistent */
    private static void checkConditionConsistency(String triggerName, String variableName, HashSet<String> conditions, List<Finding> findings) {
        int numEquals = 0, numNotEquals = 0, numLessThan = 0, numGreaterThan = 0;
        for (String condition : conditions) {
            checkNumbersInCondition(triggerName, variableName, condition, findings);
            switch (condition.substring(0, 2)) {
                case "eq":
                    numEquals++;
                    break;
                case "ne":
                    numNotEquals++;
                    break;
                case "lt":
                    numLessThan++;
                    break;
                case "gt":
                    numGreaterThan++;
                    break;
            }
        }
        final int totalChecks = numEquals + numNotEquals + numLessThan + numGreaterThan;
        if (numEquals > 1) {
            findings.add(new Finding(Finding.Severity.ERROR, "Trigger '" + triggerName + "' has inconsistent conditions on variable '" + variableName + "'"));
        }
        else if (numEquals == 1 && totalChecks > 1) {
            findings.add(new Finding(Finding.Severity.ERROR, "Trigger '" + triggerName + "' has inconsistent conditions on variable '" + variableName + "'"));
        }
        else if (numGreaterThan > 1 || numLessThan > 1) {
            findings.add(new Finding(Finding.Severity.ERROR, "Trigger '" + triggerName + "' has redundant conditions on variable '" + variableName + "'"));
        }
        // TODO: Could potentially check for other inconsistencies here, like impossible combinations of lt and gt
    }

    private static void checkNumbersInCondition(String triggerName, String variableName, String condition, List<Finding> findings) {
        char operator = condition.charAt(0);
        if (operator == '<' || operator == '>') {
            String value = condition.substring(3);
            if (!isNumber(value)) {
                findings.add(new Finding(Finding.Severity.ERROR, "Condition '" + triggerName + "' has an invalid numeric condition on variable '" + variableName + "'"));
            }
        }
    }

    // TODO: Maybe move somewhere more general
    private static boolean isNumber(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            Integer.parseInt(value.trim());
            return true;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static void checkForLanguageConsistency(Quest quest, List<Finding> findings) {
        HashSet<String> incompleteLanguages = new HashSet<>();
        HashSet<String> allLanguages = new HashSet<>(quest.getDescriptions().keySet());
        for (Scene scene : quest.getScenes()) {
            boolean isSingleLanguage = isEmpty(allLanguages);
            HashSet<String> sceneLanguages = new HashSet<>(scene.getLanguages());
            if (isSingleLanguage) {
                allLanguages = sceneLanguages;
            }
            else {
                incompleteLanguages.addAll(getMismatch(allLanguages, sceneLanguages));
                allLanguages.addAll(sceneLanguages);
            }
        }
        // Give warnings for incomplete languages
        for (String language : incompleteLanguages) {
            findings.add(new Finding(Finding.Severity.WARNING, "Language '" + language + "' is not complete"));
        }
    }

    private static boolean isEmpty(HashSet<String> languages) {
        return languages.isEmpty() || (languages.size() == 1 && languages.contains(""));
    }

    /** @return all languages which aren't part of both sets */
    private static Collection<String> getMismatch(HashSet<String> allLanguages, HashSet<String> sceneLanguages) {
        if (isEmpty(allLanguages) || isEmpty(sceneLanguages)) {
            return new ArrayList<>();
        }
        HashSet<String> total = new HashSet<>(allLanguages);
        total.addAll(sceneLanguages);

        HashSet<String> result = new HashSet<>();
        for (String language : total) {
            if (!allLanguages.contains(language) || !sceneLanguages.contains(language)) {
                result.add(language);
            }
        }
        result.remove("");
        return result;
    }
}
