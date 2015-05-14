package ee.jjanno.libjsimplex.util.colorizer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

public class ColorMapper {

	List<ColorRange> colorRanges = new ArrayList<ColorRange>();

	/**
	 * Calculates an RGB array given an array of noise data.
	 * 
	 * @param input Array of noise data.
	 * @return RGB array representing the noise input.
	 */
	
	public float[] getRGBArray(float[][] input) {
		int width = input[0].length;
		int height = input.length;
		
		float[] output = new float[height*width*3];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				float pointValue = input[y][x];
				Color newColor = assignColor(pointValue);
				
				output[y*3*width+x*3] = newColor.getRed();
				output[y*3*width+x*3+1] = newColor.getGreen();
				output[y*3*width+x*3+2] = newColor.getBlue();
			}
		}
		return output;
	}
	
	/**
	 * Calculates an RGB array given an array of noise data.
	 * 
	 * @param input Array of noise data.
	 * @return RGB array representing the noise input.
	 */
	
	public float[] getRGBArray(float[] input) {
		
		float[] output = new float[input.length*3];
		for(int x = 0; x < input.length; x++){
				float pointValue = input[x];
				Color newColor = assignColor(pointValue);
				
				output[3*x] = newColor.getRed();
				output[3*x+1] = newColor.getGreen();
				output[3*x+2] = newColor.getBlue();
		}
		return output;
	}
	
	/**
	 * Calculates a BufferedImage given a 2D array of noise data.
	 * 
	 * @param input 2D array of noise data.
	 * @return BufferedImage representing the noise input.
	 */
	
	public BufferedImage getBufferedImage(float[][] input) {
		int width = input[0].length;
		int height = input.length;	
		BufferedImage imgB = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) imgB.getRaster();	
		float[] imgF = getRGBArray(input);
		raster.setPixels(0, 0, width, height, imgF);	
		imgB.setData(raster);
		return imgB;
	}
	
	/**
	 * Calculates a BufferedImage given a 1D array of noise data and the desired image's dimensions.
	 * 
	 * @param input Array of noise data.
	 * @param width Width of the noise surface.
	 * @param height Height of the noise surface.
	 * @return
	 */
	
	public BufferedImage getBufferedImage(float[] input, int width, int height) {
		BufferedImage imgB = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) imgB.getRaster();	
		float[] imgF = getRGBArray(input);
		raster.setPixels(0, 0, width, height, imgF);	
		imgB.setData(raster);
		return imgB;
	}
	
	/**
	 * Adds a color range rule used when generating a RGB array of BufferedImage.
	 * 
	 * @param lowerLimit Lowest value of noise this rule applies to.
	 * @param upperLimit Highest value of noise this rule applies to.
	 * @param lowerColor Color representing the lowest value of noise.
	 * @param upperColor Color representing the highest value of noise.
	 * @param contrast Sets contrast of the color range.
	 */
	
	public void addRange(float lowerLimit, float upperLimit, Color lowerColor,
			Color upperColor, double contrast) {
		colorRanges.add(new ColorRange(lowerLimit, upperLimit, lowerColor, upperColor, contrast));
	}
	
	private Color assignColor(float pointValue) {
		pointValue = Math.min(pointValue, 1f);
		pointValue = Math.max(pointValue, -1f);
		for(ColorRange range: colorRanges){
			if(range.test(pointValue))
				return range.getColor(pointValue);
		}
		float grayScaled = (pointValue + 1f) / 2;
		return new Color(grayScaled,grayScaled,grayScaled);
	}

	private static class ColorRange {

		private float lowerLimit;
		private float upperLimit;
		private Color lowerColor;
		private Color upperColor;
		private double contrast;

		public ColorRange(float lowerLimit, float upperLimit, Color lowerColor,
				Color upperColor, double contrast) {
			super();
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
			this.lowerColor = lowerColor;
			this.upperColor = upperColor;
			this.contrast = contrast;
		}

		public boolean test(float point) {
			return point >= lowerLimit && point <= upperLimit;
		}

		public Color getColor(float point) {
			float relativePosition = (point - lowerLimit)
					/ (upperLimit - lowerLimit);
			int red = lowerColor.getRed();
			int green = lowerColor.getGreen();
			int blue = lowerColor.getBlue();
			int redRange = upperColor.getRed() - red;
			int greenRange = upperColor.getGreen() - green;
			int blueRange = upperColor.getBlue() - blue;
			if (contrast != 1)
				relativePosition = (float) Math.pow(relativePosition, contrast);
			return new Color((int)(red + (float)redRange * relativePosition), (int)(green
					+ (float)greenRange * relativePosition), (int)(blue + (float)blueRange
					* relativePosition));
		}
	}

}
