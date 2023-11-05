package tim.quest.simulator;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class I18nTexts {
    private final Properties localTexts = new Properties();

    public void setLanguage(String language) {
        final String bundleName = "tim.quest.simulator.lang.quest-texts";
        final Locale backupLocale = new Locale("en", "GB");
        localTexts.clear();
        // Load English texts first to use as defaults
        loadFromBundle(ResourceBundle.getBundle(bundleName, backupLocale));
        // Now choose selected language to overwrite
        Locale locale = getLocale(language);
        if (locale != null) {
            loadFromBundle(ResourceBundle.getBundle(bundleName, locale));
        }
    }

    /**
     * Copy all the translations from the given bundle and store in the Properties object
     * overwriting the existing translations if necessary
     * @param bundle bundle object loaded from file
     */
    private void loadFromBundle(ResourceBundle bundle) {
        for (String key : bundle.keySet()) {
            localTexts.setProperty(key, bundle.getString(key));
        }
    }

    /**
     * Choose a locale based on the given code
     * @param language code for locale
     * @return Locale object if available, otherwise null
     */
    private static Locale getLocale(String language) {
        if (language.length() == 2) {
            return new Locale(language);
        }
        else if (language.length() == 5 && language.charAt(2) == '_') {
            return new Locale(language.substring(0, 2), language.substring(3));
        }
        return null;
    }

    public String getText(String key) {
        return localTexts.getProperty(key, key);
    }

    /** @return text formatted with the given string parameter */
    public String getTextWithString(String key, String param)
    {
        String localText = getText(key);
        try {
            localText = String.format(localText, param);
        }
        catch (Exception e) {
            // printf formatting didn't work, maybe the placeholders are wrong?
            System.err.println("String formatting for key '" + key + "' with parameter '" + param
                    + "' threw exception: " + e.getMessage());
        }
        return localText;
    }
}
