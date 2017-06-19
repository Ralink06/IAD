package model;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import kmeans.KMeans;
import kmeans.Point;
import kmeans.Cluster;
import lombok.Data;
import model.layer.HiddenLayer;
import model.layer.OutputLayer;
import model.neuron.RadialNeuron;

import java.util.ArrayList;
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

    private MLP(List<Point> input, KMeans kMeans, int hiddenSize, double learningRate, double eps, int iteration) {
        this.kMeans = kMeans;
        this.hiddenSize = hiddenSize;
        this.learningRate = learningRate;
        this.eps = eps;
        this.input = input;
        this.iteration = iteration;
        generateRandomWeights(new Random());

    }


    public void train() {
        trainHidden();
        trainOutput();
    }

    private void trainHidden() {

        List<Point> centroidsToHidden = getCentroidsFromKMeans(kMeans.getClusters());
        for (int i = 0; i < hiddenSize;i++){
            hiddenLayer.addNeuron(new RadialNeuron(centroidsToHidden.get(i)));
        }
        hiddenLayer.calculateNeuronWidth();
    }

    private void trainOutput(){

    }

    private List<Point> getCentroidsFromKMeans(List<Cluster> clusters) {
        List<Point> returned = new ArrayList<>();
        for (Cluster a : clusters) {
            returned.add(a.getCentroid());
        }
        return returned;
    }

    public List<Double> forwardPropagation(List<Double> input) {
        inputLayer.setLayerInput(input);
        hiddenLayer.setLayerInput(propagateLayer(inputLayer, hiddenLayer, weightsForHidden, true));
        outputLayer.setLayerInput(propagateLayer(hiddenLayer, outputLayer, weightsForOutput, false));

        return outputLayer.getLayerOutput();
    }

    private List<Double> propagateLayer(Layer firstLayer, Layer secondLayer, double[][] layerWeights, boolean sigmoid) {
        List<Double> output = new ArrayList<>();

        for (int j = 1; j < secondLayer.getLayerSize(); j++) {
            double passedValue = 0.0;
            for (int i = 0; i < firstLayer.getLayerSize(); i++) {
                //System.out.println(layerWeights[j][i]);
                passedValue += layerWeights[j][i] * firstLayer.getNeuron(i);
            }
            if (sigmoid) output.add(activate(passedValue));
            else output.add(passedValue);
        }

        return output;
    }


    private void generateRandomWeights(Random random) {

        for (int j = 1; j < hiddenLayer.getLayerSize(); j++) {
            for (int i = 0; i < inputLayer.getLayerSize(); i++) {
                double temp = generateRandom(random);
//                System.out.print("HIDDEN ");
//                System.out.print(temp+ " ");
                weightsForHidden[j][i] = temp;
            }
            System.out.println("");
        }

        for (int j = 1; j < outputLayer.getLayerSize(); j++) {
            for (int i = 0; i < hiddenLayer.getLayerSize(); i++) {
                double temp = generateRandom(random);
//                System.out.print("OUTPUT ");
//                System.out.print(temp +" ");
                weightsForOutput[j][i] = temp;
            }
        }
    }

    private double activate(double value) {
        return 1.0 / (1.0 + Math.exp(-value));
    }

    public static Double sigmoidDerivative(Double value) {
        return Math.exp(value) / ((Math.exp(value) + 1) * (Math.exp(value) + 1));
    }

    private double generateRandom(Random random) {
        return random.nextDouble() * 2 + (-1);
    }


    public void printWeights() {

        for (int j = 1; j < hiddenLayer.getLayerSize(); j++) {
            for (int i = 0; i < inputLayer.getLayerSize(); i++) {
                System.out.print("HIDDEN ");
                System.out.println(weightsForHidden[j][i]);
            }
            System.out.println("");
        }

        for (int j = 1; j < outputLayer.getLayerSize(); j++) {
            for (int i = 0; i < hiddenLayer.getLayerSize(); i++) {
                System.out.println("OUTPUT ");
                System.out.println(weightsForOutput[j][i]);

            }
            System.out.println("");
        }
    }


}
