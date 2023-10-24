package tim.quest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConditionTest {

    @Test
    public void testApplyEq() {
        Condition equalsCondition = new Condition("a", "4", "eq");
        Assertions.assertEquals("a", equalsCondition.getVariableName());
        Assertions.assertTrue(equalsCondition.apply("4"));
        Assertions.assertFalse(equalsCondition.apply("5"));
    }

    @Test
    public void testApplyNe() {
        Condition condition = new Condition("Abc", "h", "ne");
        Assertions.assertEquals("Abc", condition.getVariableName());
        Assertions.assertTrue(condition.apply("4"));
        Assertions.assertTrue(condition.apply("H"));
        Assertions.assertFalse(condition.apply("h"));
    }

    @Test
    public void testApplyGt() {
        Condition condition = new Condition("v", "4", "gt");
        for (int i=-4; i<10; i++) {
            Assertions.assertEquals(i > 4, condition.apply("" + i));
        }
    }

    @Test
    public void testApplyLt() {
        Condition condition = new Condition("v", "2", "lt");
        for (int i=-4; i<10; i++) {
            Assertions.assertEquals(i < 2, condition.apply("" + i));
        }
    }
}
