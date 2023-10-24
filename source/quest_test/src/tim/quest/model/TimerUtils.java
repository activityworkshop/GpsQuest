package tim.quest.model;

public class TimerUtils {
    public static void waitForSeconds(double numSeconds) {
        try {
            Thread.sleep((long) (numSeconds * 1000.0));
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("sleeping shouldn't be interrupted");
        }
    }
}
