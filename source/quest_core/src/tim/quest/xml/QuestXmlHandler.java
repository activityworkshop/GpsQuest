package tim.quest.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class QuestXmlHandler extends DefaultHandler {
    private final XmlQuestModel model = new XmlQuestModel();
    private String language = null;
    private String currentText = null;
    private XmlProperties currentPolygon = null;
    private XmlProperties currentTrigger = null;
    private XmlProperties currentScene = null;


    public void parseXmlStream(InputStream inStream) throws ParseException {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(inStream, this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ParseException(e.getMessage());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("Description")) {
            language = attributes.getValue("lang");
        }
        else if (qName.equals("Variable")) {
            model.addVariable(attributes.getValue("id"), attributes.getValue("value"));
        }
        else if (qName.equals("Timer")) {
            model.addTimer(attributes.getValue("id"), attributes.getValue("period"),
                    attributes.getValue("trigger"), attributes.getValue("repeating"));
        }
        else if (qName.equals("StartScene")) {
            model.getMainProperties().set(XmlQuestModel.TAG_STARTSCENE, attributes.getValue("id"));
        }
        else if (qName.equals("Point")) {
            model.addPointZone(attributes.getValue("id"), attributes.getValue("lat"),
                    attributes.getValue("lon"), attributes.getValue("radius"),
                    attributes.getValue("enter"), attributes.getValue("exit"),
                    attributes.getValue("visible"));
        }
        else if (qName.equals("Polygon")) {
            currentPolygon = new XmlProperties();
            currentPolygon.set(XmlQuestModel.TAG_ID, attributes.getValue("id"));
            currentPolygon.set(XmlQuestModel.TAG_ENTER_TRIGGERS, attributes.getValue("enter"));
            currentPolygon.set(XmlQuestModel.TAG_EXIT_TRIGGERS, attributes.getValue("exit"));
        }
        else if (qName.equals("Node") && currentPolygon != null) {
            currentPolygon.setIndexed(XmlQuestModel.TAG_POINT_LATITUDE, attributes.getValue("lat"));
            currentPolygon.setIndexed(XmlQuestModel.TAG_POINT_LONGITUDE, attributes.getValue("lon"));
        }
        else if (qName.equals("Trigger")) {
            currentTrigger = new XmlProperties();
            currentTrigger.set(XmlQuestModel.TAG_ID, attributes.getValue("id"));
        }
        else if (qName.equals("Condition") && currentTrigger != null) {
            currentTrigger.setIndexed(XmlQuestModel.TAG_TRIGGER_CONDITION_VAR, attributes.getValue("var"));
            currentTrigger.setIndexed(XmlQuestModel.TAG_TRIGGER_CONDITION_VALUE, attributes.getValue("value"));
            currentTrigger.setIndexed(XmlQuestModel.TAG_TRIGGER_CONDITION_OPERATOR, attributes.getValue("comparison"));
        }
        else if (qName.equals("GoToScene") && currentTrigger != null) {
            final String sceneId = attributes.getValue("id");
            currentTrigger.set(XmlQuestModel.TAG_STARTSCENE, sceneId);
        }
        else if (qName.equals("StartTimer") && currentTrigger != null) {
            final String timerId = attributes.getValue("id");
            currentTrigger.set(XmlQuestModel.TAG_TRIGGER_TIMERID, timerId);
        }
        else if (qName.equals("SetVariable") && currentTrigger != null) {
            final String variableId = attributes.getValue("var");
            currentTrigger.setIndexed(XmlQuestModel.TAG_TRIGGER_SETVARIABLENAME, variableId);
            final String variableValue = attributes.getValue("value");
            currentTrigger.setIndexed(XmlQuestModel.TAG_TRIGGER_SETVALUE, variableValue);
        }
        else if (qName.equals("Scene")) {
            currentScene = new XmlProperties();
            currentScene.set(XmlQuestModel.TAG_ID, attributes.getValue("id"));
        }
        // TODO: Extract more scene details, make language-specific
        currentText = "";
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("Name")) {
            model.getMainProperties().set(XmlQuestModel.TAG_NAME, currentText);
        }
        else if (qName.equals("Author")) {
            model.getMainProperties().set(XmlQuestModel.TAG_AUTHOR, currentText);
        }
        else if (qName.equals("Description")) {
            model.getMainProperties().set(XmlQuestModel.TAG_DESCRIPTION, language, currentText);
        }
        else if (qName.equals("Polygon") && currentPolygon != null) {
            model.addPolygonZone(currentPolygon);
            currentPolygon = null;
        }
        else if (qName.equals("Trigger") && currentTrigger != null) {
            model.addTrigger(currentTrigger);
            currentTrigger = null;
        }
        else if (qName.equals("Scene") && currentScene != null) {
            model.addScene(currentScene);
            currentScene = null;
        }
        language = null;
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length).strip();
        currentText += content;
        super.characters(ch, start, length);
    }

    public XmlQuestModel getModel() {
        return model;
    }
}
