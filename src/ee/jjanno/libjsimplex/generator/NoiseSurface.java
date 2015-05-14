package ee.jjanno.libjsimplex.generator;

import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu2D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu3D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu4D;

public class NoiseSurface {
	
	/**
	 * Generates a 2d surface of noise in a 1D array representation.
	 * Array is laid out as consecutive rows.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 1D array representation.
	 */

	public static float[] generate2dRaw(float x, float y, int width,
			int height, float frequency, boolean fast) {
		if (fast)
			return SimplexNoiseGpu2D.calculateFast(x, y, width, height,
					frequency);
		else
			return SimplexNoiseGpu2D.calculate(x, y, width, height, frequency);
	}
	
	/**
	 * Generates a 2d surface of noise in a 2D array representation.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 2D array representation.
	 */
	
	public static float[][] generate2d(float x, float y, int width,
			int height, float frequency, boolean fast) {
		return unPack2D(generate2dRaw(x, y, width, height, frequency, fast), width, height);
	}
	
	/**
	 * Generates a 2d surface of noise in a 1D array representation.
	 * Array is laid out as consecutive rows.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 1D array representation.
	 */

	public static float[] generate2dRawFrom3d(float x, float y, float z,
			int width, int height, float frequency, boolean fast) {
		if (fast)
			return SimplexNoiseGpu3D.calculateFast(x, y, z, width, height, 1,
					frequency);
		else
			return SimplexNoiseGpu3D.calculate(x, y, z, width, height, 1,
					frequency);
	}
	
	/**
	 * Generates a 2d surface of noise in a 2D array representation.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 2D array representation.
	 */
	
	public static float[][] generate2dFrom3d(float x, float y, float z,
			int width, int height, float frequency, boolean fast) {
		return unPack2D(generate2dRawFrom3d(x, y, z, width, height, frequency, fast), width, height);
	}
	
	/**
	 * Generates a 2d surface of noise in a 1D array representation.
	 * Array is laid out as consecutive rows.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param w W coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 1D array representation.
	 */

	public static float[] generate2dRawFrom4d(float x, float y, float z,
			float w, int width, int height, float frequency, boolean fast) {
		if (fast)
			return SimplexNoiseGpu4D.calculateFast(x, y, z, w, width, height,
					1, 1, frequency);
		else
			return SimplexNoiseGpu4D.calculate(x, y, z, w, width, height, 1, 1,
					frequency);
	}
	
	/**
	 * Generates a 2d surface of noise in a 2D array representation.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param w W coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 2D array representation.
	 */
	
	
	public static float[][] generate2dFrom4d(float x, float y, float z,
			float w, int width, int height, float frequency, boolean fast) {
		return unPack2D(generate2dRawFrom4d(x, y, z, w, width, height, frequency, fast), width, height);
	}
	
	/**
	 * Generates a 2d surface of multi-octave noise in a 1D array representation.
	 * Array is laid out as consecutive rows.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param persistence Contribution from a frequency weighted against the last octave. (1 for equal, 0.5 for half, 2 for double etc.)
	 * @param octaves Number of sub-frequencies generated in the noise.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 1D array representation.
	 */

	public static float[] generate2dRawOctaved(float x, float y, int width,
			int height, float frequency, double persistence, int octaves,
			boolean fast) {
		float[] weights = generateWeights(octaves, persistence);
		if (fast)
			return SimplexNoiseGpu2D.calculateFastOctaved(x, y, width, height,
					1, 1, frequency, weights);
		else
			return SimplexNoiseGpu2D.calculateOctaved(x, y, width, height, 1,
					1, frequency, weights);
	}
	
	/**
	 * Generates a 2d surface of multi-octave noise in a 2D array representation.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param persistence Contribution from a frequency weighted against the last octave. (1 for equal, 0.5 for half, 2 for double etc.)
	 * @param octaves Number of sub-frequencies generated in the noise.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 2D array representation.
	 */
	
	public static float[][] generate2dOctaved(float x, float y, int width,
			int height, float frequency, double persistence, int octaves,
			boolean fast) {
		return unPack2D(generate2dRawOctaved(x, y, width, height, frequency, persistence, octaves, fast), width, height);
	}
	
	/**
	 * Generates a 2d surface of noise multi-octave in a 1D array representation.
	 * Array is laid out as consecutive rows.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param persistence Contribution from a frequency weighted against the last octave. (1 for equal, 0.5 for half, 2 for double etc.)
	 * @param octaves Number of sub-frequencies generated in the noise.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 1D array representation.
	 */	

	public static float[] generate2dRawFrom3dOctaved(float x, float y, float z,
			int width, int height, float frequency, double persistence,
			int octaves, boolean fast) {
		float[] weights = generateWeights(octaves, persistence);
		if (fast)
			return SimplexNoiseGpu3D.calculateFastOctaved(x, y, z, width,
					height, 1, frequency, weights);
		else
			return SimplexNoiseGpu3D.calculateOctaved(x, y, z, width, height,
					1, frequency, weights);
	}
	
	/**
	 * Generates a 2d surface of multi-octave noise in a 2D array representation.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param persistence Contribution from a frequency weighted against the last octave. (1 for equal, 0.5 for half, 2 for double etc.)
	 * @param octaves Number of sub-frequencies generated in the noise.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 2D array representation.
	 */
	
	public static float[][] generate2dFrom3dOctaved(float x, float y, float z,
			int width, int height, float frequency, double persistence,
			int octaves, boolean fast) {
		return unPack2D(generate2dRawFrom3dOctaved(x, y, z, width, height, frequency, persistence, octaves, fast), width, height);
	}
	
	/**
	 * Generates a 2d surface of multi-octave noise in a 1D array representation.
	 * Array is laid out as consecutive rows.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param w W coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param persistence Contribution from a frequency weighted against the last octave. (1 for equal, 0.5 for half, 2 for double etc.)
	 * @param octaves Number of sub-frequencies generated in the noise.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 1D array representation.
	 */

	public static float[] generate2dRawFrom4dOctaved(float x, float y, float z,
			float w, int width, int height, float frequency,
			double persistence, int octaves, boolean fast) {
		float[] weights = generateWeights(octaves, persistence);
		if (fast)
			return SimplexNoiseGpu4D.calculateFastOctaved(x, y, z, w, width,
					height, 1, 1, frequency, weights);
		else
			return SimplexNoiseGpu4D.calculateOctaved(x, y, z, w, width,
					height, 1, 1, frequency, weights);
	}
	
	/**
	 * Generates a 2d surface of multi-octave noise in a 2D array representation.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param z Z coordinate of the surface in noise space.
	 * @param w W coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param persistence Contribution from a frequency weighted against the last octave. (1 for equal, 0.5 for half, 2 for double etc.)
	 * @param octaves Number of sub-frequencies generated in the noise.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of noise in a 2D array representation.
	 */
	
	public static float[][] generate2dFrom4dOctaved(float x, float y, float z,
			float w, int width, int height, float frequency,
			double persistence, int octaves, boolean fast) {
		return unPack2D(generate2dRawFrom4dOctaved(x, y, z, w, width, height, frequency, persistence, octaves, fast), width, height);
	}
	
	/**
	 * Generates a 2d surface of tiled multi-octave noise in a 1D array representation.
	 * Array is laid out as consecutive rows.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of tiled multi-octave noise in a 1D array representation.
	 */

	public static float[] generate2dRawTiledOctaved(float x, float y, int width,
			int height, int xSlices, int ySlices, float frequency, double persistence, int octaves) {
		float[] weights = generateWeights(octaves, persistence);
		return SimplexNoiseGpu4D.calculateTiled(x, y, width, height, xSlices, ySlices, frequency,
				weights);
	}
	
	/**
	 * Generates a 2d surface of tiled multi-octave noise in a 2D array representation.
	 * 
	 * @param x X coordinate of the surface in noise space.
	 * @param y Y coordinate of the surface in noise space.
	 * @param width Width of the surface.
	 * @param height Height of the surface.
	 * @param frequency Base frequency of the noise wave.
	 * @param fast Whether memory bandwidth optimizations are used.
	 * @return 2D surface of tiled multi-octave noise in a 2D array representation.
	 */
	
	public static float[][] generate2dTiledOctaved(float x, float y, int width,
			int height, int xSlices, int ySlices, float frequency, double persistence, int octaves) {
		return unPack2D(generate2dRawTiledOctaved(x, y, width, height, xSlices, ySlices, frequency, persistence, octaves), width, height);
	}
	
	private static float[][] unPack2D(float[] noise, int width, int height) {
		float[][] unpacked = new float[width][height];
		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++)
				unpacked[y][x] = noise[y*width+x];
		return unpacked;
	}

	private static float[] generateWeights(int octaves, double persistence) {
		float[] weights = new float[octaves];
		float totalWeight = 0;
		float lastWeight = 1;
		for (int i = 0; i < octaves; i++) {
			totalWeight += lastWeight;
			weights[i] = lastWeight;
			lastWeight *= persistence;
		}
		normalize(weights, totalWeight);
		return weights;
	}

	private static void normalize(float[] weights, float totalWeight) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = weights[i] / totalWeight;
		}
	}

}
