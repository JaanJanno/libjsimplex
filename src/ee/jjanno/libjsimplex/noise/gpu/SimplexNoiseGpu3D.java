package ee.jjanno.libjsimplex.noise.gpu;

public class SimplexNoiseGpu3D {

	private static SimplexNoiseGpu3DKernel simplexKernel = new SimplexNoiseGpu3DKernel();
	private static SimplexNoiseGpu3DKernelIntNoised fastSimplexKernel = new SimplexNoiseGpu3DKernelIntNoised();

	private static SimplexNoiseGpu3DKernelOctaved simplexKernelOctaved = new SimplexNoiseGpu3DKernelOctaved();
	private static SimplexNoiseGpu3DKernelIntNoisedOctaved fastSimplexKernelOctaved = new SimplexNoiseGpu3DKernelIntNoisedOctaved();

	public static synchronized float[] calculate(float x, float y, float z, int width,
			int height, int depth, float frequency) {
		simplexKernel.setParameters(x, y, z, width, height, depth, frequency);
		simplexKernel.execute(width * height * depth);
		return simplexKernel.getResult();
	}

	public static synchronized float[] calculateFast(float x, float y, float z,
			int width, int height, int depth, float frequency) {
		fastSimplexKernel.setParameters(x, y, z, width, height, depth,
				frequency);
		fastSimplexKernel.execute(width * height * depth);
		return fastSimplexKernel.getResult();
	}

	public static synchronized float[] calculateOctaved(float x, float y, float z,
			int width, int height, int depth, float frequency, float[] weight) {
		simplexKernelOctaved.setParameters(x, y, z, width, height, depth,
				frequency, weight);
		simplexKernelOctaved.execute(width * height * depth);
		return simplexKernelOctaved.getResult();
	}

	public static synchronized float[] calculateFastOctaved(float x, float y, float z,
			int width, int height, int depth, float frequency, float[] weight) {
		fastSimplexKernelOctaved.setParameters(x, y, z, width, height, depth,
				frequency, weight);
		fastSimplexKernelOctaved.execute(width * height * depth);
		return fastSimplexKernelOctaved.getResult();
	}
}