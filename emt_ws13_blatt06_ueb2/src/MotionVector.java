import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Daniel Posch
 */
public class MotionVector
{
	public static void main(String args[])
	{
		int[][] r_frame = getLumincanceChannel("image-0096.png");
		int[][] t_frame = getLumincanceChannel("image-0097.png");
		
		MotionVector vector = searchMotionVector(r_frame, t_frame, 8, 32, 256, 256);
		
		if(vector != null)
			System.out.println(vector.toString());
		else
			System.out.println("Vector is null!");
	}
	
	/////////////////////////////////////START SECTION FOR STUDENT IMPLEMENTATION\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	/**
	 * @param r_frame	the reference frame of a video (actually just 1 channel, e.g. Y')
	 * @param t_frame	the target frame of a video	(actually just 1 channel, e.g. Y')
	 * @param blocksize	the blocksize of the macroblocks (should be 8x8 or 16x16)
	 * @param searchWindow	the searchwindow size for the algorithm (the bigger the better the result, but the worse the performance)
	 * @param row_index	the y-coordinate of first (top,left) element of the macroblock in the t_frame 
	 * @param col_index	the x-coordinate of the first (top,left) element of the macroblock in the t_frame
	 * @return the motion vector
	 */
	private static MotionVector searchMotionVector(int[][] r_frame,	int[][] t_frame, int blocksize, int searchWindow, int row_index, int col_index)
	{				
		double mad = Double.MAX_VALUE;
		MotionVector result = new MotionVector(0, 0);
		int p = (searchWindow - 1) / 2;

		// 1. check if the specified macroblock does not exceed the frame
		// border. if so return null.
		if (row_index - p <= -p || row_index + p >= r_frame.length)
			return null;

		if (col_index - p <= -p || col_index + p >= r_frame[0].length)
			return null;

		// 2. extract the macroblock's data from the t_frame
		int[][] t_block = new int[blocksize][blocksize];
		for (int i = 0; i < blocksize; i++)
			for (int j = 0; j < blocksize; j++) 
			{
				t_block[i][j] = t_frame[row_index + i][col_index + j];
			}
		// 3. sequential search algorithm on r_frame to determine the motion
		// vector
		for (int i = -p; i < p; i++) {
			for (int j = -p; j < p; j++) {
				// use MAD function as difference measure.
				double curr_mad = MAD(t_block, r_frame, i, j, blocksize,
						row_index, col_index);
				if (curr_mad < mad) {
					mad = curr_mad;
					result.setNewValues(i, j);
				}
			}
		}

		return result;
	}

	/**
	 * @param t_block	the target macroblock for which you search the MV
	 * @param r_frame	the reference frame
	 * @param i			the search offset of the y coordinate
	 * @param j			the search offset of the x coordinate
	 * @param blocksize	the blocksize of the macroblock
	 * @param row_index	the offset of the macroblock's y coordinate
	 * @param col_index the offset of the macroblock's x coordinate
	 * @return	the MDA value 
	 */
	private static double MAD(int[][] t_block, int[][] r_frame, int i, int j, int blocksize, int row_index, int col_index) 
	{
		
		//calculate the MAD value. if you exceed the r_frame's border return Double.MAX_VALUE;
		double sum = 0;
		for (int k = 0; k < blocksize; k++) 
		{
			for (int l = 0; l < blocksize; l++) 
			{
				try
				{
					sum += Math.abs(t_block[k][l] - r_frame[row_index + k + i][col_index + l + j]);
				} catch (IndexOutOfBoundsException e) 
				{
					return Double.MAX_VALUE;
				}
			}
		}
		return ((1 / Math.pow(blocksize, 2)) * sum);
	}
	
	/////////////////////////////////////END SECTION FOR STUDENT IMPLEMENTATION\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	private int u, v;
	
	/**
	 * creates a new motion vector
	 * @param u vertical change
	 * @param v horizontal change
	 */
	public MotionVector(int u, int v)
	{
		this.u = u;
		this.v = v;
	}
	
	/**
	 * updates the motion vector
	 * @param u vertical change
	 * @param v horizontal change
	 */
	public void setNewValues(int u, int v)
	{
		this.u = u;
		this.v = v;
	}
	
	/**
	 * concerts the MV to a String.
	 */
	public String toString()
	{
		return new String("MV(" +u +"," + v +")");
	}

	/**
	 * loads an image file from the filesystem. returns the luminanceChannel of the image as int[][].
	 * @param file the image to read.
	 * @return
	 */
	private static int[][] getLumincanceChannel(String file)
	{
		BufferedImage image = null;
		try 
		{
			 image = ImageIO.read(new File(file));
		} 
		catch (IOException e)
		{
			System.out.println("Could not load Image: " + file);
			System.exit(-1);
		}
		
		int h = image.getHeight();
		int w = image.getWidth();
		int pixel = 0;
		
		int[][] data = new int[h][w];		
		for(int i = 0; i < h; i++)
		{
			for(int j = 0; j < w ; j++)
			{
				pixel = image.getRGB(j, i);
				data[i][j] = (int) Math.round(0.299*((pixel >> 16) & 0xFF) + 0.587*((pixel >> 8) & 0xFF) + 0.114*(pixel & 0xFF));
			}
		}
			
		return data;
	}
}