package com.umassd.ece489;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Steganographer
{
	public static File licensePlateImagePath = new File("IkesPlate.bmp");
	public static File licensePlateRebuiltPath = new File("IkesPlateRebuilt.bmp");
	public static File darkImage = new File("Dark.bmp");
	public static File lightImage = new File("Light.bmp");
	
	public static BufferedImage licensePlate = null;
	public static BufferedImage licensePlateRebuilt = null;
	public static BufferedImage darkPlate = null;
	public static BufferedImage lightPlate = null;
	
	public static String dark12 = "000000000000";
	public static String light12 = "111111111111";
	
	public static int seed = 1337;
	
	public static int rgbLength = 24;
	
	public static Random randomNumber = new Random(seed);
	
	public static void main(String[] args)
	{
		
		try 
		{
			licensePlate = ImageIO.read(licensePlateImagePath);
			darkPlate = deepCopy(licensePlate);
			lightPlate = deepCopy(licensePlate);
		}
		catch(IOException ioe) { ioe.printStackTrace(); }
		
		// Hide Image in Dark/Light Images
		for(int i = 0; i < licensePlate.getWidth(); i++)
		{
			for(int j = 0; j < licensePlate.getHeight(); j++)
			{
				String pixelBinary = Integer.toBinaryString(licensePlate.getRGB(i, j)).substring(8);
				String randomNumber = getRandomNumber();

				String xoredByte = xor(pixelBinary, randomNumber);
				
				// 24 bits 
				String dark = dark12 + xoredByte.substring(0, 12);
				String light = light12 + xoredByte.substring(12, 24);
				
				int alpha = 0xFF;
				
				int dRed = Integer.parseInt(dark.substring(0, 8), 2); 
				int dGreen = Integer.parseInt(dark.substring(8, 16), 2); 
				int dBlue = Integer.parseInt(dark.substring(16, 24), 2);

				int lRed = Integer.parseInt(light.substring(0, 8), 2); 
				int lGreen = Integer.parseInt(light.substring(8, 16), 2); 
				int lBlue = Integer.parseInt(light.substring(16, 24), 2);
				
				darkPlate.setRGB(i, j, ((alpha << 24) + (dRed << 16) + (dGreen << 8) + dBlue));
				lightPlate.setRGB(i, j, ((alpha << 24) + (lRed << 16) + (lGreen << 8) + lBlue));
			}
		}
		
		// Save Hidden Images
		try 
		{
			ImageIO.write(darkPlate, "bmp", darkImage);
			ImageIO.write(lightPlate, "bmp", lightImage);
			licensePlateRebuilt = deepCopy(darkPlate);
		}
		catch(IOException ioe) { ioe.printStackTrace(); }
		
		// Rebuild Original from Dark and Light

			// Restart the Random Number Counter from the top
		randomNumber = new Random(seed);
		for(int i = 0; i < licensePlateRebuilt.getWidth(); i++)
		{
			for(int j = 0; j < licensePlateRebuilt.getHeight(); j++)
			{
				String darkPixelBinary = Integer.toBinaryString(darkPlate.getRGB(i, j)).substring(8);
				String lightPixelBinary = Integer.toBinaryString(lightPlate.getRGB(i, j)).substring(8);
				String mixedPixel = darkPixelBinary.substring(12) + lightPixelBinary.substring(12);
				
				String randomNumber = getRandomNumber();
				
				String xoredByte = xor(mixedPixel, randomNumber);
				
				int alpha = 0xFF;
				int red = Integer.parseInt(xoredByte.substring(0, 8), 2);
				int green = Integer.parseInt(xoredByte.substring(8, 16), 2); 
				int blue = Integer.parseInt(xoredByte.substring(16, 24), 2);
				
				licensePlateRebuilt.setRGB(i, j, ((alpha << 24) + (red << 16) + (green << 8) + blue));
			}
		}
		
		// Save Rebuilt Image
		try 
		{
			ImageIO.write(licensePlateRebuilt, "bmp", licensePlateRebuiltPath);
		}
		catch(IOException ioe) { ioe.printStackTrace(); }
	}
	
	public static String xor(String s1, String s2)
	{
		// One or the other, but not both!
		String returnString = "";
		
		for(int i = 0; i < s1.length(); i++)
		{
			if(s1.charAt(i) == s2.charAt(i))
				returnString += "0";
			else
				returnString += "1";
		}
		
		return returnString;
	}
	
	public static String getRandomNumber()
	{
		String returnString = "";
		while(returnString.length() < rgbLength)
		{
			returnString += randomNumber.nextInt(2);
		}
		
		return returnString;
	}
	
	// Code from: http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	public static BufferedImage deepCopy(BufferedImage bi)
	{
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}