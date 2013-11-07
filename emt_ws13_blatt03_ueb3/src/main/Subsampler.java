package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Subsampler {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		File picture = new File("lena_512x512.png");

		try 
		{
			BufferedImage img = ImageIO.read(picture);
			Byte[][] bytes = new Byte[img.getWidth()][img.getHeight()];
			
			for (int w = 0; w < (img.getWidth()); w++)
				for (int h = 0; h < (img.getHeight()); h++)
				{	
					int values=img.getRGB(w, h);
		            // Convert the single integer pixel value to RGB color
		            Color oldColor = new Color(values);

		            int red   = oldColor.getRed();        // get red value
		            int green = oldColor.getGreen();    // get green value
		            int blue  = oldColor.getBlue();  // get blue value
		            
		            int newRed = (red / (256 / 8)) << 5;
		            int newGreen = (green / (256 / 8)) << 2;
		            int newBlue = blue / (256 / 4);
		            
		            //System.out.println((newRed >> 5) + " " + (newGreen >> 2) + " " + newBlue);
		            bytes[w][h] = (byte) (newRed + newGreen + newBlue);           
		            
		            int reRed = (newRed >> 5) * 256 / 8;
		            int reGreen = (newGreen >> 2) * 256 / 8;
		            int reBlue = (newBlue) * 256 / 4;
		            
		            Color newColor = new Color(reRed, reGreen, reBlue);
					img.setRGB(w, h, newColor.getRGB());
				}
			
			File outputfile = new File("out.png");
			ImageIO.write(img, "png", outputfile);
			System.out.println("\n...\nsuccessful");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
