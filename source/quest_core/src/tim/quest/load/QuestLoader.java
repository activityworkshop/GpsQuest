package tim.quest.load;

import tim.quest.Findings;
import tim.quest.model.Quest;
import tim.quest.model.QuestFileVisitor;
import tim.quest.xml.ParseException;
import tim.quest.xml.QuestXmlHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class QuestLoader {

    /** Create a Quest object from the specified directory or zip file */
    public static Quest fromFile(File file, Findings findings) throws QuestFileException {
        if (!file.exists() || !file.canRead()) {
            throw new QuestFileException("Cannot read file '" + file.getAbsolutePath() + "'");
        }
        if (file.isFile()) {
            return fromZipFile(file, findings);
        }
        if (file.isDirectory()) {
            return fromDirectory(file.getAbsolutePath(), findings);
        }
        return null;
    }

    private static Quest fromZipFile(File file, Findings findings) throws QuestFileException {
        Quest quest = null;
        try (ZipFile zf = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                // System.out.println("Found entry: " + zipEntry.getName());
                if (!zipEntry.isDirectory()) {
                    if (zipEntry.getName().equals("main.xml")) {
                        System.out.println("Found main.xml");
                        if (quest != null) { // not possible to have two entries with the same name, right?
                            throw new QuestFileException("more than one main.xml file");
                        }
                        QuestXmlHandler xmlHandler = new QuestXmlHandler();
                        xmlHandler.parseXmlStream(zf.getInputStream(zipEntry));
                        quest = new QuestBuilder(xmlHandler.getModel(), findings).buildQuest();
                    }
                    else if (zipEntry.getName().endsWith(".xml")) {
                        findings.addWarning("Xml file '" + zipEntry.getName() + "' ignored");
                    }
                    else if (zipEntry.getName().startsWith("res/")) {
                        System.out.println("Found resource: " + zipEntry.getName().substring(4));
                        // TODO: Just add paths to list
                    }
                }
            }
        } catch (IOException e) {
            throw new QuestFileException("Cannot read file: " + e.getMessage());
        } catch (ParseException e) {
            throw new QuestFileException("Parsing error reading file: " + e.getMessage());
        }
        if (quest == null) {
            throw new QuestFileException("No main.xml file found inside zip");
        }
        return quest;
    }

    private static Quest fromDirectory(String directory, Findings findings) throws QuestFileException {
        // Walk through file tree, do same parsing as above
        QuestFileVisitor visitor = new QuestFileVisitor(findings);
        try {
            Files.walkFileTree(Path.of(directory), visitor);
        } catch (IOException e) {
            throw new QuestFileException("Error reading files: " + e.getMessage());
        }
        return visitor.getQuest();
    }
}
