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
	public static File darkVisible = new File("DarkVisible.bmp");
	public static File lightVisible = new File("LightVisible.bmp");

	public static BufferedImage licensePlate = null;
	public static BufferedImage darkPlateVisible = null;
	public static BufferedImage lightPlateVisible = null;

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
			darkPlateVisible = new BufferedImage(licensePlate.getWidth(), licensePlate.getHeight(), BufferedImage.TYPE_INT_RGB);
			lightPlateVisible = new BufferedImage(licensePlate.getWidth(), licensePlate.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		catch(IOException ioe) { ioe.printStackTrace(); }

		// Hide Image in Dark/Light Images
		for(int i = 0; i < licensePlate.getWidth(); i++)
		{
			for(int j = 0; j < licensePlate.getHeight(); j++)
			{
				String pixelBinary = Integer.toBinaryString(licensePlate.getRGB(i, j)).substring(8);

				// 24 bits
				String darkVis = dark12 + pixelBinary.substring(0, 12);
				String lightVis = light12 + pixelBinary.substring(12, 24);

				int alpha = 0xFF;

				int dVRed = Integer.parseInt(darkVis.substring(0, 8), 2); 
				int dVGreen = Integer.parseInt(darkVis.substring(8, 16), 2); 
				int dVBlue = Integer.parseInt(darkVis.substring(16, 24), 2);

				int lVRed = Integer.parseInt(lightVis.substring(0, 8), 2); 
				int lVGreen = Integer.parseInt(lightVis.substring(8, 16), 2); 
				int lVBlue = Integer.parseInt(lightVis.substring(16, 24), 2);

				darkPlateVisible.setRGB(i, j, ((alpha << 24) + (dVRed << 16) + (dVGreen << 8) + dVBlue));
				lightPlateVisible.setRGB(i, j, ((alpha << 24) + (lVRed << 16) + (lVGreen << 8) + lVBlue));
			}
		}

		// Save Shifted Images
		try 
		{
			ImageIO.write(darkPlateVisible, "bmp", darkVisible);
			ImageIO.write(lightPlateVisible, "bmp", lightVisible);
		}
		catch(IOException ioe) { ioe.printStackTrace(); }
	}
}