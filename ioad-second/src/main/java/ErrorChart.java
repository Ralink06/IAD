import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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

/**
 * Created by Rafał on 15.05.2017.
 */
public class ErrorChart extends ApplicationFrame {


    public static JFreeChart jfreechart;

    public ErrorChart(String s, XYSeriesCollection series){
        super(s);
        JPanel jpanel = createDemoPanel(series);
        jpanel.setPreferredSize(new Dimension(640, 480));
        add(jpanel);
    }

    public static JPanel createDemoPanel(XYSeriesCollection dataset) {
        jfreechart = ChartFactory.createXYLineChart("Error chart","X","Y",dataset,PlotOrientation.VERTICAL,true,true,false);

        Shape cross = ShapeUtilities.createDiagonalCross((float) 0.001, (float) 0.001);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.getDomainAxis().setRange(0.0,dataset.getDomainUpperBound(true)+0.5);
        xyPlot.getRangeAxis().setRange(dataset.getRangeLowerBound(true) -0.1,dataset.getRangeUpperBound(true)+0.1);
        xyPlot.setDomainGridlinePaint(Color.white);
        xyPlot.setRangeGridlinePaint(Color.white);
        xyPlot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        Shape cross2 = ShapeUtilities.createDiagonalCross((float) 2.0, (float) 2.0);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShape(0,cross);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShape(1,cross);
        renderer.setSeriesStroke(1, new BasicStroke(1.0f));

        renderer.setSeriesShape(2,cross2);
        renderer.setSeriesLinesVisible(2,false);
        renderer.setSeriesPaint(2, Color.GREEN);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setShapesFilled(true);

        xyPlot.setRenderer(renderer);
        xyPlot.getSeriesCount();


        return new ChartPanel(jfreechart);
    }
}
