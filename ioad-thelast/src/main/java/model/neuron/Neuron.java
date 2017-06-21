package model.neuron;

import kmeans.Point;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ralink on 18.06.17.
 */
@Data
public class Neuron {
    private List<Double> weights = new ArrayList<>();
    private boolean bias;

    public Neuron(int inputSize, boolean bias) {
        Random random = new Random();
        this.bias = bias;
        if (bias)
            for (int i = 0; i < inputSize + 1; i++) {
                weights.add(random.nextDouble());
            }
        else
            for (int i = 0; i < inputSize; i++) {
                weights.add(random.nextDouble());
            }

    }

    public void updateWeights(List<Double> input, Point output, double learningRate) {
        double neuronOutput = calculateOutput(input);
        if (bias)
            for (int i = 0; i < weights.size(); i++) {
                double delta = output.getY() - neuronOutput;
                if (i == weights.size() - 1)
                    weights.set(i, weights.get(i) + (learningRate * delta * 1));
                else
                    weights.set(i, weights.get(i) + (learningRate * delta * input.get(i)));

            }
        else {
            for (int i = 0; i < weights.size(); i++) {
                double delta = output.getY() - neuronOutput;

                weights.set(i, weights.get(i) + (learningRate * delta * input.get(i)));
            }

        }
    }

    public Double calculateOutput(List<Double> values) {
        Double sum = 0.0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i) * weights.get(i);
        }

        if (bias) sum += 1 * weights.get(values.size());
        return sum;
    }
}
