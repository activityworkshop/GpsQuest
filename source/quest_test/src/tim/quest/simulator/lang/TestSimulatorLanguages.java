package tim.quest.simulator.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class TestSimulatorLanguages {
    /** Check the consistency of the languages (missing translations) and generate the completion file */
    @Test
    public void testLanguages() throws IOException {
        String[] languages = new String[] {"de", "en"};
        ArrayList<String> result = new ArrayList<>();
        URL url = getClass().getClassLoader().getResource(".");
        File textsDirectory = new File(new File(url.getPath()), "../quest_sim/tim/quest/simulator/lang");
        // Firstly, load the descriptions
        Properties descriptions = loadTextsFile(textsDirectory, "quest-descriptions.properties");
        boolean complete = true;
        for (String lang : languages) {
            Properties texts = loadTextsFile(textsDirectory, "quest-texts_" + lang + ".properties");
            int numTexts = 0;
            // Check if all of the descriptions have been translated
            for (Object key : descriptions.keySet()) {
                if (texts.containsKey(key)) {
                    numTexts++;
                }
                else {
                    System.out.println("Missing text for language '" + lang + "', key='" + key + "'");
                    complete = false;
                }
            }
            // Also check if there are any extra texts which shouldn't be there
            for (Object key : texts.keySet()) {
                if (!descriptions.containsKey(key)) {
                    System.out.println("Unwanted text for language '" + lang + "', key='" + key + "'");
                }
            }
            result.add(lang + " " + numTexts);
        }
        // Sort the list and output to file
        Collections.sort(result);
        try (FileWriter writer = new FileWriter("completion.txt")) {
            for (String r : result) {
                writer.write(r);
                writer.write('\n');
            }
        }
        Assertions.assertTrue(complete);
    }

    private Properties loadTextsFile(File directory, String filename) throws IOException {
        Properties texts = new Properties();
        texts.load(new FileInputStream(new File(directory, filename)));
        return texts;
    }
}
