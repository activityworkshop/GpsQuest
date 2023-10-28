package tim.quest.model;

public class QuestObject {
    private final String id;

    public QuestObject(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    protected String preventNullString(String value) {
        return value == null ? "" : value;
    }
}
