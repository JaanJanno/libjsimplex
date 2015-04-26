package ee.jjanno.libjsimplex.test;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu2D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu3D;
import ee.jjanno.libjsimplex.noise.gpu.SimplexNoiseGpu4D;
import ee.jjanno.libjsimplex.util.colorizer.Grayscale;

public class Test extends JPanel {

	private static final long serialVersionUID = -4138498611856148174L;

	SimplexNoiseGpu4D n = new SimplexNoiseGpu4D();
	SimplexNoiseGpu3D n3 = new SimplexNoiseGpu3D();
	SimplexNoiseGpu2D n2 = new SimplexNoiseGpu2D();
	Grayscale gs = new Grayscale();

	// Set the number of dimensions calculated. 2,3 and 4 possible.
	int mode = 4;

	float xc = 0;
	float yc = 0;

	float zoom = 0.001f;

	BufferedImage imgB = new BufferedImage(1024, 1024,
			BufferedImage.TYPE_INT_RGB);
	WritableRaster raster = (WritableRaster) imgB.getRaster();

	public Test(String title) {
		super();
		addKeyListener(new Nuputaja(this));
		setFocusable(true);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1024, 1024);
		f.setLocation(100, 100);
		f.setTitle("SimplexTest");
		f.add(new Test(""));
		f.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {

		long a;
		float[] img = null;
		
		switch (mode) {
		case 2:
			a = System.nanoTime();
			img = n2.calculate(15.5f + xc, 5.5f + yc, 1024, 1024,
					zoom);
			System.out.println(System.nanoTime() - a);
			break;
		case 3:
			a = System.nanoTime();
			img = n3.calculate(15.5f + xc, 5.5f + yc, 0f, 1024, 1024,
					1, zoom);
			System.out.println(System.nanoTime() - a);
			break;
		case 4:
			a = System.nanoTime();
			img = n.calculate(15.5f + xc, 5.5f + yc, 0, 0, 1024,
					1024, 1, 1, zoom);
			System.out.println(System.nanoTime() - a);
			break;
		}

		float[] imgF = gs.getGrayscaleRGBArray(img);
		raster.setPixels(0, 0, 1024, 1024, imgF);
		imgB.setData(raster);
		g.drawImage(imgB, 0, 0, null);
	}

	private class Nuputaja extends KeyAdapter {

		private Test m;
		final private float mul = 30.0f;

		public Nuputaja(Test m) {
			this.m = m;
		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_UP) {
				m.yc -= mul * zoom;
			}
			if (key == KeyEvent.VK_DOWN) {
				m.yc += mul * zoom;
			}
			if (key == KeyEvent.VK_LEFT) {
				m.xc -= mul * zoom;
			}
			if (key == KeyEvent.VK_RIGHT) {
				m.xc += mul * zoom;
			}
			if (key == KeyEvent.VK_Z) {
				zoom *= 0.2f;
			}
			if (key == KeyEvent.VK_X) {
				zoom *= 5.0f;
			}

			m.repaint();
		}
	}

}
