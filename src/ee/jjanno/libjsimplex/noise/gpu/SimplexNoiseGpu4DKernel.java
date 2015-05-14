package ee.jjanno.libjsimplex.noise.gpu;

import com.amd.aparapi.Kernel;

/*
 * A speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 *
 * This could be speeded up even further, but it's useful as it is.
 *
 * Version 2012-03-09
 *
 * This code was placed in the public domain by its original author,
 * Stefan Gustavson. You may use it as you see fit, but
 * attribution is appreciated.
 * 
 * Parallelization by Jaan Janno 2015
 *
 */

class SimplexNoiseGpu4DKernel extends Kernel {

	private float[] argsFloat = { 0, 0, 0, 0, 0 };
	private int[] argsInt = { 1, 1, 1, 1};

	private float[] r = new float[1];
	
	@Override
	public void run() {
		int i = getGlobalId();
		int x = i % argsInt[0];
		int y = (i % (argsInt[0] * argsInt[1] * argsInt[2]) % (argsInt[0] * argsInt[1]))
				/ (argsInt[1]);
		int z = i / (argsInt[0] * argsInt[1]) % argsInt[2];
		int w = i / (argsInt[0] * argsInt[1] * argsInt[2]);
		r[i] = noise(argsFloat[0] + argsFloat[4] * x, argsFloat[1]
				+ argsFloat[4] * y, argsFloat[2] + argsFloat[4] * z,
				argsFloat[3] + argsFloat[4] * w);
	}

	public float[] getResult() {
		return r;
	}

	public void setParameters(float x, float y, float z, float w, int width,
			int height, int depth, int depth4, float frequency) {
		argsFloat[0] = x;
		argsFloat[1] = y;
		argsFloat[2] = z;
		argsFloat[3] = w;
		argsInt[0] = width;
		argsInt[1] = height;
		argsInt[2] = depth;
		argsInt[3] = depth4;
		argsFloat[4] = frequency;
		r = new float[width * height * depth * depth4];
	}

	private int grad4[] = { 0, 1, 1, 1, 0, 1, 1, -1, 0, 1, -1, 1, 0, 1, -1, -1,
			0, -1, 1, 1, 0, -1, 1, -1, 0, -1, -1, 1, 0, -1, -1, -1, 1, 0, 1, 1,
			1, 0, 1, -1, 1, 0, -1, 1, 1, 0, -1, -1, -1, 0, 1, 1, -1, 0, 1, -1,
			-1, 0, -1, 1, -1, 0, -1, -1, 1, 1, 0, 1, 1, 1, 0, -1, 1, -1, 0, 1,
			1, -1, 0, -1, -1, 1, 0, 1, -1, 1, 0, -1, -1, -1, 0, 1, -1, -1, 0,
			-1, 1, 1, 1, 0, 1, 1, -1, 0, 1, -1, 1, 0, 1, -1, -1, 0, -1, 1, 1,
			0, -1, 1, -1, 0, -1, -1, 1, 0, -1, -1, -1, 0 };

	private short perm[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96,
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

	private short permMod12[] = new short[512];

	public SimplexNoiseGpu4DKernel() {
		for (int i = 0; i < 512; i++) {
			permMod12[i] = (short) (perm[i] % 12);
		}
	}

	private static int fastfloor(float x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	private float dot(float gx, float gy, float gz, float gw, float x, float y,
			float z, float w) {
		return gx * x + gy * y + gz * z + gw * w;
	}

	public float noise(float x, float y, float z, float w) {

		float n0 = 0f, n1 = 0f, n2 = 0f, n3 = 0f, n4 = 0f;
		float s = (x + y + z + w) * 0.30901699437494745f;
		int i = fastfloor(x + s);
		int j = fastfloor(y + s);
		int k = fastfloor(z + s);
		int l = fastfloor(w + s);
		float t = (i + j + k + l) * 0.1381966011250105f;
		float X0 = i - t;
		float Y0 = j - t;
		float Z0 = k - t;
		float W0 = l - t;
		float x0 = x - X0;
		float y0 = y - Y0;
		float z0 = z - Z0;
		float w0 = w - W0;
		int rankx = 0;
		int ranky = 0;
		int rankz = 0;
		int rankw = 0;
		if (x0 > y0)
			rankx++;
		else
			ranky++;
		if (x0 > z0)
			rankx++;
		else
			rankz++;
		if (x0 > w0)
			rankx++;
		else
			rankw++;
		if (y0 > z0)
			ranky++;
		else
			rankz++;
		if (y0 > w0)
			ranky++;
		else
			rankw++;
		if (z0 > w0)
			rankz++;
		else
			rankw++;
		int i1, j1, k1, l1;
		int i2, j2, k2, l2;
		int i3, j3, k3, l3;

		i1 = rankx >= 3 ? 1 : 0;
		j1 = ranky >= 3 ? 1 : 0;
		k1 = rankz >= 3 ? 1 : 0;
		l1 = rankw >= 3 ? 1 : 0;

		i2 = rankx >= 2 ? 1 : 0;
		j2 = ranky >= 2 ? 1 : 0;
		k2 = rankz >= 2 ? 1 : 0;
		l2 = rankw >= 2 ? 1 : 0;

		i3 = rankx >= 1 ? 1 : 0;
		j3 = ranky >= 1 ? 1 : 0;
		k3 = rankz >= 1 ? 1 : 0;
		l3 = rankw >= 1 ? 1 : 0;

		float x1 = x0 - i1 + 0.1381966011250105f;

		float y1 = y0 - j1 + 0.1381966011250105f;
		float z1 = z0 - k1 + 0.1381966011250105f;
		float w1 = w0 - l1 + 0.1381966011250105f;
		float x2 = x0 - i2 + 2.0f * 0.1381966011250105f;

		float y2 = y0 - j2 + 2.0f * 0.1381966011250105f;
		float z2 = z0 - k2 + 2.0f * 0.1381966011250105f;
		float w2 = w0 - l2 + 2.0f * 0.1381966011250105f;
		float x3 = x0 - i3 + 3.0f * 0.1381966011250105f;

		float y3 = y0 - j3 + 3.0f * 0.1381966011250105f;
		float z3 = z0 - k3 + 3.0f * 0.1381966011250105f;
		float w3 = w0 - l3 + 3.0f * 0.1381966011250105f;
		float x4 = x0 - 1.0f + 4.0f * 0.1381966011250105f;

		float y4 = y0 - 1.0f + 4.0f * 0.1381966011250105f;
		float z4 = z0 - 1.0f + 4.0f * 0.1381966011250105f;
		float w4 = w0 - 1.0f + 4.0f * 0.1381966011250105f;

		int ii = i & 255;
		int jj = j & 255;
		int kk = k & 255;
		int ll = l & 255;
		int gi0 = perm[ii + perm[jj + perm[kk + perm[ll]]]] % 32;
		int gi1 = perm[ii + i1 + perm[jj + j1 + perm[kk + k1 + perm[ll + l1]]]] % 32;
		int gi2 = perm[ii + i2 + perm[jj + j2 + perm[kk + k2 + perm[ll + l2]]]] % 32;
		int gi3 = perm[ii + i3 + perm[jj + j3 + perm[kk + k3 + perm[ll + l3]]]] % 32;
		int gi4 = perm[ii + 1 + perm[jj + 1 + perm[kk + 1 + perm[ll + 1]]]] % 32;

		float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
		if (t0 < 0)
			n0 = 0.0f;
		else {
			t0 *= t0;
			n0 = t0
					* t0
					* dot(grad4[gi0 * 4], grad4[gi0 * 4 + 1],
							grad4[gi0 * 4 + 2], grad4[gi0 * 4 + 3], x0, y0, z0,
							w0);
		}
		float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
		if (t1 < 0)
			n1 = 0.0f;
		else {
			t1 *= t1;
			n1 = t1
					* t1
					* dot(grad4[gi1 * 4], grad4[gi1 * 4 + 1],
							grad4[gi1 * 4 + 2], grad4[gi1 * 4 + 3], x1, y1, z1,
							w1);
		}
		float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
		if (t2 < 0)
			n2 = 0.0f;
		else {
			t2 *= t2;
			n2 = t2
					* t2
					* dot(grad4[gi2 * 4], grad4[gi2 * 4 + 1],
							grad4[gi2 * 4 + 2], grad4[gi2 * 4 + 3], x2, y2, z2,
							w2);
		}
		float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
		if (t3 < 0)
			n3 = 0.0f;
		else {
			t3 *= t3;
			n3 = t3
					* t3
					* dot(grad4[gi3 * 4], grad4[gi3 * 4 + 1],
							grad4[gi3 * 4 + 2], grad4[gi3 * 4 + 3], x3, y3, z3,
							w3);
		}
		float t4 = 0.6f - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
		if (t4 < 0)
			n4 = 0.0f;
		else {
			t4 *= t4;
			n4 = t4
					* t4
					* dot(grad4[gi4 * 4], grad4[gi4 * 4 + 1],
							grad4[gi4 * 4 + 2], grad4[gi4 * 4 + 3], x4, y4, z4,
							w4);
		}
		return 27.0f * (n0 + n1 + n2 + n3 + n4);
	}

}
