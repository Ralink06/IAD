package model;

import kmeans.KMeans;
import kmeans.Point;
import kmeans.Cluster;
import lombok.Data;
import model.layer.HiddenLayer;
import model.layer.OutputLayer;
import model.neuron.Neuron;
import model.neuron.RadialNeuron;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import plots.AproximationChartJFree;
import plots.ErrorChartJFree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    private XYSeriesCollection outputErrorChart = new XYSeriesCollection();
    private XYSeries outputPlot = new XYSeries("Output Error");

    private XYSeriesCollection aproximationChart = new XYSeriesCollection();
    private XYSeries pointsPlot = new XYSeries("Points Plot");
    private XYSeries clustersPlot = new XYSeries("Centroids Plot");
    private XYSeries aproximationPlot = new XYSeries("Output Error");

    public MLP(List<Point> input, KMeans kMeans, int hiddenSize, double learningRate, double eps, int iteration) {
        this.kMeans = kMeans;
        this.hiddenSize = hiddenSize;
        this.learningRate = learningRate;
        this.eps = eps;
        this.input = input;
        this.iteration = iteration;

    }


    public void train() {
        trainHidden();
        trainOutput();

        outputErrorChart.addSeries(outputPlot);
        ErrorChartJFree errorChartJFree = new ErrorChartJFree("", outputErrorChart, "");
        errorChartJFree.pack();
        RefineryUtilities.centerFrameOnScreen(errorChartJFree);
        errorChartJFree.setVisible(true);

        addPointsToXYSeries();

        AproximationChartJFree aprox = new AproximationChartJFree("", aproximationChart, "");
        aprox.pack();
        RefineryUtilities.centerFrameOnScreen(aprox);
        aprox.setVisible(true);
    }

    private void trainHidden() {

        List<Point> centroidsToHidden = getCentroidsFromKMeans(kMeans.getClusters());
        for (int i = 0; i < hiddenSize; i++) {
            hiddenLayer.addNeuron(new RadialNeuron(centroidsToHidden.get(i)));
        }
        hiddenLayer.calculateNeuronWidth();
    }

    private void trainOutput() {
        List<Point> points = new ArrayList<>(input);
        outputLayer.setNeuron(new Neuron(hiddenSize));
        outputLayer.setLearningRate(learningRate);

        double error = Double.MAX_VALUE;
        for (int i = 0; i < iteration && error > eps; i++) {
            error = 0;
            Collections.shuffle(points);
            for (Point a : points) {
                outputLayer.train(hiddenLayer.calculateOutput(a.getX()), a);
                double outputError = a.getY() - propagateOutputLayer(a.getX());
                error += outputError * outputError;
            }
            error /= points.size();
            System.out.println(error);
            outputErrorList.add(error);
            outputPlot.add(i, error);
        }
    }

    private List<Point> getCentroidsFromKMeans(List<Cluster> clusters) {
        List<Point> returned = new ArrayList<>();
        for (Cluster a : clusters) {
            returned.add(a.getCentroid());
        }
        return returned;
    }


    public double propagateOutputLayer(double input) {
        return outputLayer.calculateOutput(hiddenLayer.calculateOutput(input));
    }

    public void addPointsToXYSeries() {
        for (Point a : input) {
            pointsPlot.add(a.getX(), a.getY());
        }
        aproximationChart.addSeries(pointsPlot);


        for (Cluster a : kMeans.getClusters()) {
            clustersPlot.add(a.getCentroid().getX(), a.getCentroid().getY());
        }
        aproximationChart.addSeries(clustersPlot);

        double min = -4;
        for (int i = 0; i < 1000; i++) {
            aproximationPlot.add(min,propagateOutputLayer(min));
            min = min + 8/1000.0;
        }

        aproximationChart.addSeries(aproximationPlot);
    }
}
