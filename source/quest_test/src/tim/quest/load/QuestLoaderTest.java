package tim.quest.load;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tim.quest.Findings;
import tim.quest.model.Quest;

import java.io.File;

public class QuestLoaderTest {
    @Test
    public void testLoadTextNotZip() {
        Findings findings = new Findings();
        File questFile = getDataFile("textfile.quest");
        Assertions.assertThrows(QuestFileException.class, () -> QuestLoader.fromFile(questFile, findings));
    }

    private File getDataFile(String filename) {
        return new File(getClass().getResource("data/" + filename).getPath());
    }

    @Test
    public void testLoadBinaryNotZip() {
        Findings findings = new Findings();
        File questFile = getDataFile("pngfile.quest");
        Assertions.assertThrows(QuestFileException.class, () -> QuestLoader.fromFile(questFile, findings));
    }

    @Test
    public void testLoadZipWithoutXml() {
        Findings findings = new Findings();
        File questFile = getDataFile("zippedtext.quest");
        Assertions.assertThrows(QuestFileException.class, () -> QuestLoader.fromFile(questFile, findings));
    }

    @Test
    public void testLoadEmptyXml() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("emptyxml.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("", quest.getName());
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Start scene id"));
    }

    @Test
    public void testLoadStartSceneNotFound() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("scenenotfound.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("Start Scene not found", quest.getName());
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Start scene 'Intro'"));
    }

    @Test
    public void testLoadStartSceneFound() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("scenefound.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("Start Scene found", quest.getName());
        Assertions.assertTrue(findings.allOk());
    }

    @Test
    public void testMultipleXmlInZip() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("multiplexml.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("Start Scene found", quest.getName());
        Assertions.assertFalse(findings.hasErrors());
        Assertions.assertTrue(FindingsChecks.containsWarning(findings, "another.xml"));
    }

    @Test
    public void testDuplicateVariables() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("duplicatevariables.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "defined multiple times"));
    }

    @Test
    public void testDuplicateTriggers() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("duplicatetriggers.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "defined multiple times"));
    }

    @Test
    public void testDuplicateZones() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("duplicatezones.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "defined multiple times"));
    }

    @Test
    public void testNoZonesDefined() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("nozones.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "No zones defined"));
    }

    @Test
    public void testZonesUsingUndefinedTrigger() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("zonetriggernotfound.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "refers to trigger 'T1'"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "refers to trigger 'T2'"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "does not have any valid triggers"));
    }

    @Test
    public void testPointZonesWithWrongRadius() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("pointwithwrongradius.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P1' has improperly defined point or radius"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P2' has improperly defined point or radius"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P3' has improperly defined point or radius"));
    }

    @Test
    public void testPolygonsWithTooFewNodes() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("polygonswithfewnodes.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P1' has improperly defined nodes"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P2' has improperly defined nodes"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P3' has improperly defined nodes"));
    }

    @Test
    public void testInvalidCoordinates() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("pointswithwrongcoordinates.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P1' has improperly defined point coordinates"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P2' has a node with improperly defined coordinates"));
    }

    @Test
    public void testDuplicateTrigger() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("duplicatetrigger.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' is defined multiple times"));
    }

    @Test
    public void testTriggerUReadingWrongVariable() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("triggercallswrongvariable.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' refers to variable 'hasKey' which cannot be found"));
    }

    @Test
    public void testTriggerSettingWrongVariable() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("triggersetswrongvariable.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' modifies variable 'defeatedDragon' which cannot be found"));
    }

    @Test
    public void testTriggerStartsWrongTimer() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("triggerstartswrongtimer.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' refers to timer 'stormcoming' which cannot be found"));
    }

    @Test
    public void testTriggerGoesToWrongScene() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("triggergoestowrongscene.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' calls scene 'planelanded' but this scene was not found"));
    }

    @Test
    public void testDuplicateScenes() throws QuestFileException {
        Findings findings = new Findings();
        File questFile = getDataFile("duplicatescenes.quest");
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Scene id 'Intro' is defined multiple times"));
    }
}
