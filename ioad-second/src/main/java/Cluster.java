import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cluster {
    private List<Point> points = new ArrayList<>();
    private Point xy;
    private int id;

    Cluster(Point xy, int id) {
        this.xy = xy;
        this.id = id;
    }

    void addPoint(Point point) {
        points.add(point);
    }

    public void removePoints() {
        points = new ArrayList<>();
    }

    public double distanceToOldCluster(Cluster cluster) {
        return Math.pow(this.xy.getX() - cluster.getXy().getX(), 2) + Math.pow(this.xy.getY() - cluster.getXy().getY(), 2);
    }

    double calculateCost() {
        if (points.size() == 0) {
            return 0;
        }
        double temp = 0;
        for (Point point : points) {
            temp = temp + Math.abs(point.distanceToPoint(this.xy)) * Math.abs(point.distanceToPoint(this.xy));
        }
        return temp / points.size();
    }

}
