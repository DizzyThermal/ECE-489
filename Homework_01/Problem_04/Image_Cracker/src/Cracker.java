import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Cracker
{
	public static File darkImage = new File("Dark.bmp");
	public static File lightImage = new File("Light.bmp");
	public static File outputFile = new File("Output.bmp");

	public static BufferedImage darkPlate = null;
	public static BufferedImage lightPlate = null;
	public static BufferedImage outputImage = null;

	public static String dark12 = "000000000000";
	public static String light12 = "111111111111";

	public static int rgbLength = 24;

	public static Random randomNumber;

	public static void main(String[] args)
	{

		try 
		{
			darkPlate = ImageIO.read(darkImage);
			lightPlate = ImageIO.read(lightImage);
			outputImage = new BufferedImage(darkPlate.getWidth(), darkPlate.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		catch(IOException ioe) { ioe.printStackTrace(); }

		// Rebuild Original from Dark and Light
		// BRUTE FORCE
		for(int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++)
		{
			randomNumber = new Random(i);
			for(int j = 0; j < darkPlate.getWidth(); j++)
			{
				for(int k = 0; k < darkPlate.getHeight(); k++)
				{
					String darkPixelBinary = Integer.toBinaryString(darkPlate.getRGB(j, k)).substring(8);
					String lightPixelBinary = Integer.toBinaryString(lightPlate.getRGB(j, k)).substring(8);
					String mixedPixel = darkPixelBinary.substring(12) + lightPixelBinary.substring(12);
		
					String randomNumber = getRandomNumber();
		
					String xoredByte = xor(mixedPixel, randomNumber);
		
					int alpha = 0xFF;
					int red = Integer.parseInt(xoredByte.substring(0, 8), 2);
					int green = Integer.parseInt(xoredByte.substring(8, 16), 2); 
					int blue = Integer.parseInt(xoredByte.substring(16, 24), 2);
		
					outputImage.setRGB(j, k, ((alpha << 24) + (red << 16) + (green << 8) + blue));
				}
			}
			
			// boolean valid = checkImageAgainstOCR(outputImage);
			// if(valid)
			// 		saveImage(outputImage);
		}
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
}