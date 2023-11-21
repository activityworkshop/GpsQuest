package tim.quest.model;

import tim.quest.QuestController;

import java.util.ArrayList;
import java.util.List;

public class Trigger extends QuestObject implements TriggerInterface {
    private final ArrayList<Condition> conditions = new ArrayList<>();
    private String sceneId = "";
    private Scene scene = null;
    private String timerIds = "";
    private final ArrayList<Timer> timers = new ArrayList<>();
    private VariableMap variables = null;
    private final VariableModMap varModMap = new VariableModMap();
    private QuestController controller = null;


    public Trigger(String triggerId) {
        super(triggerId);
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public void setSceneId(String id) {
        sceneId = preventNullString(id);
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void addTimer(Timer timer) {
        if (timer != null) {
            timers.add(timer);
        }
    }

    public void setTimerIds(String value) {
        timerIds = preventNullString(value);
    }

    public String getTimerIds() {
        return timerIds;
    }

    public List<Timer> getTimers() {
        return timers;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public boolean allConditionsMatch() {
        for (Condition condition : conditions) {
            String variableName = condition.getVariableName();
            String value = variables.getValue(variableName);
            if (!condition.apply(value)) {
                return false;
            }
        }
        return true;
    }

    public boolean fire() {
        if (allConditionsMatch()) {
            for (Timer timer : timers) {
                timer.start();
            }
            // update all variables
            varModMap.apply(variables);
            if (scene != null && controller != null) {
                controller.activateScene(scene);
            }
            return true;
        }
        return false;
    }

    public void setVariables(VariableMap variables) {
        this.variables = variables;
    }

    public void addSetVariable(String varName, String varValue, String operator) {
        varModMap.addAction(varName, operator, varValue);
    }

    public VariableModMap getVariableModMap() {
        return varModMap;
    }

    public void setController(QuestController controller) {
        this.controller = controller;
    }
}
