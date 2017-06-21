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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private int iteration;

    private HiddenLayer hiddenLayer = new HiddenLayer();
    private OutputLayer outputLayer = new OutputLayer();

    private List<Double> outputErrorList = new ArrayList<>();

    private ChartHolder chartHolder = new ChartHolder();

    public MLP(final List<Point> input,
               final KMeans kMeans,
               final int hiddenSize,
               final double learningRate,
               final double eps,
               final int iteration) {
        this.kMeans = kMeans;
        this.hiddenSize = hiddenSize;
        this.learningRate = learningRate;
        this.eps = eps;
        this.input = input;
        this.iteration = iteration;
    }


    public void train() {

        //hidden train
        List<Point> centroidsToHidden = getCentroidsFromKMeans(kMeans.getClusters());
        for (int i = 0; i < hiddenSize; i++) {
            hiddenLayer.addNeuron(new RadialNeuron(centroidsToHidden.get(i)));
        }
        hiddenLayer.calculateNeuronWidth();

        //output train
        List<Point> points = new ArrayList<>(input);
        outputLayer.setNeuron(new Neuron(hiddenSize));
        outputLayer.setLearningRate(learningRate);
        outputLayer.getNeuron().getWeights().forEach(System.out::println);
        System.out.println();
        double error = Double.MAX_VALUE;
        for (int i = 0; i < iteration && error > eps; i++) {
            error = 0;
            for (Point a : points) {
                outputLayer.train(hiddenLayer.calculateOutput(a.getX()), a);
                double outputError = a.getY() -  outputLayer.calculateOutput(hiddenLayer.calculateOutput(a.getX()));
                error += outputError * outputError;
            }
            error = error / points.size();
            //System.out.println(error);
            outputErrorList.add(error);
            chartHolder.addPointsToOutputPlot(i, error);
        }

        outputLayer.getNeuron().getWeights().forEach(System.out::println);

        createChartsPoints();
        chartHolder.addOutputPointsPlotToErrorChart();
        chartHolder.displayChart(hiddenLayer);
    }


    private List<Point> getCentroidsFromKMeans(List<Cluster> clusters) {
        return clusters.stream()
                .map(Cluster::getCentroid)
                .collect(Collectors.toList());
    }


    private void createChartsPoints() {
        input.forEach(chartHolder::addPointsToPlotPoints);
        chartHolder.addPointsPlotToApproximationChart();

        kMeans.getClusters().forEach(chartHolder::addPointsToClustersPlot);
        chartHolder.addClustersPlotToApproximationChart();

        for(int i =0;i<kMeans.getErrorList().size();i++){
            chartHolder.addPointsToKmeansOutput(i,kMeans.getErrorList().get(i));
        }

        double min = -4;
        for (int i = 0; i < 1000; i++) {
            chartHolder.addPointsToApproximationPlot(min, outputLayer.calculateOutput(hiddenLayer.calculateOutput(min)));
            min = min + 8 / 1000.0;
        }
        chartHolder.addApproximationPlotToApproximationChart();




//        //KMEANS error
//        for (int i = 0; i < kMeans.getErrorList().size(); i++) {
//            chartHolder.addPointsToKmeansOutput(i + 1, kMeans.getErrorList().get(i));
//        }
//        chartHolder.addKmeansPlotToOutputErrorChart();

    }
}
