import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;


public class PointsChart extends ApplicationFrame {

    public static JFreeChart jfreechart;

    public PointsChart(String s, XYSeriesCollection series) {
        super(s);
        JPanel jpanel = createDemoPanel(series);
        jpanel.setPreferredSize(new Dimension(640, 480));
        add(jpanel);
    }

    public static JPanel createDemoPanel(XYSeriesCollection dataset) {
        jfreechart = ChartFactory.createScatterPlot(
                "Scatter Plot Demo", "X", "Y", dataset,
                PlotOrientation.VERTICAL, true, true, false);




        Shape cross = ShapeUtilities.createDiagonalCross((float) 0.3, (float) 0.3);
        Shape cross2 = ShapeUtilities.createDiagonalCross((float) 2.0, (float) 2.0);
        //Shape cross2 = ShapeUtilities.createDiagonalCross((float) 2.0, (float) 2.0);

        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.getDomainAxis().setRange(-10,12.5);
        xyPlot.getRangeAxis().setRange(-12,8.5);
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesShape(0, cross2);
        renderer.setSeriesPaint(0, Color.black);
        renderer.setSeriesShape(1, cross);
        renderer.setSeriesPaint(1, Color.gray);

        xyPlot.getSeriesCount();



        return new ChartPanel(jfreechart);
    }


}