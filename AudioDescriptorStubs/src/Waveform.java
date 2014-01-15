import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Waveform extends ApplicationFrame
{
	/**
	 * Parses the MPEG-7 Data of the AudioDescriptor AudioWaveformType and stores <Min></Min> / <Max></Max> values into a double[][].
	 * @return double[][]
	 */
	public static double[][] parseAudioWavformData()
	{
		try {
			
			// the input file can be hardcoded like this
			File fXmlFile = new File("mpeg7_02ms.xml");
			
			// search for all AudioDescriptor elements first
					 
			// iterate over them and find the element with the type AudioWaveformType
			
			// if you found that element search for the next nested element SeriesOfScalar 
			
			// you could search here for the attribute totalNumOfSamples so you get the knowledge of how many data points there are
			
			// now find the <Min></Min> / <Max></Max> elements and read their content (first as string) 
			
			// separate the data points in the string via string.split(RegEx), convert them to double and store them in a double[][]
			
			// return the double[][]
		}
		catch (Exception e)
		{
			e.printStackTrace();
	    }
	
		return null;
	}
	
	/**
	 * Creates an XYSeriesCollection out of XYSeries wich contain the MPEG-7 Metadata
	 * @return XYSeriesCollection
	 */
	public static XYDataset createDataset()
	{
		// well lets get the data
		double[][] data = parseAudioWavformData();
		
		//create two XYSeries Min and Max
		
		// fill the XYSeries with data
		
		// create a XYSeriesCollection
		
		// add the XYSeries to the collection
		
		// return the collection
		
		return null;
	}
	
	private static final long serialVersionUID = 1L;

	public Waveform(String s)
	{
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
	}

	private static JFreeChart createChart(XYDataset xydataset)
	{
		JFreeChart jfreechart = ChartFactory.createXYLineChart ("Difference Chart", "Time", "Value", xydataset);
		jfreechart.setBackgroundPaint(Color.white);
		XYPlot xyplot = (XYPlot)jfreechart.getPlot();
		XYDifferenceRenderer xydifferencerenderer = new XYDifferenceRenderer(Color.green, Color.red, false);
		xydifferencerenderer.setRoundXCoordinates(true);
		xyplot.setDomainCrosshairLockedOnData(true);
		xyplot.setRangeCrosshairLockedOnData(true);
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		xyplot.setRenderer(xydifferencerenderer);
		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		DateAxis dateaxis = new DateAxis("Time");
		dateaxis.setLowerMargin(0.0D);
		dateaxis.setUpperMargin(0.0D);
		xyplot.setDomainAxis(dateaxis);
		xyplot.setForegroundAlpha(0.5F);
		return jfreechart;
	}

	public static JPanel createDemoPanel()
	{
		JFreeChart jfreechart = createChart(createDataset());
		return new ChartPanel(jfreechart);
	}

	public static void main(String args[])
	{
		Waveform differencechartdemo1 = new Waveform("Difference Chart Demo 1");
		differencechartdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(differencechartdemo1);
		differencechartdemo1.setVisible(true);
	}
}