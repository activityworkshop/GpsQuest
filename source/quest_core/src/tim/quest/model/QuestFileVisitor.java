package tim.quest.model;

import tim.quest.Findings;
import tim.quest.load.QuestBuilder;
import tim.quest.xml.ParseException;
import tim.quest.xml.QuestXmlHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class QuestFileVisitor extends SimpleFileVisitor<Path> {
    private final Findings findings;
    private final ArrayList<String> resourcePaths = new ArrayList<>();
    private Quest quest = null;

    public QuestFileVisitor(Findings findings) {
        this.findings = findings;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        // TODO: Only deal with paths relative to the root - need root parameter in constructor?
        final String filename = file.getFileName().toString();
        if (filename.equals("main.xml")) {
            if (quest != null) {
                findings.addError("Multiple files with name 'main.xml' found");
            }
            QuestXmlHandler xmlHandler = new QuestXmlHandler();
            try {
                xmlHandler.parseXmlStream(new FileInputStream(file.toFile()));
            } catch (ParseException e) {
                throw new IOException(e.getMessage());
            }
            quest = new QuestBuilder(xmlHandler.getModel(), findings).buildQuest();
        }
        else if (filename.endsWith(".xml")) {
            findings.addWarning("Xml file '" + filename + "' found and ignored");
        }
        // TODO: Also deal with resource files, add to list
        return super.visitFile(file, attrs);
    }

    public Quest getQuest() {
        return quest;
    }
}
