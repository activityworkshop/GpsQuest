package tim.quest.load;

import tim.quest.Finding;
import tim.quest.model.*;
import tim.quest.xml.SceneContents;
import tim.quest.xml.XmlProperties;
import tim.quest.xml.XmlQuestModel;

import java.util.HashMap;
import java.util.List;

/** Responsible for building the Quest object from the String-based Xml model */
public class QuestBuilder {
    private final XmlQuestModel model;
    private final List<Finding> findings;


    public QuestBuilder(XmlQuestModel model, List<Finding> findings) {
        this.model = model;
        this.findings = findings;
    }

    /** @return int from a String, or the default value if it can't be recognised */
    private boolean getBoolean(String value, boolean defaultValue) {
        if (value != null && !value.isEmpty()) {
            return "true".equalsIgnoreCase(value)
                    || "y".equalsIgnoreCase(value)
                    || "yes".equalsIgnoreCase(value);
        }
        return defaultValue;
    }

    /** @return int from a String, or -1 if it can't be parsed */
    private int getInt(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException ignored) {}
        }
        return -1;
    }

    /** @return double from a String, or null if it can't be parsed */
    private Double getDouble(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Double.parseDouble(value);
            }
            catch (NumberFormatException ignored) {}
        }
        return null;
    }

    public Quest buildQuest() {
        Quest quest = new Quest();
        setBasicProperties(quest);
        setVariables(quest);
        setTimers(quest);
        setZones(quest);
        setTriggers(quest);
        setScenes(quest);
        setStartScene(quest);
        // Link objects together, perform more validation
        connectTimersToTriggers(quest);
        connectZonesToTriggers(quest);
        connectTriggersToScenes(quest);
        connectTriggersToTimers(quest);
        connectTriggersToVariables(quest);
        // TODO: What about resources?
        return quest;
    }

    private void setBasicProperties(Quest quest) {
        quest.setName(model.getMainProperties().getProperty(XmlQuestModel.TAG_NAME));
        quest.setAuthor(model.getMainProperties().getProperty(XmlQuestModel.TAG_AUTHOR));
        HashMap<String, String> descriptions = model.getMainProperties().getAllProperties(XmlQuestModel.TAG_DESCRIPTION);
        for (String key : descriptions.keySet()) {
            final String description = descriptions.get(key);
            if (!description.isEmpty()) {
                quest.addDescription(key, description);
            }
        }
    }

    private void setVariables(Quest quest) {
        for (XmlProperties variable : model.getVariables()) {
            String variableId = variable.getProperty(XmlQuestModel.TAG_ID);
            if (isInvalidId(variableId)) {
                addError("Variable id '" + variableId + "' is not valid");
            }
            else if (quest.hasVariable(variableId)) {
                addError("Variable id '" + variableId + "' is defined multiple times");
            }
            else {
                quest.addVariable(variableId,
                        variable.getProperty(XmlQuestModel.TAG_VALUE));
            }
        }
    }

    private void setTimers(Quest quest) {
        for (XmlProperties timer : model.getTimers()) {
            final String timerId = timer.getProperty(XmlQuestModel.TAG_ID);
            final String timerPeriodString = timer.getProperty(XmlQuestModel.TAG_TIMER_PERIOD);
            int period = getInt(timerPeriodString);
            final String triggerString = timer.getProperty(XmlQuestModel.TAG_TRIGGERS);
            if (isInvalidId(timerId)) {
                addError("Timer id '" + timerId + "' is not valid");
            }
            else if (quest.hasTimer(timerId)) {
                addError("Timer id '" + timerId + "' is defined multiple times");
            }
            else if (period <= 0) {
                addError("Timer period '" + timerPeriodString + "' for id '" + timerId + "' is not valid");
            }
            else if (triggerString.isEmpty()) {
                addError("Timer '" + timerId + "' has no triggers");
            }
            else {
                final boolean repeating = getBoolean(timer.getProperty(XmlQuestModel.TAG_TIMER_REPEATING), false);
                quest.addTimer(timerId, period, triggerString, repeating);
            }
        }
    }

    private void setZones(Quest quest) {
        for (XmlProperties zone : model.getZones()) {
            final String zoneId = zone.getProperty(XmlQuestModel.TAG_ID);
            if (quest.hasZone(zoneId)) {
                addError("Zone '" + zoneId + "' is defined multiple times");
                continue;
            }
            final String enterTriggers = zone.getProperty(XmlQuestModel.TAG_ENTER_TRIGGERS);
            final String exitTriggers = zone.getProperty(XmlQuestModel.TAG_EXIT_TRIGGERS);
            Zone zoneDef = null;
            if (zone.hasProperty(XmlQuestModel.TAG_POINT_LATITUDE)) {
                Double lat = getDouble(zone.getProperty(XmlQuestModel.TAG_POINT_LATITUDE));
                Double lon = getDouble(zone.getProperty(XmlQuestModel.TAG_POINT_LONGITUDE));
                int radius = getInt(zone.getProperty(XmlQuestModel.TAG_ZONE_RADIUS));
                if (lat == null || lon == null || radius < 1) {
                    addError("Zone '" + zoneId + "' has improperly defined point or radius");
                }
                else {
                    Point point = new Point(lat, lon);
                    if (point.isValid()) {
                        zoneDef = new PointZone(zoneId, enterTriggers, exitTriggers, point, radius);
                    }
                    else {
                        addError("Zone '" + zoneId + "' has improperly defined point coordinates");
                    }
                }
            }
            else {
                int numLatitudes = zone.getAllProperties(XmlQuestModel.TAG_POINT_LATITUDE).size();
                int numLongitudes = zone.getAllProperties(XmlQuestModel.TAG_POINT_LONGITUDE).size();
                if (numLatitudes < 3 || numLatitudes != numLongitudes) {
                    addError("Zone '" + zoneId + "' has improperly defined nodes");
                }
                else {
                    PolygonZone polygon = new PolygonZone(zoneId, enterTriggers, exitTriggers);
                    for (int i=1; i<=numLatitudes; i++) {
                        Double lat = getDouble(zone.getProperty(XmlQuestModel.TAG_POINT_LATITUDE, i));
                        Double lon = getDouble(zone.getProperty(XmlQuestModel.TAG_POINT_LONGITUDE, i));
                        if (lat == null || lon == null) {
                            addError("Zone '" + zoneId + "' has an improperly defined node");
                        }
                        else {
                            Point point = new Point(lat, lon);
                            if (point.isValid()) {
                                polygon.addNode(point);
                            }
                            else {
                                addError("Zone '" + zoneId + "' has a node with improperly defined coordinates");
                            }
                        }
                    }
                    zoneDef = polygon;
                }
            }
            if (zoneDef != null) {
                zoneDef.setVisible(getBoolean(zone.getProperty(XmlQuestModel.TAG_ZONE_VISIBLE), true));
                quest.addZone(zoneDef);
            }
        }
        if (quest.getZones().isEmpty()) {
            addError("No zones defined - a Quest should define at least one!");
        }
    }

    private void setTriggers(Quest quest) {
        for (XmlProperties trigger : model.getTriggers()) {
            final String triggerId = trigger.getProperty(XmlQuestModel.TAG_ID);
            if (quest.hasTrigger(triggerId)) {
                addError("Trigger '" + triggerId + "' is defined multiple times");
                continue;
            }
            Trigger triggerDef = new Trigger(triggerId);
            final int numConditions = trigger.countProperties(XmlQuestModel.TAG_TRIGGER_CONDITION_VAR);
            for (int i=1; i<=numConditions; i++) {
                String varName = trigger.getProperty(XmlQuestModel.TAG_TRIGGER_CONDITION_VAR, i);
                String varValue = trigger.getProperty(XmlQuestModel.TAG_TRIGGER_CONDITION_VALUE, i);
                String operator = trigger.getProperty(XmlQuestModel.TAG_TRIGGER_CONDITION_OPERATOR, i);
                triggerDef.addCondition(new Condition(varName, varValue, operator));
            }
            triggerDef.setSceneId(trigger.getProperty(XmlQuestModel.TAG_STARTSCENE));
            triggerDef.setTimerIds(trigger.getProperty(XmlQuestModel.TAG_TRIGGER_TIMERID));
            final int numSetVariables = trigger.countProperties(XmlQuestModel.TAG_TRIGGER_SETVARIABLENAME);
            for (int i=1; i<=numSetVariables; i++) {
                String varName = trigger.getProperty(XmlQuestModel.TAG_TRIGGER_SETVARIABLENAME, i);
                String varValue = trigger.getProperty(XmlQuestModel.TAG_TRIGGER_SETVALUE, i);
                String operator = trigger.getProperty(XmlQuestModel.TAG_TRIGGER_SETOPERATOR, i);
                triggerDef.addSetVariable(varName, varValue, operator);
            }
            quest.addTrigger(triggerDef);
        }
    }

    private void setScenes(Quest quest) {
        for (SceneContents scene : model.getScenes()) {
            Scene existingSceneDef = quest.getScene(scene.getId());
            if (existingSceneDef != null) {
                if (existingSceneDef.getLanguages().contains(scene.getLanguage())) {
                    addError("Scene id '" + scene.getId() + "' is defined multiple times for language '" + scene.getLanguage() + "'");
                }
                else {
                    existingSceneDef.addContent(scene.getLanguage(), scene.getContents());
                }
            }
            else {
                Scene sceneDef = new Scene(scene.getId());
                sceneDef.addContent(scene.getLanguage(), scene.getContents());
                quest.addScene(sceneDef);
            }
        }
    }

    private void setStartScene(Quest quest) {
        String sceneId = model.getMainProperties().getProperty(XmlQuestModel.TAG_STARTSCENE);
        Scene scene = quest.getScene(sceneId);
        if (isInvalidId(sceneId)) {
            addError("Start scene id '" + sceneId + "' not specified or not valid");
        }
        else if (scene == null) {
            addError("Start scene '" + sceneId + "' not found");
        }
        quest.setStartScene(scene);
    }

    private void connectTimersToTriggers(Quest quest) {
        for (Timer timer : quest.getTimers()) {
            String triggerString = timer.getTriggerString();
            for (String triggerId : triggerString.split(",")) {
                if (!triggerId.isEmpty()) {
                    Trigger trigger = quest.getTrigger(triggerId);
                    if (trigger == null) {
                        addError("Timer '" + timer.getId() + "' refers to trigger '" + triggerId + "' which cannot be found");
                    }
                    timer.addTrigger(trigger);
                }
            }
            if (timer.getNumTriggers() == 0) {
                addError("Timer '" + timer.getId() + "' does not have any valid triggers");
            }
        }
    }

    private void connectZonesToTriggers(Quest quest) {
        for (Zone zone : quest.getZones()) {
            for (String triggerId : LoadUtils.splitByCommas(zone.getEnterTriggerString())) {
                Trigger trigger = quest.getTrigger(triggerId);
                if (trigger == null) {
                    addError("Zone '" + zone.getId() + "' refers to trigger '" + triggerId + "' which cannot be found");
                }
                zone.addEnterTrigger(trigger);
            }
            for (String triggerId : LoadUtils.splitByCommas(zone.getExitTriggerString())) {
                Trigger trigger = quest.getTrigger(triggerId);
                if (trigger == null) {
                    addError("Zone '" + zone.getId() + "' refers to trigger '" + triggerId + "' which cannot be found");
                }
                zone.addExitTrigger(trigger);
            }
            if (zone.getNumEnterTriggers() == 0 && zone.getNumExitTriggers() == 0) {
                addError("Zone '" + zone.getId() + "' does not have any valid triggers");
            }
        }
    }

    private void connectTriggersToScenes(Quest quest) {
        for (Trigger trigger : quest.getTriggers()) {
            String sceneId = trigger.getSceneId();
            if (isInvalidId(sceneId)) {
                continue;
            }
            Scene scene = quest.getScene(sceneId);
            if (scene == null) {
                addError("Trigger '" + trigger.getId() + "' calls scene '" + sceneId + "' but this scene was not found");
            }
            trigger.setScene(scene);
        }
    }

    private void connectTriggersToTimers(Quest quest) {
        for (Trigger trigger : quest.getTriggers()) {
            String timerId = trigger.getTimerIds();
            if (isInvalidId(timerId)) {
                continue;
            }
            Timer timer = quest.getTimer(timerId);
            if (timer == null) {
                addError("Trigger '" + trigger.getId() + "' refers to timer '" + timerId + "' which cannot be found");
            }
            trigger.addTimer(timer);
        }
    }

    private void connectTriggersToVariables(Quest quest) {
        for (Trigger trigger : quest.getTriggers()) {
            for (Condition condition : trigger.getConditions()) {
                String variableId = condition.getVariableName();
                if (!quest.hasVariable(variableId)) {
                    addError("Trigger '" + trigger.getId() + "' refers to variable '" + variableId + "' which cannot be found");
                }
            }
            for (String variableId : trigger.getVariableModMap().getVariables()) {
                if (!quest.hasVariable(variableId)) {
                    addError("Trigger '" + trigger.getId() + "' modifies variable '" + variableId + "' which cannot be found");
                }
            }
        }
    }

    private void addError(String message) {
        if (findings != null) {
            findings.add(new Finding(Finding.Severity.ERROR, message));
        }
    }

    private boolean isInvalidId(String id) {
        return id == null || id.equals("") || id.contains("_");
    }
}
