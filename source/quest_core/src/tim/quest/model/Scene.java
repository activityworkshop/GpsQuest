package tim.quest.model;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Scene extends QuestObject {
    private final HashMap<String, List<SceneObject>> contents = new HashMap<>();

    public Scene(String id) {
        super(id);
    }

    public void addContent(String language, List<SceneObject> content) {
        contents.put(language, content);
    }

    public List<SceneObject> getContent(String language) {
        List<SceneObject> result = contents.get(language);
        if (result == null) {
            // Fallback to unspecified language
            result = contents.get("");
        }
        return result;
    }

    public Set<String> getLanguages() {
        return contents.keySet();
    }
}
