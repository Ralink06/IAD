package model.neuron;

import kmeans.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ralink on 18.06.17.
 */
public class Neuron {
    private List<Double> weights = new ArrayList<>();


    public Neuron(int inputSize) {
        Random random = new Random();
        for (int i = 0; i < inputSize; i++) {
            weights.add(random.nextDouble());
        }
    }

    public void updateWeights(List<Double> input, Point output, double learningRate) {
        double neuronOutput = calculateOutput(input);
        for (int i = 0; i < weights.size(); i++) {
            double delta = output.getY() - neuronOutput;
            weights.set(i, weights.get(i) + (learningRate * delta * input.get(i)));
        }
    }

    public Double calculateOutput(List<Double> values) {
        Double sum = 0.0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i) * weights.get(i);
        }
        return sum;
    }
}
