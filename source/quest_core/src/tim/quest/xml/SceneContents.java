package tim.quest.xml;

import tim.quest.model.SceneObject;

import java.util.ArrayList;
import java.util.List;

public class SceneContents {
    private final String id;
    private final String language;
    private final ArrayList<SceneObject> contents = new ArrayList<>();

    public SceneContents(String id, String language) {
        this.id = id;
        this.language = (language == null ? "" : language);
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void addObject(SceneObject object) {
        contents.add(object);
    }

    public List<SceneObject> getContents() {
        return contents;
    }
}
