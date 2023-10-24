package tim.quest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VariableModMapTest {
	@Test
	public void testSet() {
		VariableMap variables = new VariableMap();
		variables.addVariable("a", "blue");
		variables.addVariable("b", "red");

		VariableModMap modifier = new VariableModMap();
		modifier.addAction("a", "set", "yellow");
		modifier.apply(variables);

		Assertions.assertEquals("yellow", variables.getValue("a"));
		Assertions.assertEquals("red", variables.getValue("b"));
	}

	@Test
	public void testSetNonExisting() {
		VariableMap variables = new VariableMap();
		variables.addVariable("a", "blue");
		variables.addVariable("b", "red");

		VariableModMap modifier = new VariableModMap();
		modifier.addAction("c", "set", "yellow");
		Assertions.assertThrows(RuntimeException.class, () -> modifier.apply(variables));

		Assertions.assertEquals("blue", variables.getValue("a"));
		Assertions.assertEquals("red", variables.getValue("b"));
	}

	@Test
	public void testIncrement() {
		VariableMap variables = new VariableMap();
		variables.addVariable("a", "blue");
		variables.addVariable("b", "4");

		VariableModMap modifier = new VariableModMap();
		modifier.addAction("b", "inc", "2");
		modifier.apply(variables);

		Assertions.assertEquals("blue", variables.getValue("a"));
		Assertions.assertEquals("6", variables.getValue("b"));
	}

	@Test
	public void testMultipleActions() {
		VariableMap variables = new VariableMap();
		variables.addVariable("a", "blue");
		variables.addVariable("b", "4");
		variables.addVariable("c", "");

		VariableModMap modifier = new VariableModMap();
		modifier.addAction("a", "set", "purple");
		modifier.addAction("b", "inc", "12");
		modifier.addAction("c", "dec", "5");
		modifier.apply(variables);

		Assertions.assertEquals("purple", variables.getValue("a"));
		Assertions.assertEquals("16", variables.getValue("b"));
		Assertions.assertEquals("-5", variables.getValue("c"));
	}

	@Test
	public void testDecrement() {
		VariableMap variables = new VariableMap();
		variables.addVariable("a", "blue");
		variables.addVariable("b", "9");

		VariableModMap modifier = new VariableModMap();
		modifier.addAction("b", "dec", "1");
		modifier.apply(variables);

		Assertions.assertEquals("blue", variables.getValue("a"));
		Assertions.assertEquals("8", variables.getValue("b"));
	}
}
