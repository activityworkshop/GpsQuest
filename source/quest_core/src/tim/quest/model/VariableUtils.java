package tim.quest.model;

public class VariableUtils {
    /** Convert a variable string into a numeric value, swallowing parse errors */
    public static int getInt(String value) {
        String s = (value == null ? "" : value.trim());
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
