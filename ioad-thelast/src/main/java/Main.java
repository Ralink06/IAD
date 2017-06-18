import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import kmeans.*;
import kmeans.Point;
import model.MLP;
import model.Transformation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


public class Main {

    private static Transformation trans;


    private static boolean activeBias = true;
    private static int hiddenNumber = 5;
    private static double LEARNING_RATE = 0.3;
    private static int MAX_ITERATIONS = 5000;
    private static double MIN_ERROR = 0.00000001;
    private static int centroidsNumber = 20;
    private List<kmeans.Point> points = new ArrayList<>();
    private static XYSeriesCollection error = new XYSeriesCollection();
    private static XYSeriesCollection aproximation = new XYSeriesCollection();
    private static XYSeries expectedPlot = new XYSeries("Expected");
    private static XYSeries resultsPlot = new XYSeries("Results");


    static File numbers = new File(Main.class.getClassLoader().getResource("numbers.txt").getFile());

    public static void main(String[] args) throws FileNotFoundException {

        Queue<KMeans> fiveAttempts = new PriorityQueue<>((o1, o2) -> Double.compare(o1.getEndError(), o2.getEndError()));

        for (int i = 0; i < 5; i++)
            fiveAttempts.add(new KMeans(readTrainingData(numbers), centroidsNumber));




        System.out.print(fiveAttempts.poll().getEndError());
    }

    private static List<Point> readTrainingData(File file) throws FileNotFoundException {
        List<Point> trainingData = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            Point point = new Point(Double.valueOf(scanner.next()), Double.valueOf(scanner.next()));
            trainingData.add(point);
        }

        return trainingData;
    }


}