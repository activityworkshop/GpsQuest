package tim.quest.model;

import java.util.ArrayList;

public abstract class Zone extends QuestObject {
    private boolean visible = true;
    private final String enterTriggerString;
    private final String exitTriggerString;
    private final ArrayList<Trigger> enterTriggers = new ArrayList<>();
    private final ArrayList<Trigger> exitTriggers = new ArrayList<>();


    public Zone(String id, String enterTriggers, String exitTriggers) {
        super(id);
        enterTriggerString = preventNullString(enterTriggers);
        exitTriggerString = preventNullString(exitTriggers);
    }

    public abstract boolean containsPoint(Point point);

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getEnterTriggerString() {
        return enterTriggerString;
    }

    public String getExitTriggerString() {
        return exitTriggerString;
    }

    public void addEnterTrigger(Trigger trigger) {
        if (trigger != null) {
            enterTriggers.add(trigger);
        }
    }

    public int getNumEnterTriggers() {
        return enterTriggers.size();
    }

    public void addExitTrigger(Trigger trigger) {
        if (trigger != null) {
            exitTriggers.add(trigger);
        }
    }

    public int getNumExitTriggers() {
        return exitTriggers.size();
    }
}
