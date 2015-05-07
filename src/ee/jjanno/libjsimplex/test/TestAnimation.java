package ee.jjanno.libjsimplex.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ee.jjanno.libjsimplex.generator.NoiseSurface;
import ee.jjanno.libjsimplex.util.colorizer.ColorMapper;
import ee.jjanno.libjsimplex.util.colorizer.Grayscale;

public class TestAnimation extends JPanel {

	private static final long serialVersionUID = -4138498611856148174L;

	NoiseSurface noise = new NoiseSurface();
	ColorMapper m = new ColorMapper();
	Grayscale g = new Grayscale();

	float xc = 0;
	float yc = 0;
	float zc = 0;
	float wc = 0;

	static int n = 90;
	static int fps = 30;
	static int xRes = 1024;
	static int yRes = 1024;

	static float zoom = 0.006f;

	public BufferedImage[] images = new BufferedImage[n];
	BufferedImage image = null;

	public TestAnimation(String title) {
		m.addRange(0.5f, 1f, new Color(100, 100, 0), new Color(255, 255, 255),
				0.7);
		m.addRange(0.3f, 0.5f, new Color(0, 100, 0), new Color(100, 100, 0),
				0.8);
		m.addRange(0.15f, 0.3f, new Color(195, 195, 0), new Color(0, 100, 0),
				0.8);
		m.addRange(0, 0.15f, new Color(0, 0, 155), new Color(195, 195, 0), 1);
		m.addRange(-1, 0, new Color(0, 0, 0), new Color(0, 0, 155), 3);

		for (int i = 0; i < n; i++) {
			final float z = (float) Math.sin(Math.PI * 2.0 * ((double) i / n));
			final float w = (float) Math.cos(Math.PI * 2.0 * ((double) i / n));
			final int index = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					addImage(w, z, index);
				}
			}).start();
		}
	}

	void addImage(float w, float z, int index) {
		float[] img = NoiseSurface.generate2dRawFrom4dOctaved(0, 0, z * 0.05f, w * 0.05f, xRes, yRes, zoom, 0.6, 4, true);
		images[index] = m.getBufferedImage(img, xRes, yRes);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1024, 1024);
		f.setLocation(100, 100);
		f.setTitle("SimplexTest");
		TestAnimation t = new TestAnimation("");
		f.add(t);
		f.setVisible(true);

		while (true) {
			f.repaint();
			try {
				Thread.sleep(500 / fps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		int a = (int) ((double) System.nanoTime() / 1000000000 * fps);
		if (images[a % n] != null) {
			image = images[a % n];
			g.drawImage(images[a % n], 0, 0, 1024, 1024, null);
		} else
			g.drawImage(image, 0, 0, 1024, 1024, null);

	}

}
