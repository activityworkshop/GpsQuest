package tim.quest.load;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LoadUtilsTest {
    @Test
    public void testSplitNothing() {
        List<String> result = LoadUtils.splitByCommas(null);
        Assertions.assertTrue(result.isEmpty());

        result = LoadUtils.splitByCommas("");
        Assertions.assertTrue(result.isEmpty());

        result = LoadUtils.splitByCommas(",");
        Assertions.assertTrue(result.isEmpty());

        result = LoadUtils.splitByCommas("  ,  ,");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testSplitSingle() {
        List<String> result = LoadUtils.splitByCommas("Abc");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Abc", result.get(0));

        result = LoadUtils.splitByCommas(",Abc,,");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Abc", result.get(0));
    }

    @Test
    public void testSplitTwo() {
        List<String> result = LoadUtils.splitByCommas("Abc,,Def");
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Abc", result.get(0));
        Assertions.assertEquals("Def", result.get(1));
    }
}
