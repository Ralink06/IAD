import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.MLP;
import model.Transformation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static Transformation trans;


    private static boolean activeBias = true;
    private static int hiddenNumber = 5;
    private static double LEARNING_RATE = 0.3;
    private static int MAX_ITERATIONS = 5000;
    private static double MIN_ERROR = 0.00000001;
    private static XYSeriesCollection error = new XYSeriesCollection();
    private static XYSeriesCollection aproximation = new XYSeriesCollection();
    private static XYSeries expectedPlot = new XYSeries("Expected");
    private static XYSeries resultsPlot = new XYSeries("Results");


    static File numbers = new File("/home/ralink/sise/zad2/src/main/resources/numbers.txt");
    static File results = new File("/home/ralink/sise/zad2/src/main/resources/results.txt");


    public static void main(String[] args) throws FileNotFoundException {


        MLP mlp = new MLP.MlpBuilder()
                .inputNeurons(1)
                .hiddenNeurons(hiddenNumber)
                .outputNeurons(1)
                .bias(activeBias)
                .build();


        mlp.printWeights();
        List<List<Double>> trainingData = readTrainingData(numbers);
        List<List<Double>> resultsData = readTrainingData(results);

        trans = new Transformation(mlp, trainingData, resultsData, LEARNING_RATE, MAX_ITERATIONS, MIN_ERROR);
        trans.perform();
        trainingData.forEach(doubles -> {
            mlp.forwardPropagation(doubles)
                    .stream()
                    .filter(n -> n != 1.0)
                    .map(n -> n + " ");
                    //.forEach(System.out::print);
            //System.out.println();
        });
        mlp.printWeights();

        double error2 = trans.getCost();
        DecimalFormat df = new DecimalFormat("###.#####");
        df.setRoundingMode(RoundingMode.CEILING);

        //ERROR
        String descirption = "LearningRate: " + LEARNING_RATE + "&Bias: " + activeBias + "&Hidden Neurons: " + hiddenNumber + "&End Error: " + df.format(error2) + "&Iteration: " + trans.getIterator() +  "&Time: " + trans.getTimeCost() + "ms";
        error.addSeries(trans.getSeries());
        ErrorChartJFree errorChartJFree = new ErrorChartJFree("", error, descirption);
        errorChartJFree.pack();
        RefineryUtilities.centerFrameOnScreen(errorChartJFree);
        errorChartJFree.setVisible(true);



        //APPROXIMATION
        List<Double> temp = new ArrayList<>();
        for(int i=0;i<100;i++){
            resultsPlot.add(i+1,mlp.forwardPropagation(trainingData.get(i)).get(1));
        }

        expected(expectedPlot, trainingData);
        aproximation.addSeries(expectedPlot);
        aproximation.addSeries(resultsPlot);
        AproximationChartJFree errorChartJFree2 = new AproximationChartJFree("", aproximation, descirption);
        errorChartJFree2.pack();
        RefineryUtilities.centerFrameOnScreen(errorChartJFree2);
        errorChartJFree2.setVisible(true);




    }

    private static List<List<Double>> readTrainingData(File file) throws FileNotFoundException {
        List<List<Double>> trainingData = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        for (int i = 0; i < 100; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < 1; j++) {
                row.add(Double.valueOf(scanner.nextLine()));
            }
            trainingData.add(row);
        }

        return trainingData;
    }

    private static void expected(XYSeries series, List<List<Double>> training) {
        for (int i = 0; i < 100; i++) {
            series.add(i + 1, Double.valueOf(Math.sqrt(training.get(i).get(0))));
        }
    }


}