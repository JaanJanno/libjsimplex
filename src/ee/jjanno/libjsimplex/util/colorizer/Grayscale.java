package ee.jjanno.libjsimplex.util.colorizer;

public class Grayscale {
	
	float input[];
	float output[];

	public void execute(int length) {
		for(int i = 0; i < length; i++){
			output[i*3+0] = (input[i] + 1f) * 128f;
			output[i*3+1] = (input[i] + 1f) * 128f;
			output[i*3+2] = (input[i] + 1f) * 128f;
		}
	}
	
	public float[] getGrayscaleRGBArray(float[] a) {
		input = a;
		output = new float[a.length * 3];
		execute(a.length);
		return output;
	}

}
