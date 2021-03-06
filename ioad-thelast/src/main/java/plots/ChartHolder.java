package plots;

import kmeans.Cluster;
import kmeans.Point;
import model.layer.HiddenLayer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

public class ChartHolder {

    private XYSeriesCollection outputErrorChart = new XYSeriesCollection();
    private XYSeries outputPlot = new XYSeries("Output Error");

    private XYSeriesCollection outputErrorKMeansChart = new XYSeriesCollection();
    private XYSeries outputPlotKMeans = new XYSeries("KMeans Error");

    private XYSeriesCollection aproximationChart = new XYSeriesCollection();
    private XYSeries pointsPlot = new XYSeries("Points Plot");
    private XYSeries clustersPlot = new XYSeries("Centroids Plot");
    private XYSeries aproximationPlot = new XYSeries("Aproximation");

    public void addPointsToPlotPoints(final Point point) {
        pointsPlot.add(point.getX(), point.getY());
    }

    public void addPointsToClustersPlot(final Cluster cluster) {
        clustersPlot.add(cluster.getCentroid().getX(), cluster.getCentroid().getY());
    }

    public void addPointsToApproximationPlot(final double min, final double propagateValue) {
        aproximationPlot.add(min, propagateValue);
    }

    public void addPointsToOutputPlot(final double x, final double y) {
        outputPlot.add(x, y);
    }

    public void addPointsToKmeansOutput(final int i, final Double value) {
        outputPlotKMeans.add(i, value);
    }

    public void addOutputPointsPlotToErrorChart() {
        outputErrorChart.addSeries(outputPlot);
    }

    public void addPointsPlotToApproximationChart() {
        aproximationChart.addSeries(pointsPlot);
    }

    public void addClustersPlotToApproximationChart() {
        aproximationChart.addSeries(clustersPlot);
    }

    public void addApproximationPlotToApproximationChart() {
        aproximationChart.addSeries(aproximationPlot);
    }

    public void displayChart(final HiddenLayer hiddenLayer,String description) {

        outputErrorKMeansChart.addSeries(outputPlotKMeans);
        ErrorChartJFree kMeans = new ErrorChartJFree("", outputErrorChart, description);
        kMeans.pack();
        RefineryUtilities.centerFrameOnScreen(kMeans);
        kMeans.setVisible(true);

//        ErrorChartJFree errorChartJFree = new ErrorChartJFree("", outputErrorKMeansChart,description );
//        errorChartJFree.pack();
//        RefineryUtilities.centerFrameOnScreen(errorChartJFree);
//        errorChartJFree.setVisible(true);

        AproximationChartJFree aprox = new AproximationChartJFree("", aproximationChart, "", hiddenLayer.getNeurons());
        aprox.pack();
        RefineryUtilities.centerFrameOnScreen(aprox);
        aprox.setVisible(true);
    }

    public void addKmeansPlotToOutputErrorChart() {
        outputErrorChart.addSeries(outputPlotKMeans);
    }
}
