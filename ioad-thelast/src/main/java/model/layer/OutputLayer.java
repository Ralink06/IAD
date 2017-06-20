package model.layer;

import kmeans.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.neuron.Neuron;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutputLayer {
    private Neuron neuron;
    private double learningRate;

    public Double calculateOutput(List<Double> input) {
        return neuron.calculateOutput(input);
    }

    public void train(List<Double> input, Point point) {
        neuron.updateWeights(input, point, learningRate);
    }

}
