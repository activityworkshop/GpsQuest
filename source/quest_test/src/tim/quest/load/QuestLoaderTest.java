package tim.quest.load;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tim.quest.Finding;
import tim.quest.model.Quest;

import java.util.ArrayList;

public class QuestLoaderTest {
    @Test
    public void testLoadTextNotZip() {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/textfile.quest").getPath();
        Assertions.assertThrows(QuestFileException.class, () -> QuestLoader.fromFile(questFile, findings));
    }

    @Test
    public void testLoadBinaryNotZip() {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/pngfile.quest").getPath();
        Assertions.assertThrows(QuestFileException.class, () -> QuestLoader.fromFile(questFile, findings));
    }

    @Test
    public void testLoadZipWithoutXml() {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/zippedtext.quest").getPath();
        Assertions.assertThrows(QuestFileException.class, () -> QuestLoader.fromFile(questFile, findings));
    }

    @Test
    public void testLoadEmptyXml() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/emptyxml.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("", quest.getName());
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Start scene id"));
    }

    @Test
    public void testLoadStartSceneNotFound() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/scenenotfound.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("Start Scene not found", quest.getName());
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Start scene 'Intro'"));
    }

    @Test
    public void testLoadStartSceneFound() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/scenefound.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("Start Scene found", quest.getName());
        Assertions.assertTrue(FindingsChecks.allOk(findings));
    }

    @Test
    public void testMultipleXmlInZip() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/multiplexml.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertEquals("Start Scene found", quest.getName());
        Assertions.assertFalse(FindingsChecks.containsError(findings));
        Assertions.assertTrue(FindingsChecks.containsWarning(findings, "another.xml"));
    }

    @Test
    public void testDuplicateVariables() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/duplicatevariables.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "defined multiple times"));
    }

    @Test
    public void testDuplicateTriggers() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/duplicatetriggers.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "defined multiple times"));
    }

    @Test
    public void testDuplicateZones() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/duplicatezones.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "defined multiple times"));
    }

    @Test
    public void testNoZonesDefined() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/nozones.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "No zones defined"));
    }

    @Test
    public void testZonesUsingUndefinedTrigger() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/zonetriggernotfound.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "refers to trigger 'T1'"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "refers to trigger 'T2'"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "does not have any valid triggers"));
    }

    @Test
    public void testPointZonesWithWrongRadius() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/pointwithwrongradius.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P1' has improperly defined point or radius"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P2' has improperly defined point or radius"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P3' has improperly defined point or radius"));
    }

    @Test
    public void testPolygonsWithTooFewNodes() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/polygonswithfewnodes.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P1' has improperly defined nodes"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P2' has improperly defined nodes"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P3' has improperly defined nodes"));
    }

    @Test
    public void testInvalidCoordinates() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/pointswithwrongcoordinates.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P1' has improperly defined point coordinates"));
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Zone 'P2' has a node with improperly defined coordinates"));
    }

    @Test
    public void testDuplicateTrigger() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/duplicatetrigger.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' is defined multiple times"));
    }

    @Test
    public void testTriggerUReadingWrongVariable() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/triggercallswrongvariable.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' refers to variable 'hasKey' which cannot be found"));
    }

    @Test
    public void testTriggerSettingWrongVariable() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/triggersetswrongvariable.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' modifies variable 'defeatedDragon' which cannot be found"));
    }

    @Test
    public void testTriggerStartsWrongTimer() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/triggerstartswrongtimer.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' refers to timer 'stormcoming' which cannot be found"));
    }

    @Test
    public void testTriggerGoesToWrongScene() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/triggergoestowrongscene.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Trigger 'T1' calls scene 'planelanded' but this scene was not found"));
    }

    @Test
    public void testDuplicateScenes() throws QuestFileException {
        ArrayList<Finding> findings = new ArrayList<>();
        String questFile = getClass().getResource("data/duplicatescenes.quest").getPath();
        Quest quest = QuestLoader.fromFile(questFile, findings);
        Assertions.assertNotNull(quest);
        Assertions.assertTrue(FindingsChecks.containsError(findings, "Scene id 'Intro' is defined multiple times"));
    }
}
