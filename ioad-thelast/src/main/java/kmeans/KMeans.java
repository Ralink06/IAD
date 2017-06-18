package kmeans;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ralink on 18.06.17.
 */
@Data
public class KMeans {

    private List<Point> points = new ArrayList<>();
    private int clustersNumber;
    private List<Cluster> clusters = new ArrayList<>();
    private List<Double> errorList = new ArrayList<>();
    private double endError = 0;



    public KMeans(List<Point> input,int clustersNumber){
        this.points = input;
        this.clustersNumber = clustersNumber;
        run();
    }


    public void run() {
        boolean finish = false;
        boolean changeFlag = false;
        int iterator = 0;
        List<Point> prevPoints = getCentroids();
        List<Point> currentPoints;
        initClusters();
        while(!finish) {

            clearClusters();
            assignPointsToClusters();
            updateCentroids();
            currentPoints = getCentroids();
            errorList.add(Math.abs(calculateClustersError()));
            changeFlag = checkIfCentroidMoved(prevPoints,currentPoints);
            if(!changeFlag && iterator!=0){
                finish=true;
            }
            iterator++;
            prevPoints = currentPoints;
        }

        }

    private void initClusters() {
        Collections.shuffle(points);
        for (int i = 0; i < clustersNumber; i++) {
            Cluster cluster = new Cluster();
            cluster.setCentroid(points.get(i));
            clusters.add(cluster);
        }
    }

    private void assignPointsToClusters() {
        double max = Double.MAX_VALUE;
        double min;
        double distance;
        int closest = 0;

        for (Point point : points) {
            min = max;
            for (int i = 0; i < clustersNumber; i++) {
                distance = point.distanceToPoint(clusters.get(i).getCentroid());
                if (distance < min) {
                    min = distance;
                    closest = i;
                }
            }
            clusters.get(closest).addPoint(point);
        }
    }

    private void updateCentroids() {
        for (Cluster cluster : clusters) {
            double x = 0;
            double y = 0;
            List<Point> list = cluster.getPoints();
            int size = list.size();

            for(Point point : list){
                x+= point.getX();
                y+= point.getY();
            }

            Point centroid = cluster.getCentroid();
            if (size > 0) {
                double centroidX = x/size;
                double centroidY = y/size;
                centroid.setLocation(centroidX,centroidY);
            }

        }
    }

    private boolean checkIfCentroidMoved(List<Point> prevCentroids, List<Point> currentCentroids) {
        for (int i = 0; i < prevCentroids.size(); i++) {
            if (prevCentroids.get(i).distanceToPoint(currentCentroids.get(i)) != 0) {
                return true;
            }
        }
        return false;
    }

    private List<Point> getCentroids() {
        List<Point> centroids = new ArrayList<>(clustersNumber);
        for (Cluster cluster : clusters) {
            Point centroidCopy = new Point(cluster.getCentroid());
            centroids.add(centroidCopy);
        }
        return centroids;
    }

    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.clearPoints();
        }
    }

    private double calculateClustersError() {
        double output = 0;
        for (Cluster cluster : clusters) {
            output += cluster.calculateCost();
        }

        output /= this.clustersNumber;

        return output;
    }

    public double getEndError(){
        return errorList.get(errorList.size()-1);
    }

}
