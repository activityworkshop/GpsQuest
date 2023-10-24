package tim.quest.model;

import java.util.HashMap;

public class VariableMap extends HashMap<String, String> {
    public void addVariable(String name, String value) {
        put(name, value);
    }

    public void setValue(String name, String value) {
        if (!containsKey(name)) {
            throw new IllegalArgumentException("Variable '" + name + "' not defined");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value of variable '" + name + "' can't be null");
        }
        put(name, value);
    }

    public String getValue(String name) {
        String value = get(name);
        if (value == null) {
            throw new IllegalArgumentException("Variable '" + name + "' not defined");
        }
        return value;
    }
}
