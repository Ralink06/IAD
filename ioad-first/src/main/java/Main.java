import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.MLP;
import model.Transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {

    private static Transformation trans;
    private static boolean activeBias;
    private static int hiddenNumber;

    public static void main(String[] args) throws FileNotFoundException {
        activeBias = false;
        hiddenNumber = 3;

        MLP mlp = new MLP.MlpBuilder()
                .inputNeurons(4)
                .hiddenNeurons(hiddenNumber)
                .outputNeurons(4)
                .bias(activeBias)
                .build();

        List<List<Double>> trainingData = readTrainingData();

        trans = new Transformation(mlp, trainingData, trainingData);
        trans.perform();

        trainingData.forEach(doubles -> {
            mlp.forwardPropagation(doubles)
                    .stream()
                    .filter(n -> n != 1.0)
                    .map(n -> n + " ")
                    .forEach(System.out::print);
            System.out.println();
        });

        launch(args);
    }

    private static List<List<Double>> readTrainingData() throws FileNotFoundException {
        File file = new File("C:/transformation.txt");
        List<List<Double>> trainingData = new ArrayList<>();

        Scanner scanner = new Scanner(file);

        for (int i = 0; i < 4; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                if (scanner.hasNextInt()) {
                    row.add(scanner.nextDouble());
                }
            }
            trainingData.add(row);
        }

        return trainingData;
    }


    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Iteration");

        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);


        XYChart.Series series = trans.getSeries();


        lineChart.setTitle("LR: " + trans.getLearningRate() + "\nMomentum: " + trans.getMOMENTUM() + "\nBias: " + activeBias + "\nW ukrytej: " + hiddenNumber + "\nIter: " + trans.getIterator() + "\nCost: " + trans.getCost());

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.setCreateSymbols(false);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

}