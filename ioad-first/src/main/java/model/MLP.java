package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MLP {
    private Layer inputLayer;
    private Layer hiddenLayer;
    private Layer outputLayer;

    private double[][] weightsForHidden;
    private double[][] weightsForOutput;

    private MLP(MlpBuilder builder) {
        inputLayer = new Layer(builder.numberOfInputNeurons, builder.withBias);
        hiddenLayer = new Layer(builder.numberOfHiddenNeurons, builder.withBias);
        outputLayer = new Layer(builder.numberOfOutputNeurons, builder.withBias);

        weightsForHidden = new double[builder.numberOfHiddenNeurons + 1][builder.numberOfInputNeurons + 1];
        weightsForOutput = new double[builder.numberOfOutputNeurons + 1][builder.numberOfHiddenNeurons + 1];

        generateRandomWeights(new Random());
    }


    public List<Double> train(List<Double> input, List<Double> expectedResult, double learningRate, double momentum) {
        List<Double> afterPropagation = forwardPropagation(input);
        backPropagation(expectedResult, learningRate, momentum);

        return afterPropagation;
    }

    private void backPropagation(List<Double> expectedResult, double learningRate, double momentum) {
        double[] errorsForOutput = new double[outputLayer.getLayerSize()];
        double[] errorsForHidden = new double[hiddenLayer.getLayerSize()];
        double sum = 0.0;

        for (int i = 1; i < outputLayer.getLayerSize(); i++) {
            Double neuron = outputLayer.getNeuron(i);
            errorsForOutput[i] = neuron * (1.0 - neuron) * (expectedResult.get(i - 1) - neuron);
        }


        for (int i = 0; i < hiddenLayer.getLayerSize(); i++) {
            for (int j = 1; j < outputLayer.getLayerSize(); j++) {
                sum += weightsForOutput[j][i] * errorsForOutput[j];
            }

            errorsForHidden[i] = hiddenLayer.getNeuron(i) * (1.0 - hiddenLayer.getNeuron(i)) * sum;
            sum = 0.0;
        }

        for (int j = 1; j < outputLayer.getLayerSize(); j++) {
            for (int i = 0; i < hiddenLayer.getLayerSize(); i++) {
                weightsForOutput[j][i] += learningRate * errorsForOutput[j] * hiddenLayer.getNeuron(i);
            }
        }

        for (int j = 1; j < hiddenLayer.getLayerSize(); j++) {
            for (int i = 0; i < inputLayer.getLayerSize(); i++) {
                weightsForHidden[j][i] += learningRate * errorsForHidden[j] * inputLayer.getNeuron(i) * momentum;
            }
        }

    }

    public List<Double> forwardPropagation(List<Double> input) {
        inputLayer.setLayerInput(input);
        hiddenLayer.setLayerInput(propagateLayer(inputLayer, hiddenLayer, weightsForHidden));
        outputLayer.setLayerInput(propagateLayer(hiddenLayer, outputLayer, weightsForOutput));

        return outputLayer.getLayerOutput();
    }

    private List<Double> propagateLayer(Layer firstLayer, Layer secondLayer, double[][] layerWeights) {
        List<Double> output = new ArrayList<>();

        for (int j = 1; j < secondLayer.getLayerSize(); j++) {
            double passedValue = 0.0;
            for (int i = 0; i < firstLayer.getLayerSize(); i++) {
                passedValue += layerWeights[j][i] * firstLayer.getNeuron(i);
            }
            output.add(activate(passedValue));
        }

        return output;
    }


    private void generateRandomWeights(Random random) {

        for (int j = 1; j < hiddenLayer.getLayerSize(); j++) {
            for (int i = 0; i < inputLayer.getLayerSize(); i++) {
                weightsForHidden[j][i] = generateRandom(random);
            }
        }

        for (int j = 1; j < outputLayer.getLayerSize(); j++) {
            for (int i = 0; i < hiddenLayer.getLayerSize(); i++) {
                weightsForOutput[j][i] = generateRandom(random);
            }
        }
    }

    private double activate(double value) {
        return 1.0 / (1.0 + Math.exp(-value));
    }

    private double generateRandom(Random random) {
        return random.nextDouble() * 2 + (-1);
    }

    public static class MlpBuilder {
        private int numberOfInputNeurons;
        private int numberOfHiddenNeurons;
        private int numberOfOutputNeurons;
        private boolean withBias;

        public MlpBuilder() {

        }

        public MlpBuilder inputNeurons(int numberOfInputsNeurons) {
            this.numberOfInputNeurons = numberOfInputsNeurons;
            return this;
        }

        public MlpBuilder hiddenNeurons(int numberOfHiddenNeurons) {
            this.numberOfHiddenNeurons = numberOfHiddenNeurons;
            return this;
        }

        public MlpBuilder outputNeurons(int numberOfOutputNeurons) {
            this.numberOfOutputNeurons = numberOfOutputNeurons;
            return this;
        }

        public MlpBuilder bias(boolean withBias) {
            this.withBias = withBias;
            return this;
        }

        public MLP build() {
            return new MLP(this);
        }
    }

}
