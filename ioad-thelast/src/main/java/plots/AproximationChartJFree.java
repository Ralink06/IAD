package plots;

import model.neuron.RadialNeuron;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;


public class AproximationChartJFree extends ApplicationFrame {


    private static JFreeChart jfreechart;

    public AproximationChartJFree(String s, XYSeriesCollection series, String description, java.util.List<RadialNeuron> neurons) {
        super(s);
        JPanel jpanel = createDemoPanel(series, description, neurons);
        jpanel.setPreferredSize(new Dimension(640, 480));
        add(jpanel);
    }

    private static JPanel createDemoPanel(XYSeriesCollection dataset, String description, java.util.List<RadialNeuron> neurons) {
        jfreechart = ChartFactory.createScatterPlot(null, "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, false);

        String[] proporties = description.split("&");
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        Shape cross = ShapeUtilities.createDiagonalCross(2, Float.valueOf("0.2"));
        xyPlot.getDomainAxis().setRange(dataset.getDomainLowerBound(true) - 1, dataset.getDomainUpperBound(true) + 1);
        xyPlot.getRangeAxis().setRange(dataset.getRangeLowerBound(true), dataset.getRangeUpperBound(true));
        xyPlot.setDomainGridlinePaint(Color.white);
        xyPlot.setRangeGridlinePaint(Color.white);
        xyPlot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesShape(0, cross);
        renderer.setSeriesPaint(0, Color.red);


        renderer.setSeriesPaint(1, Color.BLUE);
        //renderer.setBaseStroke(new BasicStroke(3));

        for (RadialNeuron a : neurons) {
            ((XYPlot) jfreechart.getPlot())
                    .addAnnotation(new XYShapeAnnotation(new Ellipse2D.Double(a.getCenter().getX() - a.getWidth(), a.getCenter().getY() - a.getWidth(), a.getWidth() * 2, a.getWidth() * 2)));
        }

        return new ChartPanel(jfreechart);
    }

}
