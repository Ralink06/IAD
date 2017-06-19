package kmeans;

import lombok.AllArgsConstructor;
import java.awt.geom.Point2D;


public class Point extends Point2D {

    private double x;
    private double y;

    public Point(Point p) {
        setLocation(p.getX(), p.getY());
    }

    public Point(double x, double y){ setLocation(x,y);}

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distanceToPoint(Point point) {
        return Math.sqrt(Math.pow((x - point.getX()), 2) + Math.pow((y - point.getY()), 2));
    }
}