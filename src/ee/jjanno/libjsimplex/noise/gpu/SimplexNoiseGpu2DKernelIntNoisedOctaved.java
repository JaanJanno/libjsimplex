package ee.jjanno.libjsimplex.noise.gpu;

import com.amd.aparapi.Kernel;

class SimplexNoiseGpu2DKernelIntNoisedOctaved extends Kernel {

	private float[] argsFloat = { 0, 0, 0 };
	private int[] argsInt = { 0, 0 };
	private float[] r = { 0 };
	private float[] argsWeight = { 1 };

	public SimplexNoiseGpu2DKernelIntNoisedOctaved() {
		super();
		for (int i = 0; i < 512; i++) {
			permMod12[i] = (short) (perm[i] % 12);
		}
	}
	
	private int intNoise(int n) {
		n = ((n + 463856334) >> 13) ^ (n + 575656768);
		return ((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff & 255);
	}

	private float grad3[] = { 1, 1, -1, 1, 1, -1, -1, -1, 1, 0, -1, 0, 1, 0,
			-1, 0, 0, 1, 0, -1, 0, 1, 0, -1 };

	private short permMod12[] = new short[512];

	private int fastfloor(float x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	private float dot(float gx, float gy, float x, float y) {
		return gx * x + gy * y;
	}

	public float noise(float xin, float yin, int index) {
		float n0 = 0f, n1 = 0f, n2 = 0f;
		int i1 = 0, j1 = 0;
		float s = (xin + yin) * 0.3660254037844386f;
		int i = fastfloor(xin + s);
		int j = fastfloor(yin + s);
		float t = (i + j) * 0.21132486540518713f;
		float X0 = i - t;
		float Y0 = j - t;
		float x0 = xin - X0;
		float y0 = yin - Y0;

		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		} else {
			i1 = 0;
			j1 = 1;
		}
		float x1 = x0 - i1 + 0.21132486540518713f;
		float y1 = y0 - j1 + 0.21132486540518713f;
		float x2 = x0 - 1.0f + 2.0f * 0.21132486540518713f;
		float y2 = y0 - 1.0f + 2.0f * 0.21132486540518713f;
		int ii = i & 255;
		int jj = j & 255;
		int gi0 = intNoise(ii + intNoise(jj)) % 12;
		int gi1 = intNoise(ii + i1 + intNoise(jj + j1)) % 12;
		int gi2 = intNoise(ii + 1 + intNoise(jj + 1)) % 12;
		float t0 = 0.5f - x0 * x0 - y0 * y0;
		if (t0 < 0)
			n0 = 0.0f;
		else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad3[gi0 * 2], grad3[gi0 * 2 + 1], x0, y0);
		}
		float t1 = 0.5f - x1 * x1 - y1 * y1;
		if (t1 < 0)
			n1 = 0.0f;
		else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad3[gi1 * 2], grad3[gi1 * 2 + 1], x1, y1);
		}
		float t2 = 0.5f - x2 * x2 - y2 * y2;
		if (t2 < 0)
			n2 = 0.0f;
		else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad3[gi2 * 2], grad3[gi2 * 2 + 1], x2, y2);
		}
		return 70.0f * (n0 + n1 + n2) * argsWeight[index];
	}

	@Override
	public void run() {
		int i = getGlobalId();
		int x = i % argsInt[0];
		int y = i / argsInt[0];
		
		float accumulator = 0;

		for (int j = 0; j < argsInt[2]; j++) {
			float power = pow(2, j);
			float newFrequency = argsFloat[2] * power;

			accumulator += noise(argsFloat[0] * power + newFrequency * x,
					argsFloat[1] * power + newFrequency * y, j);
		}
		r[i] = accumulator;
	}

	public float[] getResult() {
		return r;
	}

	public void setParameters(float x, float y, int width,
			int height, float frequency, float[] weight) {
		argsWeight = weight;
		argsFloat = new float[] { x, y, frequency };
		argsInt = new int[] { width, height, weight.length };
		r = new float[width * height];
	}
	
	private static short perm[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96,
		53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240,
		21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94,
		252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87,
		174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48,
		27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230,
		220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63,
		161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
		135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64,
		52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82,
		85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223,
		183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101,
		155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113,
		224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193,
		238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14,
		239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176,
		115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114,
		67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180, 151,
		160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225,
		140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148,
		247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11,
		32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168,
		68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83,
		111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245,
		40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76,
		132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86,
		164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
		5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47,
		16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2,
		44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39,
		253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218,
		246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162,
		241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181,
		199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150,
		254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128,
		195, 78, 66, 215, 61, 156, 180 };

}
