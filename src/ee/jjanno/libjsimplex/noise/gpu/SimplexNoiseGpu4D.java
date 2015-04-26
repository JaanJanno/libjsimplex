package ee.jjanno.libjsimplex.noise.gpu;

public class SimplexNoiseGpu4D {

	static SimplexNoiseGpu4DKernelIntNoised simplexKernel = new SimplexNoiseGpu4DKernelIntNoised();
	static {
		simplexKernel.execute(1);
	}

	public float[] calculate(float x, float y, float z, float w, int width,
			int height, int depth, int depth4, float frequency) {
		simplexKernel.setParameters(x, y, z, w, width, height, depth, depth4,
				frequency);
		simplexKernel.execute(width * height, depth);
		return simplexKernel.getResult();
	}
}