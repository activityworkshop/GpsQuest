package tim.quest.model;

import java.util.HashMap;
import java.util.Set;

public class VariableModMap {
    /** What to do with the variable value */
    private enum Operand {SET("set"), INC("inc"), DEC("dec");
        private final String str;
        Operand(String str) {
            this.str = str;
        }
        static Operand fromString(String s) {
            for (Operand op : values()) {
                if (op.str.equals(s)) {
                    return op;
                }
            }
            return SET;
        }
    }

    /** Action to perform on a single variable */
    private static class VariableAction {
        public final Operand operand;
        public final String value;

        private VariableAction(Operand operand, String value) {
            this.operand = operand;
            this.value = value;
        }
    }

    /** Map of actions according to variable name */
    private final HashMap<String, VariableAction> actions = new HashMap<>();

    public void addAction(String varName, String action, String value) {
        actions.put(varName, new VariableAction(Operand.fromString(action), value));
    }

    public void apply(VariableMap variables) {
        for (String key : actions.keySet()) {
            VariableAction action = actions.get(key);
            if (!variables.containsKey(key)) {
                throw new RuntimeException("Unrecognised variable '" + key + "' not found in variable map");
            }
            final String valueToSet;
            switch (action.operand) {
                case SET:
                    valueToSet = action.value;
                    break;
                case INC:
                    valueToSet = "" + (VariableUtils.getInt(variables.getValue(key)) + VariableUtils.getInt(action.value));
                    break;
                case DEC:
                    valueToSet = "" + (VariableUtils.getInt(variables.getValue(key)) - VariableUtils.getInt(action.value));
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognised variable operand: " + action.operand.str);
            }
            variables.setValue(key, valueToSet);
        }
    }

    public Set<String> getVariables() {
        return actions.keySet();
    }
}
