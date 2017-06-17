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
        jfreechart = ChartFactory.createXYLineChart(null,"Number","Square Root",dataset,PlotOrientation.VERTICAL,true,true,false);

        String[] proporties = description.split("&");

        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.getDomainAxis().setRange(0.0,dataset.getDomainUpperBound(true));
        xyPlot.getRangeAxis().setRange(dataset.getRangeLowerBound(true),dataset.getRangeUpperBound(true));
        xyPlot.setDomainGridlinePaint(Color.white);
        xyPlot.setRangeGridlinePaint(Color.white);
        xyPlot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        Font font = new Font("Courier", Font.PLAIN, 22);
        XYTextAnnotation annotation1 = new XYTextAnnotation(proporties[0], 80, 4);
        XYTextAnnotation annotation2 = new XYTextAnnotation(proporties[1], 80, 3.5);
        XYTextAnnotation annotation3 = new XYTextAnnotation(proporties[2], 80, 3);
        XYTextAnnotation annotation4 = new XYTextAnnotation(proporties[3], 80, 2.5);
        XYTextAnnotation annotation5 = new XYTextAnnotation(proporties[4], 80, 2);
        XYTextAnnotation annotation6 = new XYTextAnnotation(proporties[5], 80, 1.5);

        annotation1.setFont(font);
        annotation2.setFont(font);
        annotation3.setFont(font);
        annotation4.setFont(font);
        annotation5.setFont(font);
        annotation6.setFont(font);

        xyPlot.addAnnotation(annotation1);
        xyPlot.addAnnotation(annotation2);
        xyPlot.addAnnotation(annotation3);
        xyPlot.addAnnotation(annotation4);
        xyPlot.addAnnotation(annotation5);
        xyPlot.addAnnotation(annotation6);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setBaseStroke(new BasicStroke(3));

        return new ChartPanel(jfreechart);
    }

}
