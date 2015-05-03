package ee.jjanno.libjsimplex.noise.gpu;

public class SimplexNoiseGpu2D {

	private static SimplexNoiseGpu2DKernel simplexKernel = new SimplexNoiseGpu2DKernel();
	private static SimplexNoiseGpu2DKernelIntNoised fastSimplexKernel = new SimplexNoiseGpu2DKernelIntNoised();

	private static SimplexNoiseGpu2DKernelOctaved simplexKernelOctaved = new SimplexNoiseGpu2DKernelOctaved();
	private static SimplexNoiseGpu2DKernelIntNoisedOctaved fastSimplexKernelOctaved = new SimplexNoiseGpu2DKernelIntNoisedOctaved();

	public static synchronized float[] calculate(float x, float y, int width,
			int height, float frequency) {
		simplexKernel.setParameters(x, y, width, height, frequency);
		simplexKernel.execute(width * height);
		return simplexKernel.getResult();
	}

	public static synchronized float[] calculateFast(float x, float y, int width,
			int height, float frequency) {
		fastSimplexKernel.setParameters(x, y, width, height, frequency);
		fastSimplexKernel.execute(width * height);
		return fastSimplexKernel.getResult();
	}

	public static synchronized float[] calculateOctaved(float x, float y, int width,
			int height, int depth, int depth4, float frequency, float[] weight) {
		simplexKernelOctaved.setParameters(x, y, width, height, frequency,
				weight);
		simplexKernelOctaved.execute(width * height);
		return simplexKernelOctaved.getResult();
	}

	public static synchronized float[] calculateFastOctaved(float x, float y,
			int width, int height, int depth, int depth4, float frequency,
			float[] weight) {
		fastSimplexKernelOctaved.setParameters(x, y, width, height, frequency,
				weight);
		fastSimplexKernelOctaved.execute(width * height);
		return fastSimplexKernelOctaved.getResult();
	}
}
