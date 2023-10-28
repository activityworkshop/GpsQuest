package tim.quest.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointTest {
    @Test
    public void testIsValid() {
        Point point = new Point(47.0, 9.0);
        Assertions.assertTrue(point.isValid());

        point = new Point(47.0, 169.0);
        Assertions.assertTrue(point.isValid());

        point = new Point(-47.0, -9.0);
        Assertions.assertTrue(point.isValid());

        point = new Point(107.0, 107.0);
        Assertions.assertFalse(point.isValid());
    }
}
