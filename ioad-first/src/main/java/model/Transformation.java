package model;


import javafx.scene.chart.XYChart;

import java.util.List;

public class Transformation {

    private static final double LEARNING_RATE = 0.3;
    private static final double MOMENTUM = 0.99;
    private static final int MAX_ITERATIONS = 10000;
    private static final double MIN_ERROR = 0.01;
    private int iterator = 0;
    private double cost = 0.0;
    private MLP mlp;
    private List<List<Double>> inputsMatrix;
    private List<List<Double>> expectedResult;
    private XYChart.Series series = new XYChart.Series();

    public Transformation(MLP mlp, List<List<Double>> inputsMatrix, List<List<Double>> expectedResult) {
        this.mlp = mlp;
        this.inputsMatrix = inputsMatrix;
        this.expectedResult = expectedResult;
    }

    public void perform() {
        iterator = 0;

        do {
            cost = 0.0;
            iterator++;
            for (int currentEpoch = 0; currentEpoch < inputsMatrix.size(); currentEpoch++) {
                List<Double> trainingResult = mlp.train(inputsMatrix.get(currentEpoch),
                        expectedResult.get(currentEpoch), LEARNING_RATE, MOMENTUM);

                cost += calculateCost(trainingResult, expectedResult.get(currentEpoch));
            }
            series.getData().add(new XYChart.Data(iterator, cost));
            //quadraticError.add(cost);
            //iteration.add(iterator);
        } while (iterator < MAX_ITERATIONS && cost > MIN_ERROR);

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

    public static double getLearningRate() {
        return LEARNING_RATE;
    }

    public static double getMOMENTUM() {
        return MOMENTUM;
    }

    public XYChart.Series getSeries() {
        return series;
    }

    public void setSeries(XYChart.Series series) {
        this.series = series;
    }
}