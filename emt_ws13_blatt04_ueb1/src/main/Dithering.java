package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Finishings;

class Dithering
{
	/**
	 * palette will be used only for exercise 4.2
	 */
	public static final C3[] palette = new C3[]
	{
        new C3(  0,   0,   0),
        new C3(  0,   0, 255),
        new C3(  0, 255,   0),
        new C3(  0, 255, 255),
        new C3(255,   0,   0),
        new C3(255,   0, 255),
        new C3(255, 255,   0),
        new C3(255, 255, 255)
    };
	
	/** Helper Function can be ignored by students.
	 * makes a deepCopy of a BufferdImage
	 */
	static BufferedImage deepCopy(BufferedImage bi)
	{
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static void main(String args[]) 
	{
		BufferedImage image = null;
		BufferedImage image_bw = null;
		BufferedImage image_color = null;
		try 
		{							
			 image = ImageIO.read(new File("lena_512x512.png"));
			 image_bw = deepCopy(image);
			 image_color = deepCopy(image);
		} 
		catch (IOException e)
		{
			System.out.println("Could not load Image\n");
			System.exit(-1);
		}
		
		image_bw = bw_dither(image_bw);
		image_color = color_dither(image_color);
		

		File outputfile1 = new File("dither_bw.png");
		File outputfile2 = new File("dither_color.png");
	    
		try
		{	
			ImageIO.write(image_bw, "png", outputfile1);
			ImageIO.write(image_color, "png", outputfile2);
		}
		catch (IOException e)
		{
			System.out.println("Could not save Image\n");
			System.exit(-1);
		}
	}
	
	/**
	 * @param image the input image
	 * @return the dithered black and white image
	 */
	public static BufferedImage bw_dither(BufferedImage image)
	{
		BufferedImage newImage = deepCopy(image); //NOTE: durch richtiges Schachteln der for schleife erspart man 
		//sich diese kopie
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++)
			{
				C3 oldpixel = new C3(image.getRGB(x, y));
				C3 newpixel = closestColorBW(oldpixel);
				newImage.setRGB(x, y, newpixel.toRGB());
				C3 err = oldpixel.sub(newpixel);
				
				if (x != (image.getWidth() - 1) && (y != (image.getHeight() - 1)))
					image.setRGB(x + 1, y + 1, new C3(image.getRGB(x + 1, y + 1)).add(err.mul(1./16)).toRGB());
				
				if (x != (image.getWidth() - 1))
					image.setRGB(x + 1, y + 0, new C3(image.getRGB(x + 1, y + 0)).add(err.mul(7./16)).toRGB());
				
				if (x != 0 && (y != (image.getHeight() - 1)))
					image.setRGB(x - 1, y + 1, new C3(image.getRGB(x - 1, y + 1)).add(err.mul(3./16)).toRGB());
				
				if (y != (image.getHeight() - 1))
					image.setRGB(x + 0, y + 1, new C3(image.getRGB(x + 0, y + 1)).add(err.mul(5./16)).toRGB());

			}
		return newImage;
	}
	
	/**
	 * @param image the input image
	 * @return the dithered 8bit color image using the static palette
	 */
	public static BufferedImage color_dither(BufferedImage image)
	{
		BufferedImage newImage = deepCopy(image);
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++)
			{
				C3 oldpixel = new C3(image.getRGB(x, y));
				C3 newpixel = closestColor(oldpixel, palette);
				image.setRGB(x, y, newpixel.toRGB());
				newImage.setRGB(x, y, newpixel.toRGB());
				C3 err = oldpixel.sub(newpixel);
				
				if (x != (image.getWidth() - 1) && (y != (image.getHeight() - 1)))
					image.setRGB(x + 1, y + 1, new C3(image.getRGB(x + 1, y + 1)).add(err.mul(1./16)).toRGB());
				
				if (x != (image.getWidth() - 1))
					image.setRGB(x + 1, y + 0, new C3(image.getRGB(x + 1, y + 0)).add(err.mul(7./16)).toRGB());
				
				if (x != 0 && (y != (image.getHeight() - 1)))
					image.setRGB(x - 1, y + 1, new C3(image.getRGB(x - 1, y + 1)).add(err.mul(3./16)).toRGB());
				
				if (y != (image.getHeight() - 1))
					image.setRGB(x + 0, y + 1, new C3(image.getRGB(x + 0, y + 1)).add(err.mul(5./16)).toRGB());

			}
		return newImage;
	}
	
	/**
	 * @param color input color
	 * @return the closest outputcolor. (Can only be black or white!)
	 */
	public static C3 closestColorBW(C3 color)
	{	
		//TODO IMPLEMENT FOR EXCERCISE 4.1
		//BETTER: get the grey value and then compare !
		//int average = (color.r + color.g + color.b) / 3;
		int average = (int) (.299 * color.r + .587 * color.g + .114 * color.b);
		C3 closest = new C3(0, 0, 0);
		if (average > 127)
			closest = new C3(255, 255, 255);
		
		return closest;
	}
		
	/**
	 * @param c the input color
	 * @param palette the palette to use
	 * @return the closest color of the palette compared to c
	 */
	private static C3 closestColor(C3 c, C3[] palette)
	{
		int closest = 0;
		int minDiff = Integer.MAX_VALUE;
		for (int i = 0; i < 8; i++)
		{
			if (c.diff(palette[i]) < minDiff)
			{
				closest = i;
				minDiff = c.diff(palette[i]);
			}
		}
		
	    return palette[closest];
	}
	
	/**
	 * The Class C3 is a helper class to ease the calculation with colors.
	 */
	static class C3
	{
	    int r, g, b;

	    public C3(int c)
	    {
	      Color color = new Color(c);
	      this.r = color.getRed();
	      this.g = color.getGreen();
	      this.b = color.getBlue();
	    }
	    
	    public C3(int r, int g, int b)
	    {
	      this.r = r;
	      this.g = g;
	      this.b = b;
	    }

	    public C3 add(C3 o)
	    {
	      //TODO IMPLEMENT FOR EXCERCISE 4.1
		      return new C3(this.r + o.r, this.g + o.g, this.b + o.b);
	    }
	    
	    public C3 sub(C3 o)
	    {
    	 //TODO IMPLEMENT FOR EXCERCISE 4.1
	      return new C3(this.r - o.r, this.g - o.g, this.b - o.b);
	    }
	    
	    public C3 mul(double d)
	    {
	      return new C3((int) (d * r), (int) (d * g), (int) (d * b));
	    }

	    public int toRGB()
	    {
	      return toColor().getRGB();
	    }
	    
	    public Color toColor()
	    {
	      return new Color(clamp(r), clamp(g), clamp(b));
	    }
	    
	    public int clamp(int c)
	    {
	      return Math.max(0, Math.min(255, c));
	    }
	    
	    public int diff(C3 o)
	    {
	    	int diffr = this.r - o.r;
	    	int diffg = this.g - o.g;
	    	int diffb = this.b - o.b;
	    	
	    	return (diffr * diffr) + (diffg * diffg) + (diffb * diffb);
	    }
	  }
}