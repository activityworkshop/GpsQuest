package tim.quest.model;

public class PointZone extends Zone {
    private final Point centrePoint;
    private final int radiusMetres;

    // TODO: Need to worry about date line?

    public PointZone(String id, String enterTriggers, String exitTriggers,
                     Point centrePoint, int radiusMetres) {
        super(id, enterTriggers, exitTriggers);
        this.centrePoint = centrePoint;
        this.radiusMetres = radiusMetres;
    }

    @Override
    public boolean containsPoint(Point point) {
        // TODO: First check rectangle, then calculate real distance
        return false;
    }
}
