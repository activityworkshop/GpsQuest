package tim.quest.model;

public class Point {
    private final double latitude;
    private final double longitude;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isValid() {
        return Math.abs(latitude) < 90.0 && Math.abs(longitude) < 180.0;
    }
}
