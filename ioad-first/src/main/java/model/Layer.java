package model;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    private List<Double> neurons;
    private int layerSize;
    private boolean withBias;

    public Layer(int layerSize, boolean withBias) {
        neurons = new ArrayList<>();
        this.withBias = withBias;
        this.layerSize = layerSize + 1; // + 1 for bias
    }

    private void addBias() {
        if (withBias) {
            neurons.add(1.0);
        } else {
            neurons.add(0.0);
        }
    }

    public int getLayerSize() {
        return layerSize;
    }

    public double getNeuron(int index) {
        return neurons.get(index);
    }

    public List<Double> getLayerOutput() {
        return neurons;
    }

    public void setLayerInput(List<Double> input) {
        neurons.clear();
        addBias();

        neurons.addAll(input);
    }
}
