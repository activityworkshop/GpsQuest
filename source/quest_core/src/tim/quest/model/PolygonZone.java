package tim.quest.model;

import java.util.ArrayList;

public class PolygonZone extends Zone {
    private final ArrayList<Point> points = new ArrayList<>();

    // TODO: Need to worry about date line?

    public PolygonZone(String id, String enterTriggers, String exitTriggers) {
        super(id, enterTriggers, exitTriggers);
    }

    public void addNode(Point point) {
        if (point != null) {
            points.add(point);
        }
    }

    @Override
    public boolean containsPoint(Point testPoint) {
        // TODO: Go through each line segment, cross product with vector to testPoint and see if it's on the right side or the left side of this segment
        return false;
    }

    public boolean isValid() {
        return points.size() > 2 && isConvex();
    }

    private boolean isConvex() {
        // TODO: Go through each line segment, cross product with previous segment to find sign of z component
        // if always turning right or always turning left then convex
        // if there are duplicate points then not convex or not valid
        // same behaviour whether closed or not (extra point invisibly added)
        return false;
    }
}
