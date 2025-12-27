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
import globais.IProcessingApp;
import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.*;


public class OceanApp2 implements IProcessingApp {
	
	private double [] window = {-2, 2, -2, 2}; //espaço físico(tela pc) onde o programa aparece (xmin, xmax, ymin, ymax), mundo
	private float[] viewport = {0,0 , 1,1}; //é uma área dentro da janela onde realmente se desenha ou visualiza a cena. (x,y em que começa neste caso 0,0, e 1,1,ocupa 100% da janela)
	private SubPlot plt;
	private OceanSet oceanSet;
	private List<Alga> algas; //lista de algas
	private Flock flock, smallFlock;
	private Boid shark, target, squid;
	private float[] sacWeights = { 1f, 1f, 1f };
	private Eye eye;
	private List<Body> allTrackingBodiesFish,allTrackingBodiesShark, visibleBoids;
	private List<Boid> boidsList, boidsList2;
	private PImage sharkMouthOpenImg, sharkMouthCloseImg, squidImage;
	private PImage[] fishImgs, fishImgs2;
	private SoundFile sound, killSound;
	private List<ImageEffect> activeImg, imgToRemove;
	private ImageEffect image;
	private PImage[] images;
	
	
	
	public void setup(PApplet parent) {
		plt = new SubPlot (window, viewport, parent.width, parent.height); 
		oceanSet = new OceanSet(parent, plt);
		algas = new ArrayList<>();
		
		// Carregar imagens e sons
		sound = new SoundFile(parent, "data/Jaws-theme-song.wav");
		sound.loop(); 
		killSound = new SoundFile(parent, "data/smoke-bomb-6761.wav");
		sharkMouthCloseImg = parent.loadImage("sharkMouthClose.png");
		sharkMouthOpenImg = parent.loadImage("sharkMouthOpen.png");
		sharkMouthCloseImg.resize(230, 0); 
		sharkMouthOpenImg.resize(230, 0);
		squidImage = parent.loadImage("squid.png");
		squidImage.resize(60, 0);
		
		fishImgs = new PImage[3];
		activeImg = new ArrayList<>();
		images = new PImage[3];
		for (int i = 0; i < 3; i++) {
			int fileIndex = i + 1;
			fishImgs[i] = parent.loadImage("fish" + fileIndex + ".png");
			fishImgs[i].resize(60, 0); 
			images[i] = parent.loadImage("sangue" + fileIndex + ".png");
			images[i].resize(60, 0);
		}

		flock = new Flock(20, 10f, .1f, parent.color(0, 0, 255), sacWeights, parent, plt, fishImgs);
		shark = new Shark(new PVector(), 10f, .15f, parent.color(0255, 0, 0), parent, plt, sharkMouthCloseImg, sharkMouthOpenImg);
		shark.addBehaviour(new Pursuit(1f));
		shark.addBehaviour(new Wander(5f));
		
		shark.setVisionAngle(PApplet.radians(45f)); 
		shark.dna.visionDistance = 0.8f;
		shark.dna.visionSafeDistance = 0.5f;
		
		fishImgs2 = new PImage[1];
		fishImgs2[0] = parent.loadImage("data/smallFish.png");
		fishImgs2[0].resize(30, 0); 
		smallFlock = new Flock(20, 10f, .1f, parent.color(0, 0, 255), sacWeights, parent, plt, fishImgs2);
		
		allTrackingBodiesFish = new ArrayList<Body>();
		allTrackingBodiesFish.add(shark);
		
		boidsList2 = new ArrayList<Boid>(smallFlock.getBoids());
		for(Boid fish: boidsList2) {
			allTrackingBodiesFish.add(fish);
		}
		
		boidsList = new ArrayList<Boid>(flock.getBoids());
		for(Boid b: boidsList) {
			eye = new Eye(b, allTrackingBodiesFish);
			b.setEye(eye);
		}
		
		allTrackingBodiesShark = new ArrayList<Body>(flock.getBoids());
		eye = new Eye(shark, allTrackingBodiesShark);
		shark.setEye(eye);
		
		float squidX = parent.random((float)window[0], (float)window[1]);
		float squidY = parent.random((float)window[2], (float)window[3]);

		squid = new Squid(new PVector(squidX, squidY), 10f, .15f, parent.color(0255, 0, 0), parent, plt, squidImage);
		squid.addBehaviour(new Wander(5f));
		
    }
	
	
	public void draw(PApplet parent, float dt) {
		oceanSet.display(parent, plt, dt);
		for(Alga a : algas) {// a cada alga criada e introduzida na lista chama-se a funçao de crescer e mostrar a mesma atraves da turtle
            a.grow(dt);
            a.display(parent, plt);
        }
		
		squid.applyBehaviours(dt);
		squid.display(parent, plt);
		
		int behaviourI = getIndexBehaviour();
		
		float[] bb = plt.getBoundingBox();
		parent.fill(255, 84);
		parent.rect(bb[0], bb[1], bb[2], bb[3]);
		
		flock.applyBehaviour(dt);
		flock.display(parent, plt);
		
		for (Boid bigFish : flock.getBoids()) {
			Boid nearestPrey = null;
		    float minDist = Float.MAX_VALUE;
		    for (Boid small : smallFlock.getBoids()) {
		        float d = PVector.dist(bigFish.getPos(), small.getPos());
		        if (d < minDist) {
		            minDist = d;
		            nearestPrey = small;
		        }
		    }
		    // Aplicar comportamento de perseguição
		    if (nearestPrey != null) {
		    	List<Body> aux = new ArrayList<Body>();
				aux.add(nearestPrey);
		    	Eye eye = new Eye(bigFish, aux);
		    	bigFish.setEye(eye);
		        bigFish.addBehaviour(new Pursuit(1f)); // 1f = intensidade
		    }
		}
		
		smallFlock.applyBehaviour(dt);
		smallFlock.display(parent, plt);
		
		shark.applyBehaviour(behaviourI, dt);
		shark.display(parent, plt);
		
		imgToRemove = new ArrayList<>();
		for (ImageEffect image : activeImg) {
			image.display(parent, plt);
			if (image.isFinished()) {
				imgToRemove.add(image);
			}
		}
		activeImg.removeAll(imgToRemove);
	}
	
	public List<Body> getVisibleBoids() {
		shark.getEye().look();
		return shark.getEye().getFarSight();
	}
	
	public int getIndexBehaviour() {
		visibleBoids = getVisibleBoids();
		int visibleCount = visibleBoids.size();
		int behaviourIndex;

		if (visibleCount > 0 && visibleCount < 2) {
			target = (Boid) visibleBoids.get(0);
			shark.getEye().target = target;
			kill(target, shark);
			behaviourIndex = 0;
		} else {
			behaviourIndex = 1;
		}
		return behaviourIndex;
	}
	
	public void kill(Boid target, Boid killer) {
		float distance = PVector.dist(target.getPos(), killer.getPos());
		float captureRadius = 0.3f;

		if (distance < captureRadius) {
			if (flock.getBoids().contains(target)) {
				if (shark instanceof Shark) {
				    ((Shark) shark).openMouth();
				}
				
				image = new ImageEffect(target.getPos(), target.getRadius()*3, images, 10, killSound);
				activeImg.add(image);
				flock.removeBoid(target);
				this.allTrackingBodiesFish.remove(target);
				killer.setEye(new Eye(killer, this.allTrackingBodiesShark));
			}
		}
	}
	
	public void killSmallFish(Boid predator, Flock preyFlock) {
	    List<Boid> preyList = new ArrayList<>(preyFlock.getBoids());
	    for (Boid prey : preyList) {
	        float distance = PVector.dist(prey.getPos(), predator.getPos());
	        float captureRadius = 0.2f; // menor que para o shark
	        if (distance < captureRadius) {
	            ImageEffect img = new ImageEffect(prey.getPos(), prey.getRadius()*2, images, 5, killSound);
	            activeImg.add(img);
	            preyFlock.removeBoid(prey);
	            allTrackingBodiesFish.remove(prey);
	        }
	    }
	}

	
	public void keyPressed(PApplet parent) {
		if (parent.key == 'm' || parent.key == 'M') {

	        // X aleatório ao longo da areia
	        float x = parent.random(-2f, 2f); //do min ao max do retangulo que representa areia

	        // Y fixo na areia
	        float y = -1.75f;

	        PVector pos = new PVector(x, y);

	        Rule_LSyst[] ruleset = new Rule_LSyst[1];
	        ruleset[0] = new Rule_LSyst('F', "F[+F]F[-F]F");
	        Alga alga = new Alga("F", ruleset, pos, 0.05f, PApplet.radians(25f), 4, 0.5f, 1f, parent);;

	        algas.add(alga);
	    }
	}


	@Override
	public void mousePressed(PApplet parent) {
		double[] ww = plt.getWorldCoord(parent.mouseX, parent.mouseY);
		flock.addBoid(new PVector((float)ww[0],(float) ww[1]));
	}


	@Override
	public void mouseReleased(PApplet parent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(PApplet parent) {
		// TODO Auto-generated method stub
		
	}
}
