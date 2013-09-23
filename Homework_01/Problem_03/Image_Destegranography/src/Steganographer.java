import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Steganographer
{
	public static File darkImage = new File("Dark.bmp");
	public static File lightImage = new File("Light.bmp");
	public static File licensePlatePath = new File("IkesPlateRebuilt.bmp");
	public static File licensePlateWrongPath = new File("IkesPlateRebuiltWrong.bmp");

	public static BufferedImage darkPlate = null;
	public static BufferedImage lightPlate = null;
	public static BufferedImage licensePlate = null;
	public static BufferedImage licensePlateWrong = null;

	public static String dark12 = "000000000000";
	public static String light12 = "111111111111";

	public static int seed = 1337;
	public static int wrongSeed = 7331;

	public static int rgbLength = 24;

	public static Random randomNumber = new Random(seed);
	public static Random wrongRandomNumber = new Random(wrongSeed);

	public static void main(String[] args)
	{

		try 
		{
			darkPlate = ImageIO.read(darkImage);
			lightPlate = ImageIO.read(lightImage);
			licensePlate = new BufferedImage(darkPlate.getWidth(), darkPlate.getHeight(), BufferedImage.TYPE_INT_RGB);
			licensePlateWrong = new BufferedImage(darkPlate.getWidth(), darkPlate.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		catch(IOException ioe) { ioe.printStackTrace(); }

		// Rebuild Original from Dark and Light

			// Restart the Random Number Counter from the top
		randomNumber = new Random(seed);
		for(int i = 0; i < licensePlate.getWidth(); i++)
		{
			for(int j = 0; j < licensePlate.getHeight(); j++)
			{
				String darkPixelBinary = Integer.toBinaryString(darkPlate.getRGB(i, j)).substring(8);
				String lightPixelBinary = Integer.toBinaryString(lightPlate.getRGB(i, j)).substring(8);
				String mixedPixel = darkPixelBinary.substring(12) + lightPixelBinary.substring(12);

				String randomNumber = getRandomNumber(false);
				String wrongRandomNumber = getRandomNumber(true);

				String xoredByte = xor(mixedPixel, randomNumber);
				String xoredByteWrong = xor(mixedPixel, wrongRandomNumber);

				int alpha = 0xFF;
				int red = Integer.parseInt(xoredByte.substring(0, 8), 2);
				int green = Integer.parseInt(xoredByte.substring(8, 16), 2); 
				int blue = Integer.parseInt(xoredByte.substring(16, 24), 2);
				
				int wRed = Integer.parseInt(xoredByteWrong.substring(0, 8), 2);
				int wGreen = Integer.parseInt(xoredByteWrong.substring(8, 16), 2); 
				int wBlue = Integer.parseInt(xoredByteWrong.substring(16, 24), 2);

				licensePlate.setRGB(i, j, ((alpha << 24) + (red << 16) + (green << 8) + blue));
				licensePlateWrong.setRGB(i, j, ((alpha << 24) + (wRed << 16) + (wGreen << 8) + wBlue));
			}
		}

		// Save Rebuilt Image
		try 
		{
			ImageIO.write(licensePlate, "bmp", licensePlatePath);
			ImageIO.write(licensePlateWrong, "bmp", licensePlateWrongPath);
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

	public static String getRandomNumber(boolean wrong)
	{
		String returnString = "";
		while(returnString.length() < rgbLength)
		{
			if(wrong) {
				returnString += wrongRandomNumber.nextInt(2);
			}
			else {
				returnString += randomNumber.nextInt(2);
			}
		}

		return returnString;
	}
}