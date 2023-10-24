package tim.quest.model;

/** Represents a logical condition which is checked by the Trigger */
public class Condition {
    private final String variableId;
    private final String value;
    private final Operator operator;

    public Condition(String variableId, String value, String operator) {
        this.variableId = variableId;
        this.value = value;
        this.operator = Operator.fromString(operator);
    }

    public boolean apply(String variableValue) {
        return operator.apply(variableValue, value);
    }

    public String getVariableName() {
        return variableId;
    }
}
