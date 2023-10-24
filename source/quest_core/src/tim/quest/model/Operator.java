package tim.quest.model;

/** Represents a comparison operator for Trigger logic */
public enum Operator {
    EQUAL("eq"), NOT_EQUAL("ne"), GREATER_THAN("gt"), LESS_THAN("lt");

    private final String code;

    Operator(String code) {
        this.code = code;
    }

    public static Operator fromString(String code) {
        String lowerCode = (code == null ? "" : code.trim().toLowerCase());
        for (Operator op : values()) {
            if (op.code.equals(lowerCode)) {
                return op;
            }
        }
        return EQUAL;
    }

    public boolean apply(String variableValue, String compareValue) {
        switch(this) {
            case EQUAL:
                return isEqual(variableValue, compareValue);
            case NOT_EQUAL:
                return !isEqual(variableValue, compareValue);
            case GREATER_THAN:
                return isGreaterThan(variableValue, compareValue);
            case LESS_THAN:
                return isGreaterThan(compareValue, variableValue);
            default:
                throw new IllegalArgumentException("Unrecognised operator: " + code);
        }
    }

    private boolean isEqual(String variableValue, String compareValue) {
        return safeTrim(variableValue).equals(safeTrim(compareValue));
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isGreaterThan(String variableValue, String compareValue) {
        try {
            return getInt(variableValue) > getInt(compareValue);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    private int getInt(String value) throws NumberFormatException {
        return Integer.parseInt(value == null ? "" : value.trim());
    }
}
