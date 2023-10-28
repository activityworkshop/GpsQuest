package tim.quest.load;

import java.util.ArrayList;
import java.util.List;

public class LoadUtils {
    static List<String> splitByCommas(String value) {
        ArrayList<String> result = new ArrayList<>();
        String trimmed = (value == null ? "" : value.trim());
        for (String part : trimmed.split(",")) {
            part = part.trim();
            if (!part.isEmpty()) {
                result.add(part);
            }
        }
        return result;
    }
}
