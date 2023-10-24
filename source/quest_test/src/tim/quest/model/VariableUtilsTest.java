package tim.quest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VariableUtilsTest {
	@Test
	public void testGetIntParseable() {
		Assertions.assertEquals(1, VariableUtils.getInt("1"));
		Assertions.assertEquals(101, VariableUtils.getInt("101"));
		Assertions.assertEquals(-1, VariableUtils.getInt("-1"));
		Assertions.assertEquals(14, VariableUtils.getInt(" 14  "));
	}

	@Test
	public void testGetIntNotParseable() {
		Assertions.assertEquals(0, VariableUtils.getInt(null));
		Assertions.assertEquals(0, VariableUtils.getInt(""));
		Assertions.assertEquals(0, VariableUtils.getInt(" "));
		Assertions.assertEquals(0, VariableUtils.getInt("abc"));
		Assertions.assertEquals(0, VariableUtils.getInt("3a"));
		Assertions.assertEquals(0, VariableUtils.getInt("1.5"));
		Assertions.assertEquals(0, VariableUtils.getInt("1 5"));
	}
}
