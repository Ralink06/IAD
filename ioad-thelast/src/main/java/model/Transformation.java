package model;


import javafx.scene.chart.XYChart;
import org.jfree.data.xy.XYSeries;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Transformation {

    private double LEARNING_RATE;
    private int MAX_ITERATIONS;
    private double MIN_ERROR;
    private int iterator = 0;
    private double cost = 0.0;
    private long timeCost;
    private MLP mlp;
    private List<List<Double>> inputsMatrix;
    private List<List<Double>> expectedResult;
    private static XYSeries series = new XYSeries("Error");

    public Transformation(MLP mlp, List<List<Double>> inputsMatrix, List<List<Double>> expectedResult, double LEARNING_RATE, int MAX_ITERATIONS, double MIN_ERROR) {
        this.mlp = mlp;
        this.inputsMatrix = inputsMatrix;
        this.expectedResult = expectedResult;
        this.LEARNING_RATE=LEARNING_RATE;
        this.MAX_ITERATIONS=MAX_ITERATIONS;
        this.MIN_ERROR=MIN_ERROR;

    }

    public void perform() {
        iterator = 0;

        LocalTime startTime = LocalTime.now();
        do {
            cost = 0.0;
            iterator++;
            for (int currentEpoch = 0; currentEpoch < inputsMatrix.size(); currentEpoch++) {
                List<Double> trainingResult = mlp.train(inputsMatrix.get(currentEpoch),
                        expectedResult.get(currentEpoch), LEARNING_RATE);

                cost += calculateCost(trainingResult, expectedResult.get(currentEpoch));
            }
            series.add(iterator,cost/2);
            //quadraticError.add(cost);
            //iteration.add(iterator);
        } while (iterator < MAX_ITERATIONS && cost > MIN_ERROR);
        LocalTime endTime = LocalTime.now();
        timeCost = ChronoUnit.MILLIS.between(startTime,endTime);
    }

    private double calculateCost(List<Double> trainingResult, List<Double> expectedResult) {
        double costSum = 0.0;
        double localError;

        for (int i = 0; i < expectedResult.size(); i++) {
            localError = expectedResult.get(i) - trainingResult.get(i + 1);
            costSum += localError * localError;
        }

        return costSum;
    }

    public int getIterator() {
        return iterator;
    }

    public double getCost() {
        return cost;
    }

    public long getTimeCost() {
        return timeCost;
    }

    public static XYSeries getSeries() {
        return series;
    }

    public static void setSeries(XYSeries series) {
        Transformation.series = series;
    }
}