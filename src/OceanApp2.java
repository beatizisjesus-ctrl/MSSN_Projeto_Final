import java.util.ArrayList;
import java.util.List;
import Comportamentos.Body;
import Comportamentos.Boid;
import Comportamentos.Eye;
import Comportamentos.Flock;
import Comportamentos.Pursuit;
import Comportamentos.Shark;
import Comportamentos.Squid;
import Comportamentos.Wander;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class OceanApp2 extends OceanAppBase {

	private Flock flock, smallFlock;
	private Boid shark, target, squid;
	private float[] sacWeights = { 1f, 1f, 1f };
	private PImage squidImage;
	private PImage[] fishImgs2;
	private List<Body> allTrackingBodiesShark, visibleBoids;

	@Override
	protected void setupSpecifics(PApplet parent) {
		// Carregar imagem do Squid
		squidImage = parent.loadImage("squid.png");
		squidImage.resize(70, 0);

		// Inicialização de Flocks
		flock = new Flock(30, 10f, .1f, parent.color(0, 0, 255), sacWeights, parent, plt, fishImgs); // Peixes grandes

		fishImgs2 = new PImage[1];
		fishImgs2[0] = parent.loadImage("data/smallFish.png");
		fishImgs2[0].resize(30, 0);
		smallFlock = new Flock(50, 10f, .1f, parent.color(0, 0, 255), sacWeights, parent, plt, fishImgs2); // Peixes
																											// pequenos

		// Inicialização do Shark
		shark = new Shark(new PVector(), 10f, .15f, parent.color(0255, 0, 0), parent, plt, sharkMouthCloseImg,
				sharkMouthOpenImg);
		shark.addBehaviour(new Pursuit(1f));
		shark.addBehaviour(new Wander(5f));
		shark.setVisionAngle(PApplet.radians(45f));
		shark.dna.visionDistance = 0.8f;
		shark.dna.visionSafeDistance = 0.5f;

		// Inicialização do Squid
		float squidX = parent.random((float) window[0], (float) window[1]);
		float squidY = parent.random((float) window[2], (float) window[3]);
		squid = new Squid(new PVector(squidX, squidY), 10f, .15f, parent.color(0255, 0, 0), parent, plt, squidImage);
		squid.addBehaviour(new Wander(5f));

		// Configuração dos Eyes
		updateBigFishEyes(); // BigFish vê SmallFish e Shark
		updateSharkEye(); // Shark vê BigFish

		for (Boid small : smallFlock.getBoids()) { // SmallFish vê BigFish
			small.setEye(new Eye(small, new ArrayList<Body>(flock.getBoids())));
		}
	}

	@Override
	protected void drawSpecifics(PApplet parent, float dt) {
		// Lógica do Squid
		squid.applyBehaviours(dt);
		squid.display(parent, plt);

		// Lógica de caça do Shark
		int behaviourI = getIndexBehaviour();
		shark.applyBehaviour(behaviourI, dt);
		shark.display(parent, plt);

		// Lógica de Flocks (Peixes Grandes)
		flock.applyBehaviour(dt);
		flock.display(parent, plt);

		// Lógica de caça dos Peixes Grandes aos pequenos
		for (Boid bigFish : flock.getBoids()) {
			List<Body> seen = getVisibleBoids(bigFish);
			if (seen.isEmpty())
				continue;

			Boid smallTarget = (Boid) seen.get(0);
			killBoid(bigFish, smallTarget, smallFlock, 0.2f, 2f, 5);
		}

		// Lógica dos Peixes Pequenos
		smallFlock.applyBehaviour(dt);
		smallFlock.display(parent, plt);
	}

	private void updateSharkEye() {
		// O Tubarão só persegue peixes grandes
		allTrackingBodiesShark = new ArrayList<Body>(flock.getBoids());
		shark.setEye(new Eye(shark, allTrackingBodiesShark));
	}

	private void updateBigFishEyes() {
		// Peixes grandes veem peixes pequenos E o tubarão
		List<Body> bodies = new ArrayList<>(smallFlock.getBoids());
		bodies.add(shark);
		bodies.addAll(flock.getBoids());

		for (Boid b : flock.getBoids()) {
			b.setVisionAngle(PApplet.radians(45f));
			b.dna.visionDistance = 0.4f;
			b.dna.visionSafeDistance = 0.2f;
			b.setEye(new Eye(b, bodies));
		}
	}

	public List<Body> getVisibleBoids(Boid b) {
		b.getEye().look();
		return b.getEye().getFarSight();
	}

	public int getIndexBehaviour() {
		// O Tubarão procura peixes grandes
		visibleBoids = getVisibleBoids(shark);
		int visibleCount = visibleBoids.size();
		int behaviourIndex;

		if (visibleCount > 0 && visibleCount < 2) {
			target = (Boid) visibleBoids.get(0);
			shark.getEye().target = target;
			killBoid(shark, target, flock, 0.3f, 3f, 10);
			behaviourIndex = 0; // Pursuit
		} else {
			behaviourIndex = 1; // Wander
		}
		return behaviourIndex;
	}

	private void killBoid(Boid killer, Boid target, Flock sourceFlock, float captureRadius, float imgScale, int imgFrames) {
		if (!sourceFlock.getBoids().contains(target))
			return;

		float dist = PVector.dist(killer.getPos(), target.getPos());
		if (dist > captureRadius)
			return;

		if (killer instanceof Shark) {
			((Shark) killer).openMouth();
		}
		activeImg.add(new ImageEffect(target.getPos(), target.getRadius() * imgScale, images, imgFrames, killSound));

		sourceFlock.removeBoid(target);

		// Após remover um boid, os olhos de todos devem ser atualizados
		updateBigFishEyes();
		updateSharkEye();
	}

	@Override
	protected void keyPressedSpecifics(PApplet parent) {
		// Adicionar peixes pequenos
		if (parent.key == 's' || parent.key == 'S') {
			float x = parent.random((float) window[0], (float) window[1]);
			float y = parent.random((float) window[2], (float) window[3]);
			smallFlock.addBoid(new PVector(x, y));
			updateBigFishEyes();
		}
	}

	@Override
	public void mousePressed(PApplet parent) {
		// Adicionar peixes grandes
		double[] ww = plt.getWorldCoord(parent.mouseX, parent.mouseY);
		flock.addBoid(new PVector((float) ww[0], (float) ww[1]));

		// Após adicionar, os olhos devem ser atualizados
		updateBigFishEyes();
		updateSharkEye();
	}
}