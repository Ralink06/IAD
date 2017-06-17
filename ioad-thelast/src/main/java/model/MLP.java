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


    public List<Double> train(List<Double> input, List<Double> expectedResult, double learningRate) {
        List<Double> afterPropagation = forwardPropagation(input);
        backPropagation(expectedResult, learningRate);

        return afterPropagation;
    }

    private void backPropagation(List<Double> expectedResult, double learningRate) {
        double[] errorsForOutput = new double[outputLayer.getLayerSize()];
        double[] errorsForHidden = new double[hiddenLayer.getLayerSize()];
        double sum = 0.0;

        for (int i = 1; i < outputLayer.getLayerSize(); i++) {
            Double neuron = outputLayer.getNeuron(i);
            errorsForOutput[i] = sigmoidDerivative(neuron) * (expectedResult.get(i - 1) - neuron);
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
                double temp = errorsForOutput[j];
                double temp2 = hiddenLayer.getNeuron(i);
                double temp3 = learningRate*temp2*temp;
                weightsForOutput[j][i] += temp3;
            }
        }

        for (int j = 1; j < hiddenLayer.getLayerSize(); j++) {
            for (int i = 0; i < inputLayer.getLayerSize(); i++) {
                weightsForHidden[j][i] += learningRate * errorsForHidden[j] * inputLayer.getNeuron(i);
            }
        }

    }

    public List<Double> forwardPropagation(List<Double> input) {
        inputLayer.setLayerInput(input);
        hiddenLayer.setLayerInput(propagateLayer(inputLayer, hiddenLayer, weightsForHidden,true));
        outputLayer.setLayerInput(propagateLayer(hiddenLayer, outputLayer, weightsForOutput,false));

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
            if(sigmoid)output.add(activate(passedValue));
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