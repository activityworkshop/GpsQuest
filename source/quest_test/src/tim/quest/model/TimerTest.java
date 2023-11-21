package tim.quest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimerTest {

    /** Recipient of the timer firing */
    private static class Target {
        int numFires = 0;
    }

    @Test
    public void testSingleOneSecondFire() {
        Target target = new Target();
        Timer timer = new Timer("timer1", 1, "", false);
        timer.addTrigger(() -> {target.numFires++; return true;});
        Assertions.assertEquals(0, target.numFires);

        // Start timer and wait for 3 secs
        timer.start();
        TimerUtils.waitForSeconds(3);
        Assertions.assertEquals(1, target.numFires);
    }

    @Test
    public void testMultipleTimerStarts() {
        Target target = new Target();
        Timer timer = new Timer("timer1", 1, "", false);
        timer.addTrigger(() -> {target.numFires++; return true;});
        Assertions.assertEquals(0, target.numFires);

        // Start timer several times and wait for 3 secs
        for (int i=0; i<5; i++) {
            timer.start();
        }
        TimerUtils.waitForSeconds(3);
        Assertions.assertEquals(1, target.numFires);
    }

    @Test
    public void testMultipleTimerStartsConsecutive() {
        Target target = new Target();
        Timer timer = new Timer("timer1", 1, "", false);
        timer.addTrigger(() -> {target.numFires++; return true;});
        Assertions.assertEquals(0, target.numFires);

        // Start timer several times and wait between each one
        for (int i=0; i<5; i++) {
            timer.start();
            TimerUtils.waitForSeconds(2);
        }
        Assertions.assertEquals(5, target.numFires);
    }

    @Test
    public void testRepeatingTimer() {
        Target target = new Target();
        Timer timer = new Timer("timer1", 1, "", true);
        timer.addTrigger(() -> {target.numFires++; return true;});
        Assertions.assertEquals(0, target.numFires);

        // Start timer and wait
        timer.start();
        TimerUtils.waitForSeconds(5.5); // expect then exactly 5 firings
        timer.stop();
        TimerUtils.waitForSeconds(5.5); // expect no more firings after stop
        Assertions.assertEquals(5, target.numFires);
    }
}
