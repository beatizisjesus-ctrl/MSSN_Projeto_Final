package Comportamentos;
import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Shark extends Boid {
	private PImage imgClosed;
	private PImage imgOpen;

	private boolean mouthOpen = false;
	private float mouthTimer = 0f;
	private float mouthOpenDuration = 0.3f; 

	public Shark(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt, PImage imgClosed,
			PImage imgOpen) {

		super(pos, mass, radius, color, p, plt);
		this.imgClosed = imgClosed;
		this.imgOpen = imgOpen;
	}

	public void openMouth() {
		mouthOpen = true;
		mouthTimer = mouthOpenDuration;
	}

	private void updateMouth(float dt) {
		if (mouthOpen) {
			mouthTimer -= dt;
			if (mouthTimer <= 0) {
				mouthOpen = false;
			}
		}
	}

	@Override
	public void display(PApplet p, SubPlot plt) {

		updateMouth(p.frameRate > 0 ? 1f / p.frameRate : 0.016f);

		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] vv = plt.getVectorCoord(vel.x, vel.y);
		PVector vaux = new PVector(vv[0], vv[1]);

		p.pushMatrix();
		p.translate(pp[0], pp[1]);
		p.rotate(-vaux.heading());

		p.imageMode(PApplet.CENTER);

		if (mouthOpen) {
			p.image(imgOpen, 0, 0);
		} else {
			p.image(imgClosed, 0, 0);
		}

		p.popMatrix();
	}
}
