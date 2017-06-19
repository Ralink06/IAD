package plots;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;


public class AproximationChartJFree extends ApplicationFrame {


    public static JFreeChart jfreechart;

    public AproximationChartJFree(String s, XYSeriesCollection series,String description){
        super(s);
        JPanel jpanel = createDemoPanel(series,description);
        jpanel.setPreferredSize(new Dimension(640, 480));
        add(jpanel);
    }

    public static JPanel createDemoPanel(XYSeriesCollection dataset,String description) {
        jfreechart = ChartFactory.createScatterPlot(null,"Number","Square Root",dataset,PlotOrientation.VERTICAL,true,true,false);

        String[] proporties = description.split("&");

        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        Shape cross = ShapeUtilities.createDiagonalCross(2,Float.valueOf("0.2"));
        xyPlot.getDomainAxis().setRange(dataset.getDomainLowerBound(true)-1,dataset.getDomainUpperBound(true)+1);
        xyPlot.getRangeAxis().setRange(dataset.getRangeLowerBound(true),dataset.getRangeUpperBound(true));
        xyPlot.setDomainGridlinePaint(Color.white);
        xyPlot.setRangeGridlinePaint(Color.white);
        xyPlot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());



        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesShape(0, cross);
        renderer.setSeriesPaint(0, Color.red);


        renderer.setSeriesPaint(1, Color.BLUE);
        //renderer.setBaseStroke(new BasicStroke(3));

        return new ChartPanel(jfreechart);
    }

}
