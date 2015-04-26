package ee.jjanno.libjsimplex.noise.gpu;



public class SimplexNoiseGpu3D {
	
	static SimplexNoiseGpu3DKernelIntNoised simplexKernel = new SimplexNoiseGpu3DKernelIntNoised();
	static {
		simplexKernel.execute(1);
	}
	
	public float[] calculate(float x, float y, float z, int width, int height, int depth,
			float frequency) {
		simplexKernel.setParameters(x, y, z, width, height, depth, frequency);
		simplexKernel.execute(width * height, depth);
		return simplexKernel.getResult();
	}
}