package ee.jjanno.libjsimplex.util.colorizer;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Grayscale {
	
	public float[] getGrayscaleRGBArray(float[][] input) {
		int width = input[0].length;
		int height = input.length;
		
		float[] output = new float[height*width*3];
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				output[y*3*width+x*3] = (input[y][x] + 1f) * 128f;
				output[y*3*width+x*3+1] = (input[y][x] + 1f) * 128f;
				output[y*3*width+x*3+2] = (input[y][x] + 1f) * 128f;
			}
		}
		return output;
	}
	
	public float[] getGrayscaleRGBArray(float[] input) {
	
		float[] output = new float[input.length*3];
		for(int x = 0; x < input.length; x++){
				output[x*3] = (input[x] + 1f) * 128f;
				output[x*3+1] = (input[x] + 1f) * 128f;
				output[x*3+2] = (input[x] + 1f) * 128f;
		}
		return output;
	}
	
	public float[] getGrayscaleRGBArray(float[][] input, float contrast) {
		
		float[] noise = getGrayscaleRGBArray(input);
		for(int i = 0; i < noise.length; i++)
			noise[i] *= (float)Math.pow(noise[i] / 256, contrast);
		return noise;
	}
	
	public BufferedImage getBufferedImage(float[][] input) {
		int width = input[0].length;
		int height = input.length;	
		BufferedImage imgB = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) imgB.getRaster();	
		float[] imgF = getGrayscaleRGBArray(input);
		raster.setPixels(0, 0, width, height, imgF);	
		imgB.setData(raster);
		return imgB;
	}
	
	public BufferedImage getBufferedImage(float[] input, int width, int height) {

		BufferedImage imgB = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) imgB.getRaster();	
		float[] imgF = getGrayscaleRGBArray(input);
		raster.setPixels(0, 0, width, height, imgF);	
		imgB.setData(raster);
		return imgB;
	}

}
