package es.um.multigraph.gui;

import es.um.multigraph.core.MainClass;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A simple demonstration application showing how to create a line chart using
 * data from a {@link CategoryDataset}.
 */
public class MemChartPanel {

    private DefaultCategoryDataset dataset;
    private Runtime runtime;
    private ChartPanel panel;
    private DataCollectionThread memdatacollector;
    private Thread dataCollectionThread;
    private JFreeChart chart;
    private MainClass parent;

    /**
     * Creates a new demo.
     *     
     * @param parent
* @param title the frame title.
     */
    public MemChartPanel(MainClass parent, String title) {
        this.parent = parent;
        dataset = new DefaultCategoryDataset();
        runtime = Runtime.getRuntime();

        //dataset.addValue((runtime.totalMemory() - runtime.freeMemory())/(1024*1024), "Mem(MiB)", "0");
        chart = createChart(dataset, title);
        panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(567, 345));
    }

    /**
     * Begins the measuring thread for memory consumption
     */
    public synchronized void start() {
        if(parent != null) 
            parent.log("Start Chart Thread", this);
        memdatacollector= new DataCollectionThread();
        dataCollectionThread = new Thread(memdatacollector);
        dataCollectionThread.start();
    }

    /**
     * Stops the thread for memory consumption
     */
    public synchronized void stop() {
        if(parent != null) 
            parent.log("Stop requested", this);
        this.interrupted = true;
    }

    /**
     * Creates a sample dataset.
     *     
* @return The dataset.
     *
    private static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(4, "Nodes", "Ex 1");
        dataset.addValue(4, "Edges", "Ex 1");
        dataset.addValue(0, "Countermeasure", "Ex 1");
        
        dataset.addValue(10, "Nodes", "Ex 2");
        dataset.addValue(15, "Edges", "Ex 2");
        dataset.addValue(2, "Countermeasure", "Ex 2");
        
        dataset.addValue(10, "Nodes", "Ex 3");
        dataset.addValue(12, "Edges", "Ex 3");
        dataset.addValue(5, "Countermeasure", "Ex 3");
        return dataset;
    }*/

    /**
     * Creates a sample chart.
     *     
* @param dataset a dataset.
     *     
* @return The chart.
     */
    private JFreeChart createChart(CategoryDataset dataset, String title) {
// create the chart...
        chart = ChartFactory.createLineChart(
                title, // chart title
                "Seconds", // domain axis label
                "MB", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
        );
        //chart.addSubtitle(new TextTitle("Number of Classes By Release"));
        /*TextTitle source = new TextTitle(
                "Blue: Edges\nRed: Nodes\nGreen: Countermeasures"
        );
        source.setFont(new Font("SansSerif", Font.PLAIN, 10));
        source.setPosition(RectangleEdge.BOTTOM);
        source.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        chart.addSubtitle(source);
        */
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
// customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRangeWithMargins(0, 200); //Math.round(Runtime.getRuntime().maxMemory()/(1024*1024))
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
// customise the renderer...
        LineAndShapeRenderer renderer
                = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(true);
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setFillPaint(Color.white);
        return chart;
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *     
* @return A panel.
     */
    public JPanel getPanel() {
        return panel;
    }
    
    private volatile boolean interrupted=false;

    private class DataCollectionThread extends Thread {
        
        @Override
        public void run() {
            interrupted=false;
            Runtime runtime = Runtime.getRuntime();
            int secondsrunning=0;
            while (! interrupted ) {
                double mem = (runtime.totalMemory() - runtime.freeMemory())/(1024*1024);
                if(mem > 200) {
                    ((NumberAxis) ((CategoryPlot)chart.getPlot()).getRangeAxis()).setRangeWithMargins(0, 500);
                    if(mem > 500) {
                        ((NumberAxis) ((CategoryPlot)chart.getPlot()).getRangeAxis()).setRangeWithMargins(0, 1000);
                        if(mem > 900) 
                            ((NumberAxis) ((CategoryPlot)chart.getPlot()).getRangeAxis()).setRangeWithMargins(0, Math.round(Runtime.getRuntime().maxMemory()/(1024*1024)));
                    }
                }
                dataset.addValue(mem, "Mem(MiB)", ""+secondsrunning);
                secondsrunning+=2;
                try {
                    //this.wait(2000);
                    this.sleep(2000);
                } catch (InterruptedException e) {
                    interrupted=true;
                }
            }

        }
        
        public synchronized void terminate() {
            interrupted=true;
            //this.notify();
        }
       
    }
    
    @Override
    public String toString() {
        return "MemChartPanel";
    }
    
}
