package ee.jjanno.libjsimplex.test;

import ee.jjanno.libjsimplex.noise.cpu.SimplexNoiseCpu;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu2D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu3D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu4D;

public class PerformanceTest {
	
	static SimplexNoiseGpu2D n2 = new SimplexNoiseGpu2D();
	static SimplexNoiseGpu3D n3 = new SimplexNoiseGpu3D();
	static SimplexNoiseGpu4D n4 = new SimplexNoiseGpu4D();
	
	public static void main(String[] args) {
		
		long algus = System.nanoTime();
		n2.calculate(0f, 0f, 2560, 1440, 1f);
		System.out.println("GPU2d(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		n3.calculate(0f, 0f, 0f, 2560, 1440, 1, 1f);
		System.out.println("GPU3d(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		n4.calculate(0f, 0f, 0f, 0f, 2560, 1440, 1, 1, 1f);
		System.out.println("GPU4d(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		for(int x = 0; x < 2560; x++){
			for(int y = 0; y < 1440; y++){
				SimplexNoiseCpu.noise(x, y);
			}
		}
		System.out.println("CPU2d(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		for(int x = 0; x < 2560; x++){
			for(int y = 0; y < 1440; y++){
				SimplexNoiseCpu.noise(x, y, 0);
			}
		}
		System.out.println("CPU3d(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		for(int x = 0; x < 2560; x++){
			for(int y = 0; y < 1440; y++){
				SimplexNoiseCpu.noise(x, y, 0, 0);
			}
		}
		System.out.println("CPU4d(ns) " + (System.nanoTime() - algus));
	}
}
