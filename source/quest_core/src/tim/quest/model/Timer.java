package tim.quest.model;

import java.util.ArrayList;

public class Timer extends QuestObject {
    private final int periodSecs;
    private final String triggerString;
    private final boolean repeating;
    private final ArrayList<TriggerInterface> triggers = new ArrayList<>();
    private Thread thread = null;
    private boolean stopped = false;


    public Timer(String id, int periodSecs, String trigger, boolean repeating) {
        super(id);
        this.periodSecs = periodSecs;
        this.triggerString = preventNullString(trigger);
        this.repeating = repeating;
    }

    public String getTriggerString() {
        return triggerString;
    }

    public void addTrigger(TriggerInterface trigger) {
        if (trigger != null) {
            triggers.add(trigger);
        }
    }

    public int getNumTriggers() {
        return triggers.size();
    }

    public synchronized void start() {
        if (thread == null) {
            thread = new Thread(this::run);
            stopped = false;
            thread.start();
        }
    }

    private void run() {
        do {
            try {
                Thread.sleep(periodSecs * 1000L);
            }
            catch (InterruptedException ignored) {}
            fireTriggers();
        }
        while (repeating && !stopped);
        synchronized (this) {
            thread = null;
        }
    }

    private void fireTriggers() {
        for (TriggerInterface trigger : triggers) {
            if (!stopped) {
                trigger.fire();
            }
        }
    }

    public void stop() {
        stopped = true;
    }
}
