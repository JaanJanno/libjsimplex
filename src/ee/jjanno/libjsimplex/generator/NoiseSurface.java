package ee.jjanno.libjsimplex.generator;

import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu2D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu3D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu4D;

public class NoiseSurface {

	public static float[] generate2dRaw(float x, float y, int width, int height,
			float frequency, boolean fast) {
		if (fast)
			return SimplexNoiseGpu2D.calculateFast(x, y, width, height, frequency);
		else
			return SimplexNoiseGpu2D.calculate(x, y, width, height, frequency);
	}

	public static float[] generate2dRawFrom3d(float x, float y, float z, int width,
			int height, float frequency, boolean fast) {
		if (fast)
			return SimplexNoiseGpu3D.calculateFast(x, y, z, width, height, 1, frequency);
		else
			return SimplexNoiseGpu3D.calculate(x, y, z, width, height, 1, frequency);
	}

	public static float[] generate2dRawFrom4d(float x, float y, float z, float w,
			int width, int height, float frequency, boolean fast) {
		if (fast)
			return SimplexNoiseGpu4D.calculateFast(x, y, z, w, width, height, 1, 1,
					frequency);
		else
			return SimplexNoiseGpu4D
					.calculate(x, y, z, w, width, height, 1, 1, frequency);
	}

	public static float[] generate2dRawOctaved(float x, float y, int width,
			int height, float frequency, double persistence, int octaves,
			boolean fast) {
		float[] weights = generateWeights(octaves, persistence);
		if (fast)
			return SimplexNoiseGpu2D.calculateFastOctaved(x, y, width, height, 1, 1,
					frequency, weights);
		else
			return SimplexNoiseGpu2D.calculateOctaved(x, y, width, height, 1, 1,
					frequency, weights);
	}

	public static float[] generate2dRawFrom3dOctaved(float x, float y, float z,
			int width, int height, float frequency, double persistence,
			int octaves, boolean fast) {
		float[] weights = generateWeights(octaves, persistence);
		if (fast)
			return SimplexNoiseGpu3D.calculateFastOctaved(x, y, z, width, height, 1,
					frequency, weights);
		else
			return SimplexNoiseGpu3D.calculateOctaved(x, y, z, width, height, 1,
					frequency, weights);
	}

	public static float[] generate2dRawFrom4dOctaved(float x, float y, float z,
			float w, int width, int height, float frequency,
			double persistence, int octaves, boolean fast) {
		float[] weights = generateWeights(octaves, persistence);
		if (fast)
			return SimplexNoiseGpu4D.calculateFastOctaved(x, y, z, w, width, height, 1,
					1, frequency, weights);
		else
			return SimplexNoiseGpu4D.calculateOctaved(x, y, z, w, width, height, 1, 1,
					frequency, weights);
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
