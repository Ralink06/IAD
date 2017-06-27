package model;

import kmeans.Cluster;
import kmeans.KMeans;
import kmeans.Point;
import lombok.Data;
import model.layer.HiddenLayer;
import model.layer.OutputLayer;
import model.neuron.Neuron;
import model.neuron.RadialNeuron;
import plots.ChartHolder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class MLP {
    private int hiddenSize;

    private double[][] weightsForHidden;
    private double[][] weightsForOutput;

    private KMeans kMeans;
    private double learningRate;
    private double eps;
    private List<Point> input = new ArrayList<>();
    private List<Point> training = new ArrayList<>();
    private int iteration;
    private boolean bias;

    private HiddenLayer hiddenLayer = new HiddenLayer();
    private OutputLayer outputLayer = new OutputLayer();

    private List<Double> outputErrorList = new ArrayList<>();

    private ChartHolder chartHolder = new ChartHolder();

    public MLP(final List<Point> input,
               final List<Point> training,
               final KMeans kMeans,
               final int hiddenSize,
               final double learningRate,
               final double eps,
               final int iteration,
               final boolean bias) {
        this.kMeans = kMeans;
        this.hiddenSize = hiddenSize;
        this.learningRate = learningRate;
        this.eps = eps;
        this.input = input;
        this.bias = bias;
        this.iteration = iteration;
        this.training=training;
    }


    public void train() {

        //hidden train
        LocalTime startTime = LocalTime.now();
        Queue<KMeans> fiveAttempts = new PriorityQueue<>(Comparator.comparingDouble(KMeans::getEndError));

        for (int i = 0; i < 20; i++) {
            fiveAttempts.add(new KMeans(training, hiddenSize));
        }

        this.kMeans = fiveAttempts.poll();
        List<Point> centroidsToHidden = getCentroidsFromKMeans(kMeans.getClusters());
        for (int i = 0; i < hiddenSize; i++) {
            hiddenLayer.addNeuron(new RadialNeuron(centroidsToHidden.get(i)));
        }
        hiddenLayer.calculateNeuronWidth();
        LocalTime endTime = LocalTime.now();
        //output train
        List<Point> points = new ArrayList<>(training);
        outputLayer.setNeuron(new Neuron(hiddenSize, bias));
        outputLayer.setLearningRate(learningRate);
//        outputLayer.getNeuron().getWeights().forEach(System.out::println);
//        System.out.println();
        double error = Double.MAX_VALUE;
        int i = 0;
        LocalTime startTimeOutput = LocalTime.now();
        for (i = 0; i < iteration && error > eps; i++) {
            error = 0;
            for (Point a : points) {
                outputLayer.train(hiddenLayer.calculateOutput(a.getX()), a);
                double outputError = a.getY() - outputLayer.calculateOutput(hiddenLayer.calculateOutput(a.getX()));
                error += outputError * outputError;
            }
            error = error / points.size();
            //System.out.println(error);
            outputErrorList.add(error);
            chartHolder.addPointsToOutputPlot(i, error);
        }
        LocalTime endTimeOutput = LocalTime.now();

//        outputLayer.getNeuron().getWeights().forEach(System.out::println);
        System.out.println("Czas uczenia ukrytej: " + ChronoUnit.MILLIS.between(startTime, endTime) + " ms");
        System.out.println("Czas uczenia wyjsciowej: " + ChronoUnit.MILLIS.between(startTimeOutput,endTimeOutput)+ " ms");
        System.out.println("Iteracje: "  + i );
        System.out.println("Błąd koncowy KMeans: " + kMeans.getEndError());
        System.out.println("Błąd koncowy wyjsciowy: " + outputErrorList.get(i - 1));

        createChartsPoints();
        chartHolder.addOutputPointsPlotToErrorChart();

        String descritpion = "";

        chartHolder.displayChart(hiddenLayer, descritpion);
    }


    private List<Point> getCentroidsFromKMeans(List<Cluster> clusters) {
        return clusters.stream()
                .map(Cluster::getCentroid)
                .collect(Collectors.toList());
    }


    private void createChartsPoints() {
        training.forEach(chartHolder::addPointsToPlotPoints);
        chartHolder.addPointsPlotToApproximationChart();

        kMeans.getClusters().forEach(chartHolder::addPointsToClustersPlot);
        chartHolder.addClustersPlotToApproximationChart();

        for (int i = 0; i < kMeans.getErrorList().size(); i++) {
            chartHolder.addPointsToKmeansOutput(i, kMeans.getErrorList().get(i));
        }

//        double min = -4;
//        for (int i = 0; i < 1000; i++) {
//            chartHolder.addPointsToApproximationPlot(min, outputLayer.calculateOutput(hiddenLayer.calculateOutput(min)));
//            min = min + 8 / 1000.0;
//        }
        List<Double> aproximationErrorPoints = new ArrayList<>();
        for(int i=0;i<input.size();i++){
            chartHolder.addPointsToApproximationPlot(input.get(i).getX(),outputLayer.calculateOutput(hiddenLayer.calculateOutput(input.get(i).getX())));
            aproximationErrorPoints.add(outputLayer.calculateOutput(hiddenLayer.calculateOutput(input.get(i).getX())));
        }
        chartHolder.addApproximationPlotToApproximationChart();

        double error = 0;
        for(int i=0;i<input.size();i++) {
            error = error + (input.get(i).getY() - aproximationErrorPoints.get(i)) * (input.get(i).getY() - aproximationErrorPoints.get(i));
        }

        System.out.println("Błąd średniokwadratowy: " + error);
//        //KMEANS error
//        for (int i = 0; i < kMeans.getErrorList().size(); i++) {
//            chartHolder.addPointsToKmeansOutput(i + 1, kMeans.getErrorList().get(i));
//        }
//        chartHolder.addKmeansPlotToOutputErrorChart();

    }
}
