package kmeans;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Cluster {

    private List<Point> points;
    private Point centroid;

    Cluster() {
        this.points = new ArrayList<>();
        this.centroid = null;
    }

    List<Point> getPoints() {
        return points;
    }

    void addPoint(Point point) {
        points.add(point);
    }

    public Point getCentroid() {
        return centroid;
    }

    void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    void clearPoints() {
        points.clear();
    }

    public double calculateCost() {
        if (points.size() == 0) {
            return 0;
        }
        double temp = 0;
        for (Point point : points) {
            temp = temp + Math.abs(point.distanceToPoint(this.centroid)) * Math.abs(point.distanceToPoint(this.centroid));
        }
        return temp / points.size();
    }

}
