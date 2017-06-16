import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class Main {
    private static final int CLUSTER_NUMBER = 6;

    private static boolean forgy = true;
    private static double globalErrorForgy = 1000;
    private static double globalErrorRandom = 1000;
    private static final boolean DIFFERENCE = false;

    private static XYSeriesCollection resultForgy = new XYSeriesCollection();
    private static XYSeriesCollection resultRandom = new XYSeriesCollection();
    private static XYSeriesCollection errorResults = new XYSeriesCollection();

    private static XYSeries centroidsRandom = new XYSeries("Centroids");
    private static XYSeries centroidsForgy = new XYSeries("Centroids");
    private static XYSeries errorForgy = new XYSeries("ErrorForgy");
    private static XYSeries errorRandom = new XYSeries("ErrorRandom");
    private static XYSeries pointsSeries = new XYSeries("Points");

    private static List<List<Double>> errorDivider = new ArrayList<>(10);
    private static List<List<Double>> errorBars = new ArrayList<>(10);
    private static List<Cluster> clusters = new ArrayList<>();
    private static List<Point> points = new ArrayList<>(10000);
    private static List<Point> oldPoints = new ArrayList<>(10000);
    private static List<Double> errors = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        for (int w = 0; w < 10; w++) {
            errorDivider.add(new ArrayList<>());
            errorBars.add(new ArrayList<>());
        }

        points = readData();
        Random rand = new Random();

        int counter5 = 0;
        int i;
        int iterForgy = 0;
        int iterRandom = 0;
        double error;
        double oldError;
        while (counter5 < 10) {
            error = 0;
            oldError = 0;
            i = 0;
            clusters.clear();
            for (int p = 0; p < CLUSTER_NUMBER; p++) {

                if (!forgy) {
                    clusters.add(new Cluster(generateRandomPoint(), p));
                }
                //forgy
                if (forgy) {
                    int temp = rand.nextInt(10000);
                    clusters.add(new Cluster(points.get(temp), p));
                }
            }
            for(int z=0;z<10000;z++){
                Random random = new Random();
                clusters.get(random.nextInt(CLUSTER_NUMBER-1)).addPoint(points.get(z));
            }
            clustersUpdate();

            while (true) {
                for (int z = 0; z < 10000; z++) {
                    oldPoints.set(z, new Point(points.get(z).getX(), points.get(z).getY())).setClusterId(points.get(z).getClusterId());
                    oldPoints.get(z).setClusterId(points.get(z).getClusterId());
                }
                oldError = 0;
                for (Cluster cluster1 : clusters) {
                    oldError = oldError + cluster1.calculateCost();
                }
                oldError = oldError / CLUSTER_NUMBER;


                error = 0;
                if (forgy) {
                    centroidsForgy.clear();
                } else {
                    centroidsRandom.clear();
                }
                assignePointsToClusters();
                clustersUpdate();

                for (Cluster cluster : clusters) {
                    error = error + cluster.calculateCost();
                }
                error = error / CLUSTER_NUMBER;

                if (forgy) {
                    if (DIFFERENCE) {
                        errorDivider.get(counter5).add(Math.abs(error - oldError));
                    } else {
                        errorDivider.get(counter5).add(error);
                    }
                    errorBars.get(counter5).add(error);
                } else {
                    if (DIFFERENCE) {
                        errorDivider.get(counter5).add(Math.abs(error - oldError));
                    } else {
                        errorDivider.get(counter5).add(error);
                    }
                    errorBars.get(counter5).add(error);
                }
                int counter = 0;
                points.set(0, new Point(0, 0));
                for (int z = 0; z < 10000; z++) {
                    if (oldPoints.get(z).getClusterId() == points.get(z).getClusterId()) counter++;
                }
                i++;
                if (counter == 10000) {
                    break;

                }
            }

            System.out.println(error);
            errors.add(error);
            counter5++;
            if (forgy) {
                if (error < globalErrorForgy) {
                    globalErrorForgy = error;
                    iterForgy = i;
                    resultForgy.removeAllSeries();
                    resultForgy.addSeries(centroidsForgy);
                    resultForgy.addSeries(pointsSeries);
                }
            } else {
                if (error < globalErrorRandom) {
                    globalErrorRandom = error;
                    iterRandom = i;
                    resultRandom.removeAllSeries();
                    resultRandom.addSeries(centroidsRandom);
                    resultRandom.addSeries(pointsSeries);
                }
            }

            if (counter5 == 5) {
                forgy = false;
            }
        }


        PointsChart forgyChart = new PointsChart("Init method: forgy " + "         Centroids: "
                + CLUSTER_NUMBER + "         Iter: " + iterForgy + "         EndError: "
                + globalErrorForgy, resultForgy);
        createPlotCharts(forgyChart);

        //randomplot
        PointsChart randomChart = new PointsChart("Init method: RANDOM " + "         Centroids: "
                + CLUSTER_NUMBER + "         Iter: " + iterRandom + "         EndError: "
                + globalErrorRandom, resultRandom);

        createPlotCharts(randomChart);

        List<Integer> sizesForgy = new ArrayList<>();
        List<Integer> sizesRandom;

        for (int b = 0; b < 10; b++) {
            sizesForgy.add(errorDivider.get(b).size());
        }

        sizesRandom = sizesForgy.subList(5, 10);
        sizesForgy = sizesForgy.subList(0, 5);
        Collections.sort(sizesRandom);
        Collections.sort(sizesForgy);


        List<Double> errorForgyList = new ArrayList<>();
        List<Double> errorRandomList = new ArrayList<>();
        int dividerError = 5;
        double temp;
        for (int w = 0; w < sizesForgy.get(0); w++) {
            temp = 0;
            for (int z = 0; z < 5; z++) {
                temp = temp + errorDivider.get(z).get(w);
            }
            if (w > 2) errorForgy.add(w, temp / dividerError);
        }

        for (int w = 0; w < sizesRandom.get(0); w++) {
            temp = 0;
            for (int z = 5; z < 10; z++) {
                temp = temp + errorDivider.get(z).get(w);
            }
            if (w > 2) errorRandom.add(w, temp / dividerError);
        }

        //DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        // słupki crossy liczenie
        for (int w = 0; w < sizesForgy.get(0); w++) {
            temp = 0;
            for (int z = 0; z < 5; z++) {
                temp = temp + errorBars.get(z).get(w);
            }
            errorForgyList.add(temp / dividerError);
        }

        int counter;
        XYSeries bars = new XYSeries("Difference Random - Forgy");

        if (sizesForgy.get(0) < sizesRandom.get(0)) {
            counter = sizesForgy.get(0);
        } else {
            counter = sizesRandom.get(0);
        }

        for (int w = 0; w < counter; w++) {
            temp = 0;
            for (int z = 5; z < 10; z++) {
                temp = temp + errorBars.get(z).get(w);
            }
            errorRandomList.add(temp / dividerError);
        }

        for (int w = 0; w < counter; w++) {
            if (w > 2) bars.add(w, errorRandomList.get(w) - errorForgyList.get(w));
            System.out.println(errorRandomList.get(w) - errorForgyList.get(w));
        }

        errorResults.addSeries(errorForgy);
        errorResults.addSeries(errorRandom);
        errorResults.addSeries(bars);
        ErrorChart errorChart = new ErrorChart("Error", errorResults);
        errorChart.pack();
        RefineryUtilities.centerFrameOnScreen(errorChart);
        errorChart.setVisible(true);
    }

    private static List<Point> readData() {
        File file = new File(Main.class.getClassLoader().getResource("attract.txt").getFile());
        List<Point> points = new ArrayList<>(10000);

        Scanner scanner;
        try {
            scanner = new Scanner(file);
            for (int i = 0; i < 10000; i++) {
                String temp = scanner.nextLine();
                String[] point = temp.split(",");
                points.add(new Point(Double.parseDouble(point[0]), Double.parseDouble(point[1])));
                oldPoints.add(new Point(0, 0));
                pointsSeries.add(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
            }
            return points;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addClustersPoints() {
        for (int i = 0; i < CLUSTER_NUMBER; i++) {
            XYSeries series = new XYSeries("Cluster ID: " + i);
            for (int j = 0; j < 10000; j++) {
                if (points.get(j).getClusterId() == i) series.add(points.get(j).getX(), points.get(j).getY());
            }
            //resultForgy.addSeries(series);
        }
    }

    private static Point generateRandomPoint() {
        Random r = new Random();
        double valueX = -9.297321 + (11.866289 + 9.297321) * r.nextDouble();
        double valueY = -11.17438 + (7.9134046 + 11.17438) * r.nextDouble();
        return new Point(valueX, valueY);
    }

    private static void assignePointsToClusters() {
        double temp = 0;
        int max = 0;


        for (Point point : points) {
            temp = point.distanceToPoint(clusters.get(0).getXy());
            max = 0;
            for (int i = 0; i < clusters.size(); i++) {
                double distance = point.distanceToPoint(clusters.get(i).getXy());
                if (distance < temp) {
                    temp = distance;
                    max = i;
                }
            }

            clusters.get(max).addPoint(point);
            point.setClusterId(max);
            //System.out.println(points.get(j).getX() + " " + points.get(j).getY() + " ID: " + points.get(j).getClusterId());
        }


    }

    private static void clustersUpdate() {
        for (int i = 0; i < clusters.size(); i++) {
            double x = 0.0;
            double y = 0.0;
            for (int j = 0; j < clusters.get(i).getPoints().size(); j++) {
                x = x + clusters.get(i).getPoints().get(j).getX();
                y = y + clusters.get(i).getPoints().get(j).getY();
            }


            if (clusters.get(i).getPoints().size() == 0) {
                clusters.get(i).setXy(generateRandomPoint());
                //System.out.println("Cluster repointing: " + i);
            } else {
//                clusters.get(i).setXy(new Point(x / clusters.get(i).getPoints().size(),y / clusters.get(i).getPoints().size()));
                clusters.get(i).getXy().setX(x / clusters.get(i).getPoints().size());
                clusters.get(i).getXy().setY(y / clusters.get(i).getPoints().size());
            }


        }

        for (Cluster cluster : clusters) {
            if (forgy) {
                centroidsForgy.add(cluster.getXy().getX(), cluster.getXy().getY());
            } else {
                centroidsRandom.add(cluster.getXy().getX(), cluster.getXy().getY());
            }
            //clusters.get(z).removePoints();
        }
    }

    private static void printClustersXY() {
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("ID: " + i + " " + clusters.get(i).getXy().getX() + " " + clusters.get(i).getXy().getY());
        }
    }

    private static void createPlotCharts(PointsChart chart) {
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

}
