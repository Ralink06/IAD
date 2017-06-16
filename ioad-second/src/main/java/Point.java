import lombok.Data;

/**
 * Created by ralink on 15.04.17.
 */
@Data
class Point {
    private double x = 0;
    private double y = 0;
    private int clusterId = 0;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distanceToPoint(Point point) {
        return Math.sqrt(Math.pow((x - point.getX()), 2) + Math.pow((y - point.getY()), 2));
    }
}
