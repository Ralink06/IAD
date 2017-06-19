package model.layer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.neuron.RadialNeuron;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ralink on 18.06.17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HiddenLayer {

    private List<RadialNeuron> neurons = new ArrayList<>();

    public List<Double> calculateOutput(double input) {
        List<Double> output = new ArrayList<>();
        for(RadialNeuron a : neurons){
            output.add(a.calculateOutput(input));
        }
        return output;
    }

    public void calculateNeuronWidth(){
        List<RadialNeuron> copy = new ArrayList<>(neurons);
        for(RadialNeuron radialNeuron : neurons){
            copy.sort((a,b)->Double.compare(distance(a,radialNeuron),distance(b,radialNeuron)));
            radialNeuron.calculateUnitWidth(copy.subList(1,3));
        }
    }

    private double distance(RadialNeuron a, RadialNeuron b){
        return Math.abs(a.getCenter().getX() - b.getCenter().getX());
    }

    public void addNeuron(RadialNeuron neuron){
        neurons.add(neuron);
    }
}
