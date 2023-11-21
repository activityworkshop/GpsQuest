package tim.quest.model;

import tim.quest.QuestController;

import java.util.*;
import java.util.stream.Collectors;

public class Quest {
    private String name;
    private String author;
    private final HashMap<String, String> descriptions = new HashMap<>();

    private final VariableMap variables = new VariableMap();
    private final HashMap<String, Timer> timers = new HashMap<>();
    private final ArrayList<Zone> zones = new ArrayList<>();
    private final HashMap<String, Trigger> triggers = new HashMap<>();
    private final HashMap<String, Scene> scenes = new HashMap<>();
    private final ArrayList<String> resourcePaths = new ArrayList<>();
    private Scene startScene = null;
    // Note: the members above are loaded from the Quest file and contain the definition of the Quest.
    // The ones below hold the current state of the running Quest, and so maybe belong in a separate class?
    private String selectedLanguage = null;


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
    }

    public HashMap<String, String> getDescriptions() {
        return descriptions;
    }

    public String getDescription() {
        if (descriptions.isEmpty()) {
            return null;
        }
        if (descriptions.size() == 1) {
            // Only one description provided, so return that one
            for (String value : descriptions.values()) {
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            }
        }
        if (descriptions.containsKey(selectedLanguage)) {
            return descriptions.get(selectedLanguage);
        }
        if (descriptions.containsKey("")) {
            return descriptions.get("");
        }
        return null;
    }

    public int getNumLanguages() {
        return descriptions.size();
    }

    public List<String> getLanguages() {
        ArrayList<String> languages = new ArrayList<>(descriptions.keySet());
        Collections.sort(languages);
        return languages;
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
        // TODO: Give the current selectedLanguage to the Scene object
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

    public List<String> getVariableNames() {
        return variables.keySet().stream().sorted().collect(Collectors.toList());
    }

    public String getVariableValue(String name) {
        return variables.getValue(name);
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
        this.startScene = startScene;
    }

    public Scene getStartScene() {
        return startScene;
    }

    public Collection<Scene> getScenes() {
        return scenes.values();
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

    public Collection<String> getZoneNames() {
        return zones.stream().map(Zone::getId).sorted().collect(Collectors.toList());
    }

    public Zone getZone(String zoneId) {
        for (Zone zone : zones) {
            if (zone.getId().equals(zoneId)) {
                return zone;
            }
        }
        return null;
    }

    public void setController(QuestController controller) {
        for (Trigger trigger : getTriggers()) {
            trigger.setController(controller);
        }
    }

    // State
    public void selectLanguage(String language) {
        selectedLanguage = language;
    }

    public String getSelectedLanguage() {
        return selectedLanguage == null ? "" : selectedLanguage;
    }
}
