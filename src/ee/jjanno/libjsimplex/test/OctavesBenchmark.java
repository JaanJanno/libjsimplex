package ee.jjanno.libjsimplex.test;

import ee.jjanno.libjsimplex.generator.NoiseSurface;
import ee.jjanno.libjsimplex.noise.cpu.SimplexNoiseCpu;

public class OctavesBenchmark {

	private static int[] octavesList = { 1, 3, 5, 7, 10, 30, 100 };

	public static void main(String[] args) {
		
		
		
		long a;
		
		for(int octaves: octavesList) {
			System.out.println("GPUO: " + Integer.toString(octaves) + " octaves.");
			for(int i = 0; i < 2; i++)
				NoiseSurface.generate2dRawFrom4dOctaved(0,0,0,0, 2560, 1440, 1, 0.5f, octaves, false);
			a = System.nanoTime();
			for(int i = 0; i < 10; i++)
				NoiseSurface.generate2dRawFrom4dOctaved(0,0,0,0, 2560, 1440, 1, 0.5f, octaves, false);
			System.out.println(Double.toString((System.nanoTime() - a) / 1000000000d));
		}
		
		for(int octaves: octavesList) {
			System.out.println("GPUF: " + Integer.toString(octaves) + " octaves.");
			for(int i = 0; i < 2; i++)
				NoiseSurface.generate2dRawFrom4dOctaved(0,0,0,0, 2560, 1440, 1, 0.5f, octaves, true);
			a = System.nanoTime();
			for(int i = 0; i < 10; i++)
				NoiseSurface.generate2dRawFrom4dOctaved(0,0,0,0, 2560, 1440, 1, 0.5f, octaves, true);
			System.out.println(Double.toString((System.nanoTime() - a) / 1000000000d));
		}
		
		for(int octaves: octavesList) {
			System.out.println("CPUO: " + Integer.toString(octaves) + " octaves.");
			a = System.nanoTime();
			for(int i = 0; i < 10; i++)
				cpuOctaves(octaves, 2560, 1440);
			System.out.println(Double.toString((System.nanoTime() - a) / 1000000000d));
		}

	}

	private static void cpuOctaves(int o, int x, int y) {
		float[] weights = generateWeights(o, 0.5f);
		double[] data = new double[x * y];
		for (int i = 0; i < o; i++)
			calc(x, y, weights[i], data);
	}

	private static void calc(int x, int y, float weight, double[] data) {
		for (int i = 0; i < x * y; i++)
			data[i] += SimplexNoiseCpu.noise(0, 0, 0, 0) * weight;
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
