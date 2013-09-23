import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Steganographer
{
	public static File darkVisible = new File("DarkVisible.bmp");
	public static File lightVisible = new File("LightVisible.bmp");
	public static File darkImage = new File("Dark.bmp");
	public static File lightImage = new File("Light.bmp");

	public static BufferedImage darkPlateVisible = null;
	public static BufferedImage lightPlateVisible = null;
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
			darkPlateVisible = ImageIO.read(darkVisible);
			lightPlateVisible = ImageIO.read(lightVisible);
			darkPlate = new BufferedImage(darkPlateVisible.getWidth(), darkPlateVisible.getHeight(), BufferedImage.TYPE_INT_RGB);
			lightPlate = new BufferedImage(lightPlateVisible.getWidth(), lightPlateVisible.getHeight(), BufferedImage.TYPE_INT_RGB);
			
		}
		catch(IOException ioe) { ioe.printStackTrace(); }

		// Hide Image in Dark/Light Images
		for(int i = 0; i < darkPlateVisible.getWidth(); i++)
		{
			for(int j = 0; j < darkPlateVisible.getHeight(); j++)
			{
				String darkPixelBinary = Integer.toBinaryString(darkPlateVisible.getRGB(i, j)).substring(8);
				String lightPixelBinary = Integer.toBinaryString(lightPlateVisible.getRGB(i, j)).substring(8);
				String randomNumber = getRandomNumber();

				String darkXoredByte = xor(darkPixelBinary, randomNumber);
				String lightXoredByte = xor(lightPixelBinary, randomNumber);

				// 24 bits
				String dark = dark12 + darkXoredByte.substring(0, 12);
				String light = light12 + lightXoredByte.substring(12, 24);

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
}