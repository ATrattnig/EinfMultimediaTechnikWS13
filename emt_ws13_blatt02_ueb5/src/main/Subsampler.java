package main;

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
		if (args.length != 2)
		{
			System.out.println("Usage: Subsampler blocksize picturefile");
			System.exit(-1);
		}

		int blocksize = Integer.parseInt(args[0]);
		File picture = new File(args[1]);
		System.out.println("Started Subsampler with \nblocksize: " + blocksize + "x" + blocksize + "\nimage: " + args[1]);
		try 
		{
			BufferedImage img = ImageIO.read(picture);
			for (int w = 0; w < (img.getWidth() / blocksize); w++)
				for (int h = 0; h < (img.getHeight() / blocksize); h++)
				{	
					int currentWidth  = w * blocksize;
					int currentHeight = h * blocksize;
					int rgb = img.getRGB(currentWidth, currentHeight);
					for (int wb = currentWidth; wb < (currentWidth + blocksize); wb++)
						for (int hb = currentHeight; hb < (currentHeight + blocksize); hb++)
						{
							img.setRGB(wb, hb, rgb);
						}
				}
			File outputfile = new File("out.png");
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
