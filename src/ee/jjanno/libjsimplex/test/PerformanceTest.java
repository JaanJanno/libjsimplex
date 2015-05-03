package ee.jjanno.libjsimplex.test;

import ee.jjanno.libjsimplex.noise.cpu.SimplexNoiseCpu;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu2D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu3D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu4D;

public class PerformanceTest {
	
	public static void main(String[] args) {
		
		SimplexNoiseGpu2D.calculate(0f, 0f, 2560, 1440, 1f);
		SimplexNoiseGpu2D.calculate(0f, 0f, 2560, 1440, 1f);
		long algus = System.nanoTime();
		SimplexNoiseGpu2D.calculate(0f, 0f, 2560, 1440, 1f);
		System.out.println("GPU2dOrig(ns) " + (System.nanoTime() - algus));
		
		SimplexNoiseGpu3D.calculate(0f, 0f, 0f, 2560, 1440, 1, 1f);
		algus = System.nanoTime();
		SimplexNoiseGpu3D.calculate(0f, 0f, 0f, 2560, 1440, 1, 1f);
		System.out.println("GPU3dOrig(ns) " + (System.nanoTime() - algus));
		
		SimplexNoiseGpu4D.calculate(0f, 0f, 0f, 0f, 2560, 1440, 1, 1, 1f);
		algus = System.nanoTime();
		SimplexNoiseGpu4D.calculate(0f, 0f, 0f, 0f, 2560, 1440, 1, 1, 1f);
		System.out.println("GPU4dOrig(ns) " + (System.nanoTime() - algus));
		
		SimplexNoiseGpu2D.calculateFast(0f, 0f, 2560, 1440, 1f);
		algus = System.nanoTime();
		SimplexNoiseGpu2D.calculateFast(0f, 0f, 2560, 1440, 1f);
		System.out.println("GPU2dFast(ns) " + (System.nanoTime() - algus));
		
		SimplexNoiseGpu3D.calculateFast(0f, 0f, 0f, 2560, 1440, 1, 1f);
		SimplexNoiseGpu3D.calculateFast(0f, 0f, 0f, 2560, 1440, 1, 1f);
		algus = System.nanoTime();
		SimplexNoiseGpu3D.calculateFast(0f, 0f, 0f, 2560, 1440, 1, 1f);
		System.out.println("GPU3dFast(ns) " + (System.nanoTime() - algus));
		
		SimplexNoiseGpu4D.calculateFast(0f, 0f, 0f, 0f, 2560, 1440, 1, 1, 1f);
		algus = System.nanoTime();
		SimplexNoiseGpu4D.calculateFast(0f, 0f, 0f, 0f, 2560, 1440, 1, 1, 1f);
		System.out.println("GPU4dFast(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		for(int x = 0; x < 2560; x++){
			for(int y = 0; y < 1440; y++){
				SimplexNoiseCpu.noise(x, y);
			}
		}
		System.out.println("CPU2dOrig(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		for(int x = 0; x < 2560; x++){
			for(int y = 0; y < 1440; y++){
				SimplexNoiseCpu.noise(x, y, 0);
			}
		}
		System.out.println("CPU3dOrig(ns) " + (System.nanoTime() - algus));
		
		algus = System.nanoTime();
		for(int x = 0; x < 2560; x++){
			for(int y = 0; y < 1440; y++){
				SimplexNoiseCpu.noise(x, y, 0, 0);
			}
		}
		System.out.println("CPU4dOrig(ns) " + (System.nanoTime() - algus));
	}
}
