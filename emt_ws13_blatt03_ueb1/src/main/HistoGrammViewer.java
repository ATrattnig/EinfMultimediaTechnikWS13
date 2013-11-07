
/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ------------------
 * HistoGrammViewer.java
 * ------------------
 * (C) Copyright 2004, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: HistoGrammViewer.java,v 1.6 2004/05/05 16:28:55 mungady Exp $
 *
 * Changes
 * -------
 * 03-Feb-2004 : Version 1 (DG);
 *
 */

package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

/**
 * This demo shows a simple bar chart created using the {@link XYSeriesCollection} dataset.
 *
 */
public class HistoGrammViewer extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     * @throws Exception 
     */
    public HistoGrammViewer(final String title) throws Exception {
        super(title);
        IntervalXYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     * @throws Exception 
     */
    private IntervalXYDataset createDataset() throws Exception {
        final XYSeries series = new XYSeries("Histogramm");
        
        File picture      = new File("goose.jpg");
        BufferedImage img = ImageIO.read(picture);
        XYDataItem[] items = new XYDataItem[256];
        for (int i = 0; i < 256; i++)
        	items[i] = new XYDataItem(i, 0);
        for (int w = 0; w < (img.getWidth()); w++)
			for (int h = 0; h < (img.getHeight()); h++)
			{
				int values=img.getRGB(w, h);
	            // Convert the single integer pixel value to RGB color
	            Color oldColor = new Color(values);

	            int red = oldColor.getRed();        // get red value
	            int green = oldColor.getGreen();    // get green value
	            int blue = oldColor.getBlue();  // get blue value

	            // Convert RGB to gray scale using formula
	            // gray = 0.299 * R + 0.587 * G + 0.114 * B
	            int gray = (int)(0.299*red + 0.587*green + 0.114*blue);
				items[gray].setY(items[gray].getY().intValue() + 1);
			}
        for (int i = 0; i < 256; i++)
        	series.add(items[i]);
        
        final XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A sample chart.
     */
    private JFreeChart createChart(IntervalXYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYBarChart(
            "Histogramm",
            "Intensity", 
            false,
            "Frequency", 
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        return chart;    
    }
    
    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     * @throws Exception 
     */
    public static void main(final String[] args) throws Exception {

        final HistoGrammViewer demo = new HistoGrammViewer("HistoGrammViewer");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}