package ee.jjanno.libjsimplex.noise.gpu;



public class SimplexNoiseGpu2D {
	
	static SimplexNoiseGpu2DKernelIntNoised simplexKernel = new SimplexNoiseGpu2DKernelIntNoised();
	static {
		simplexKernel.execute(1);
	}
	
	public float[] calculate(float x, float y, int width, int height,
			float frequency) {
		simplexKernel.setParameters(x, y, width, height, frequency);
		simplexKernel.execute(width * height);
		return simplexKernel.getResult();
	}
}
