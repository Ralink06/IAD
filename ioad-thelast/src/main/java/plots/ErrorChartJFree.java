package plots;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;


public class ErrorChartJFree extends ApplicationFrame {


    private static JFreeChart jfreechart;

    public ErrorChartJFree(String s, XYSeriesCollection series,String description){
        super(s);
        JPanel jpanel = createDemoPanel(series,description);
        jpanel.setPreferredSize(new Dimension(640, 480));
        add(jpanel);
    }

    private static JPanel createDemoPanel(XYSeriesCollection dataset, String description) {
        jfreechart = ChartFactory.createXYLineChart(null,"Iteration","Error",dataset,PlotOrientation.VERTICAL,true,true,false);

        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.getDomainAxis().setRange(0.0 - 0.5,dataset.getDomainUpperBound(true));
        xyPlot.getRangeAxis().setRange(dataset.getRangeLowerBound(true) -0.1,dataset.getRangeUpperBound(true)+0.1);
        xyPlot.setDomainGridlinePaint(Color.white);
        xyPlot.setRangeGridlinePaint(Color.white);
        xyPlot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setBaseStroke(new BasicStroke(3));

        return new ChartPanel(jfreechart);
    }

}
