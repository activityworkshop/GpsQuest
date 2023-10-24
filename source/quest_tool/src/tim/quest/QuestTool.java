package tim.quest;

import tim.quest.load.QuestFileException;
import tim.quest.load.QuestLoader;
import tim.quest.model.Quest;

import java.util.ArrayList;
import java.util.HashMap;

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
            // TODO: Do some validation
            if (quest == null) {
                findings.add(new Finding(Finding.Severity.ERROR, "Quest could not be created"));
            }
            else {
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

        // TODO: Do some specific validation here which is not required at runtime, just for validation phase
        // eg checking for variables which aren't used, timers which aren't activated, scenes which are never called,
        // triggers which set variables which aren't defined, ...
        // maybe also resources which are never referred to in scenes

        boolean allOk = true;
        for (Finding finding : findings) {
            System.out.println(finding.getTypeDesc() + ": " + finding.getText());
            allOk = allOk && finding.getSeverity() == Finding.Severity.INFO;
        }
        if (allOk) {
            System.out.println("OK");
        }
    }
}
