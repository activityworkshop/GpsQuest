package tim.quest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TriggerTest {
    @Test
    public void testMatchSingleCondition() {
        Trigger trigger = new Trigger("trigger1");
        Condition condition = new Condition("ghost", "library", "eq");
        trigger.addCondition(condition);
        VariableMap vars = new VariableMap();
        vars.addVariable("ghost", "kitchen");
        trigger.setVariables(vars);

        // condition not satisfied
        Assertions.assertFalse(trigger.allConditionsMatch());

        // Modify and check again - condition now satisfied
        vars.setValue("ghost", "library");
        Assertions.assertTrue(trigger.allConditionsMatch());
    }

    @Test
    public void testMatchBothConditions() {
        Trigger trigger = new Trigger("trigger1");
        trigger.addCondition(new Condition("haveArmour", "yes", "eq"));
        trigger.addCondition(new Condition("haveSword", "yes", "eq"));
        VariableMap vars = new VariableMap();
        vars.addVariable("haveArmour", "no");
        vars.addVariable("haveSword", "no");
        trigger.setVariables(vars);

        // conditions not satisfied
        Assertions.assertFalse(trigger.allConditionsMatch());

        // Armour but no sword
        vars.setValue("haveArmour", "yes");
        vars.setValue("haveSword", "no");
        Assertions.assertFalse(trigger.allConditionsMatch());
        // Sword but no armour
        vars.setValue("haveArmour", "no");
        vars.setValue("haveSword", "yes");
        Assertions.assertFalse(trigger.allConditionsMatch());

        // Armour and sword
        vars.setValue("haveArmour", "yes");
        vars.setValue("haveSword", "yes");
        Assertions.assertTrue(trigger.allConditionsMatch());
    }

    /** Recipient of the timer firing */
    private static class Target {
        int numFires = 0;
    }

    @Test
    public void testStartTimer() {
        Trigger trigger = new Trigger("trigger1");
        Timer timer = new Timer("timer", 1, "", false);
        trigger.addTimer(timer);
        Target target = new Target();
        timer.addTrigger(() -> {target.numFires++; return true;});
        Assertions.assertEquals(0, target.numFires);

        // Fire trigger, this should start the timer
        trigger.fire();
        Assertions.assertEquals(0, target.numFires);
        // Wait for the timer to finish, this should activate the second trigger
        TimerUtils.waitForSeconds(1.5);
        Assertions.assertEquals(1, target.numFires);
    }
}
