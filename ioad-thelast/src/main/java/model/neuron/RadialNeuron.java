package model.neuron;

import kmeans.Point;
import lombok.Data;

import java.util.List;


@Data
public class RadialNeuron {
    private Point center;
    private double width;

    public RadialNeuron(Point center) {
        this.center = center;
        width = 0.0;
    }

    public void calculateUnitWidth(List<RadialNeuron> neighbours) {
        for (RadialNeuron neighbour : neighbours) {
            width += (center.getX() - neighbour.getCenter().getX()) * (center.getX() - neighbour.getCenter().getX());
        }
        width = Math.sqrt(width / 2);
    }

    public double calculateOutput(double input) {
        return Math.exp(-(((Math.abs(input - center.getX())) * (Math.abs(input - center.getX()))) / (2*(width * width))));
    }
}
