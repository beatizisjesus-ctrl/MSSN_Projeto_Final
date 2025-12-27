package Comportamentos;
import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Fish extends Boid {

	private PImage img;

	public Fish(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt, PImage[] fishImages) {

		super(pos, mass, radius, color, p, plt);
		int idx = (int) p.random(fishImages.length);
		this.img = fishImages[idx];
	}

	@Override
	public void display(PApplet p, SubPlot plt) {

		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] vv = plt.getVectorCoord(vel.x, vel.y);
		PVector vaux = new PVector(vv[0], vv[1]);

		p.pushMatrix();
		p.translate(pp[0], pp[1]);
		p.rotate(-vaux.heading());

		p.imageMode(PApplet.CENTER);
		p.image(img, 0, 0);

		p.popMatrix();
	}
}
