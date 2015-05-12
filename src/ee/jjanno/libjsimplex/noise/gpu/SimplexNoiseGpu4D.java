package ee.jjanno.libjsimplex.noise.gpu;

public class SimplexNoiseGpu4D {

	private static SimplexNoiseGpu4DKernel simplexKernel = new SimplexNoiseGpu4DKernel();
	private static SimplexNoiseGpu4DKernelIntNoised fastSimplexKernel = new SimplexNoiseGpu4DKernelIntNoised();

	private static SimplexNoiseGpu4DKernelOctaved simplexKernelOctaved = new SimplexNoiseGpu4DKernelOctaved();
	private static SimplexNoiseGpu4DKernelIntNoisedOctaved fastSimplexKernelOctaved = new SimplexNoiseGpu4DKernelIntNoisedOctaved();

	private static SimplexNoiseGpu4DKernelTiled tiledSimplexKernelOctaved = new SimplexNoiseGpu4DKernelTiled();

	public static synchronized float[] calculate(float x, float y, float z,
			float w, int width, int height, int depth, int depth4,
			float frequency) {
		simplexKernel.setParameters(x, y, z, w, width, height, depth, depth4,
				frequency);
		simplexKernel.execute(width * height * depth * depth4);
		return simplexKernel.getResult();
	}

	public static synchronized float[] calculateFast(float x, float y, float z,
			float w, int width, int height, int depth, int depth4,
			float frequency) {
		fastSimplexKernel.setParameters(x, y, z, w, width, height, depth,
				depth4, frequency);
		fastSimplexKernel.execute(width * height * depth * depth4);
		return fastSimplexKernel.getResult();
	}

	public static synchronized float[] calculateOctaved(float x, float y,
			float z, float w, int width, int height, int depth, int depth4,
			float frequency, float[] weight) {
		simplexKernelOctaved.setParameters(x, y, z, w, width, height, depth,
				depth4, frequency, weight);
		simplexKernelOctaved.execute(width * height * depth * depth4);
		return simplexKernelOctaved.getResult();
	}

	public static synchronized float[] calculateFastOctaved(float x, float y,
			float z, float w, int width, int height, int depth, int depth4,
			float frequency, float[] weight) {
		fastSimplexKernelOctaved.setParameters(x, y, z, w, width, height,
				depth, depth4, frequency, weight);
		fastSimplexKernelOctaved.execute(width * height * depth * depth4);
		return fastSimplexKernelOctaved.getResult();
	}

	public static synchronized float[] calculateTiled(float x, float y,
			int width, int height, int xSlices, int ySlices, float frequency, float[] weight) {
		tiledSimplexKernelOctaved.setParameters(x, y, width, height, xSlices, ySlices, frequency,
				weight);
		tiledSimplexKernelOctaved.execute(width * height);
		return tiledSimplexKernelOctaved.getResult();
	}

}