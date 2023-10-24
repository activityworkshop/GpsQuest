package tim.quest.model;

import java.util.*;

public class Quest {
    private String name;
    private String author;
    private final HashMap<String, String> descriptions = new HashMap<>();
    private final HashSet<String> languages = new HashSet<>();

    private final VariableMap variables = new VariableMap();
    private final HashMap<String, Timer> timers = new HashMap<>();
    private final ArrayList<Zone> zones = new ArrayList<>();
    private final HashMap<String, Trigger> triggers = new HashMap<>();
    private final HashMap<String, Scene> scenes = new HashMap<>();
    private final ArrayList<String> resourcePaths = new ArrayList<>();


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public void addDescription(String language, String description) {
        descriptions.put(language, description);
        languages.add(language);
    }

    public HashMap<String, String> getDescriptions() {
        return descriptions;
    }

    public int getNumVariables() {
        return variables.size();
    }

    public List<Zone> getZones() {
        return zones;
    }

    public int getNumTimers() {
        return timers.size();
    }

    public Collection<Timer> getTimers() {
        return timers.values();
    }

    public Timer getTimer(String id) {
        return timers.get(id);
    }

    public int getNumTriggers() {
        return triggers.size();
    }

    public Collection<Trigger> getTriggers() {
        return triggers.values();
    }

    public boolean hasTrigger(String id) {
        return triggers.containsKey(id);
    }

    public Trigger getTrigger(String id) {
        return triggers.get(id);
    }

    public int getNumScenes() {
        return scenes.size();
    }

    public Scene getScene(String id) {
        return scenes.get(id);
    }

    public void addVariable(String id, String value) {
        if (id != null && !id.equals("")) {
            variables.put(id, value == null ? "" : value);
        }
    }

    public boolean hasVariable(String id) {
        return variables.containsKey(id);
    }

    public void addTimer(String id, int period, String trigger, boolean repeating) {
        if (id != null && !id.equals("") && period > 0) {
            timers.put(id, new Timer(id, period, trigger, repeating));
        }
    }

    public boolean hasTimer(String id) {
        return timers.containsKey(id);
    }

    public void setStartScene(Scene startScene) {
        // TODO: Can I store a single object if the scene has different languages and I don't know the language yet?
        // or does each Scene hold multiple languages?
    }

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    /** @return true if the given zone id has already been defined */
    public boolean hasZone(String id) {
        for (Zone zone : zones) {
            if (zone.getId() != null && zone.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void addTrigger(Trigger trigger) {
        triggers.put(trigger.getId(), trigger);
        trigger.setVariables(variables);
    }

    public void addScene(Scene scene) {
        scenes.put(scene.getId(), scene);
    }

    public boolean hasScene(String id) {
        return scenes.containsKey(id);
    }
}
