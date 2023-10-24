package tim.quest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OperatorTest {

    @Test
    public void testEquals() {
        // Straightforward equals
        Assertions.assertTrue(Operator.EQUAL.apply("", ""));
        Assertions.assertTrue(Operator.EQUAL.apply("abc", "abc"));
        Assertions.assertTrue(Operator.EQUAL.apply("0", "0"));
        Assertions.assertTrue(Operator.EQUAL.apply("7", "7"));

        // Not equals
        Assertions.assertFalse(Operator.EQUAL.apply("abc", "ABC"));
        Assertions.assertFalse(Operator.EQUAL.apply("7", "0"));
        Assertions.assertFalse(Operator.EQUAL.apply("0", ""));
        Assertions.assertFalse(Operator.EQUAL.apply("0", "00"));

        // Including trim
        Assertions.assertTrue(Operator.EQUAL.apply("abc", "  abc"));
        Assertions.assertTrue(Operator.EQUAL.apply("0     ", "0"));
    }

    @Test
    public void testNotEquals() {
        // Straightforward equals
        Assertions.assertFalse(Operator.NOT_EQUAL.apply("", ""));
        Assertions.assertFalse(Operator.NOT_EQUAL.apply("abc", "abc"));
        Assertions.assertFalse(Operator.NOT_EQUAL.apply("0", "0"));
        Assertions.assertFalse(Operator.NOT_EQUAL.apply("7", "7"));

        // Not equals
        Assertions.assertTrue(Operator.NOT_EQUAL.apply("abc", "ABC"));
        Assertions.assertTrue(Operator.NOT_EQUAL.apply("7", "0"));
        Assertions.assertTrue(Operator.NOT_EQUAL.apply("0", ""));
        Assertions.assertTrue(Operator.NOT_EQUAL.apply("0", "00"));

        // Including trim
        Assertions.assertFalse(Operator.NOT_EQUAL.apply("abc", "  abc"));
        Assertions.assertFalse(Operator.NOT_EQUAL.apply("0     ", "0"));
    }

    @Test
    public void testGreaterThan() {
        // Straightforward integers
        Assertions.assertFalse(Operator.GREATER_THAN.apply("0", "0"));
        Assertions.assertFalse(Operator.GREATER_THAN.apply("7", "7"));
        Assertions.assertFalse(Operator.GREATER_THAN.apply("6", "9"));
        Assertions.assertFalse(Operator.GREATER_THAN.apply("-6", "2"));

        Assertions.assertTrue(Operator.GREATER_THAN.apply("1", "0"));
        Assertions.assertTrue(Operator.GREATER_THAN.apply("199", "198"));
        Assertions.assertTrue(Operator.GREATER_THAN.apply("5", "-1"));
        // Whitespace ignored, still compared
        Assertions.assertTrue(Operator.GREATER_THAN.apply(" 4 ", "3"));

        // Can't be parsed, so always false
        Assertions.assertFalse(Operator.GREATER_THAN.apply("", ""));
        Assertions.assertFalse(Operator.GREATER_THAN.apply("abc", "abc"));
        Assertions.assertFalse(Operator.GREATER_THAN.apply("A", "B"));
        Assertions.assertFalse(Operator.GREATER_THAN.apply("C2", "C3"));
        Assertions.assertFalse(Operator.GREATER_THAN.apply("C3", "C2"));
    }

    @Test
    public void testLessThan() {
        // Straightforward integers
        Assertions.assertFalse(Operator.LESS_THAN.apply("0", "0"));
        Assertions.assertFalse(Operator.LESS_THAN.apply("7", "7"));
        Assertions.assertTrue(Operator.LESS_THAN.apply("6", "9"));
        Assertions.assertTrue(Operator.LESS_THAN.apply("-6", "2"));

        Assertions.assertFalse(Operator.LESS_THAN.apply("1", "0"));
        Assertions.assertFalse(Operator.LESS_THAN.apply("199", "198"));
        Assertions.assertFalse(Operator.LESS_THAN.apply("5", "-1"));
        // Whitespace ignored, still compared
        Assertions.assertTrue(Operator.LESS_THAN.apply(" 2 ", "10 "));

        // Can't be parsed, so always false
        Assertions.assertFalse(Operator.LESS_THAN.apply("0", "B"));
        Assertions.assertFalse(Operator.LESS_THAN.apply("b", "0"));
        Assertions.assertFalse(Operator.LESS_THAN.apply("A", "B"));
        Assertions.assertFalse(Operator.LESS_THAN.apply("A", ""));
        Assertions.assertFalse(Operator.LESS_THAN.apply("C2", "C3"));
    }

    @Test
    public void testFromString() {
        Assertions.assertEquals(Operator.EQUAL, Operator.fromString("eq"));
        Assertions.assertEquals(Operator.EQUAL, Operator.fromString("EQ"));
        Assertions.assertEquals(Operator.EQUAL, Operator.fromString("EQ  "));
        Assertions.assertEquals(Operator.EQUAL, Operator.fromString(""));

        Assertions.assertEquals(Operator.NOT_EQUAL, Operator.fromString("ne"));
        Assertions.assertEquals(Operator.NOT_EQUAL, Operator.fromString(" ne"));
        Assertions.assertEquals(Operator.NOT_EQUAL, Operator.fromString("NE"));

        Assertions.assertEquals(Operator.LESS_THAN, Operator.fromString("lt"));
        Assertions.assertEquals(Operator.GREATER_THAN, Operator.fromString("Gt"));
    }
}
