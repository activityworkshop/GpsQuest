package tim.quest.xml;

import java.util.ArrayList;
import java.util.HashMap;

public class XmlProperties {
    private final HashMap<String, String> properties = new HashMap<>();
    private final ArrayList<String> duplicateKeys = new ArrayList<>();

    public void set(String key, String value) {
        if (properties.containsKey(key)) {
            duplicateKeys.add(key);
        }
        else {
            properties.put(key, value);
        }
    }

    public void setIndexed(String key, String value) {
        final int index = getNextIndex(key);
        set(key + "_" + index, value);
    }

    public void set(String key, String suffix, String value) {
        set(key + "_" + suffix, value);
    }

    public String getProperty(String key) {
        final String value = properties.get(key);
        return value == null ? "" : value;
    }

    public String getProperty(String key, int index) {
        return properties.get(key + "_" + index);
    }

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    public int countProperties(String key) {
        int count = 0;
        final String prefix = key + "_";
        for (String storedKey : properties.keySet()) {
            if (storedKey.startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }

    public HashMap<String, String> getAllProperties(String key) {
        HashMap<String, String> result = new HashMap<>();
        final String prefix = key + "_";
        for (String storedKey : properties.keySet()) {
            if (storedKey.startsWith(prefix)) {
                result.put(storedKey.substring(prefix.length()), properties.get(storedKey));
            }
        }
        return result;
    }

    private int getNextIndex(String key) {
        int index = 1;
        while (properties.containsKey(key + "_" + index)) {
            index++;
        }
        return index;
    }
}
