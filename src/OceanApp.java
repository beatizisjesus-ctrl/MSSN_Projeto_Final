import java.util.ArrayList;
import java.util.List;

import Comportamentos.Body;
import Comportamentos.Boid;
import Comportamentos.Eye;
import Comportamentos.Flock;
import Comportamentos.Shark;
import globais.IProcessingApp;
import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;


public class OceanApp implements IProcessingApp {
	
	private double [] window = {-2, 2, -2, 2}; //espaço físico(tela pc) onde o programa aparece (xmin, xmax, ymin, ymax), mundo
	private float[] viewport = {0,0 , 1,1}; //é uma área dentro da janela onde realmente se desenha ou visualiza a cena. (x,y em que começa neste caso 0,0, e 1,1,ocupa 100% da janela)
	private SubPlot plt;
	private OceanSet oceanSet;
	private List<Alga> algas; //lista de algas
	private Flock flock;
	private Boid player, target;
	private float[] sacWeights = { 1f, 1f, 1f };
	private Eye eye;
	private List<Body> allTrackingBodiesFish,allTrackingBodiesShark, visibleBoids;
	private List<Boid> boidsList;
	private boolean wIsOn, aIsOn, sIsOn, dIsOn;
	private PImage sharkMouthOpenImg, sharkMouthCloseImg;
	private PImage[] fishImgs;
	
	
	
	public void setup(PApplet parent) {
		plt = new SubPlot (window, viewport, parent.width, parent.height); 
		oceanSet = new OceanSet(parent, plt);
		algas = new ArrayList<>();
		
		// Carregar imagens
		sharkMouthCloseImg = parent.loadImage("sharkMouthClose.png");
		sharkMouthOpenImg = parent.loadImage("sharkMouthOpen.png");
		sharkMouthCloseImg.resize(230, 0); 
		sharkMouthOpenImg.resize(230, 0);
		fishImgs = new PImage[3];
		fishImgs[0] = parent.loadImage("fish1.png");
		fishImgs[1] = parent.loadImage("fish2.png");
		fishImgs[2] = parent.loadImage("fish3.png");

		for (PImage img : fishImgs) {
		    img.resize(60, 0); 
		}

		
		flock = new Flock(20, 10f, .1f, parent.color(0, 0, 255), sacWeights, parent, plt, fishImgs);
		player = new Shark(new PVector(), 10f, .15f, parent.color(0255, 0, 0), parent, plt, sharkMouthCloseImg, sharkMouthOpenImg);
		allTrackingBodiesFish = new ArrayList<Body>();
		allTrackingBodiesFish.add(player);
		boidsList = new ArrayList<Boid>(flock.getBoids());
		for(Boid b: boidsList) {
			eye = new Eye(b, allTrackingBodiesFish);
			b.setEye(eye);
		}
		
		allTrackingBodiesShark = new ArrayList<Body>(flock.getBoids());
		eye = new Eye(player, allTrackingBodiesShark);
		player.setEye(eye);
		
		wIsOn = false;
		aIsOn = false; 
		sIsOn = false; 
		dIsOn = false;
    }
	
	
	public void draw(PApplet parent, float dt) {
		oceanSet.display(parent, plt, dt);
		for(Alga a : algas) {// a cada alga criada e introduzida na lista chama-se a funçao de crescer e mostrar a mesma atraves da turtle
            a.grow(dt);
            a.display(parent, plt);
        }
		
		visibleBoids = getVisibleBoids();
		int visibleCount = visibleBoids.size();
		
		if (visibleCount == 1 || visibleCount == 2) {
			target = (Boid) visibleBoids.get(0);
			player.getEye().target = target;
			kill(target, player);
		} 
		
		playerMove(dt);
		float[] bb = plt.getBoundingBox();
		parent.fill(255, 84);
		parent.rect(bb[0], bb[1], bb[2], bb[3]);
		flock.applyBehaviour(dt);
		flock.display(parent, plt);
		player.display(parent, plt);
	}
	
	private void playerMove(float dt) {
		PVector desiredVelocity = new PVector(0,0);
		if(wIsOn) {
			desiredVelocity.add(0, player.dna.maxSpeed);
		}
		if(dIsOn) {
			desiredVelocity.add(player.dna.maxSpeed, 0);
		}
		if(aIsOn) {
			desiredVelocity.add(-player.dna.maxSpeed, 0);
		}
		if(sIsOn) {
			desiredVelocity.add(0, -player.dna.maxSpeed);
		}
		
		player.move(dt, desiredVelocity);
	}
	
	public List<Body> getVisibleBoids() {
		player.getEye().look();
		return player.getEye().getFarSight();
	}
	
	public void kill(Boid target, Boid killer) {
		float distance = PVector.dist(target.getPos(), killer.getPos());
		float captureRadius = 0.4f;

		if (distance < captureRadius) {
			if (flock.getBoids().contains(target)) {
				if (player instanceof Shark) {
				    ((Shark) player).openMouth();
				}
				flock.removeBoid(target);
				this.allTrackingBodiesFish.remove(target);
				killer.setEye(new Eye(killer, this.allTrackingBodiesShark));
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
		
		if(parent.key == 'w' || parent.key == 'W') {
			wIsOn = true;
			aIsOn = false; 
			sIsOn = false; 
			dIsOn = false;
		}
		if(parent.key == 'a' || parent.key == 'a') {
			aIsOn = true;
			wIsOn = false;
			sIsOn = false; 
			dIsOn = false;
		}
		if(parent.key == 's' || parent.key == 'S') {
			sIsOn = true;
			wIsOn = false;
			aIsOn = false;  
			dIsOn = false;
		}
		if(parent.key == 'd' || parent.key == 'D') {
			dIsOn = true;
			wIsOn = false;
			aIsOn = false; 
			sIsOn = false; 
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
