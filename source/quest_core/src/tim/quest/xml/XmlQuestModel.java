package tim.quest.xml;

import java.util.ArrayList;
import java.util.List;

public class XmlQuestModel {
    public static final String TAG_NAME = "name";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_STARTSCENE = "startscene";
    public static final String TAG_ID = "id";
    public static final String TAG_VALUE = "value";
    public static final String TAG_TIMER_PERIOD = "period";
    public static final String TAG_TRIGGERS = "triggers";
    public static final String TAG_TIMER_REPEATING = "repeating";
    public static final String TAG_ZONE_RADIUS = "radius";
    public static final String TAG_POINT_LATITUDE = "latitude";
    public static final String TAG_POINT_LONGITUDE = "longitude";
    public static final String TAG_ENTER_TRIGGERS = "entertriggers";
    public static final String TAG_EXIT_TRIGGERS = "exittriggers";
    public static final String TAG_ZONE_VISIBLE = "visible";
    public static final String TAG_TRIGGER_CONDITION_VAR = "conditionvar";
    public static final String TAG_TRIGGER_CONDITION_VALUE = "conditionvalue";
    public static final String TAG_TRIGGER_CONDITION_OPERATOR = "conditionop";
    public static final String TAG_TRIGGER_TIMERID = "timerid";
    public static final String TAG_TRIGGER_SETVARIABLENAME = "setvariablename";
    public static final String TAG_TRIGGER_SETVALUE = "setvalue";
    public static final String TAG_TRIGGER_SETOPERATOR = "setop";

    private final XmlProperties mainProperties = new XmlProperties();
    private final ArrayList<XmlProperties> variables = new ArrayList<>();
    private final ArrayList<XmlProperties> timers = new ArrayList<>();
    private final ArrayList<XmlProperties> zones = new ArrayList<>();
    private final ArrayList<XmlProperties> triggers = new ArrayList<>();
    private final ArrayList<SceneContents> scenes = new ArrayList<>();


    public XmlProperties getMainProperties() {
        return mainProperties;
    }

    public void addVariable(String id, String initialValue) {
        XmlProperties variable = new XmlProperties();
        variable.set(TAG_ID, id);
        variable.set(TAG_VALUE, initialValue);
        variables.add(variable);
    }

    public void addTimer(String id, String period, String triggers, String repeating) {
        XmlProperties timer = new XmlProperties();
        timer.set(TAG_ID, id);
        timer.set(TAG_TIMER_PERIOD, period);
        timer.set(TAG_TRIGGERS, triggers);
        timer.set(TAG_TIMER_REPEATING, repeating);
        timers.add(timer);
    }

    public void addPointZone(String id, String lat, String lon, String radius, String enterTriggers,
                             String exitTriggers, String visible) {
        XmlProperties zone = new XmlProperties();
        zone.set(TAG_ID, id);
        zone.set(TAG_POINT_LATITUDE, lat);
        zone.set(TAG_POINT_LONGITUDE, lon);
        zone.set(TAG_ZONE_RADIUS, radius);
        zone.set(TAG_ENTER_TRIGGERS, enterTriggers);
        zone.set(TAG_EXIT_TRIGGERS, exitTriggers);
        zone.set(TAG_ZONE_VISIBLE, visible);
        zones.add(zone);
    }

    public void addPolygonZone(XmlProperties zone) {
        zones.add(zone);
    }

    public void addTrigger(XmlProperties trigger) {
        triggers.add(trigger);
    }

    public void addScene(SceneContents scene) {
        scenes.add(scene);
    }

    public List<XmlProperties> getVariables() {
        return variables;
    }

    public List<XmlProperties> getTimers() {
        return timers;
    }

    public List<XmlProperties> getZones() {
        return zones;
    }

    public List<XmlProperties> getTriggers() {
        return triggers;
    }

    public List<SceneContents> getScenes() {
        return scenes;
    }
}
