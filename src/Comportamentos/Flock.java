package Comportamentos;
import java.util.ArrayList;

import java.util.List;

import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PImage;


public class Flock {

	private List<Boid> boids;
	private Boid player;
	private float mass;
	private float radius;
	private int color;
	private float[] sacWeights;
	private PApplet p;
	private SubPlot plt;
	private PImage[] fishImgs;

	
	public Flock(int nboids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt, PImage[] fishImgs) {
		this.mass = mass;
		this.radius = radius;
		this.color = color;
		this.sacWeights = sacWeights;
		this.p = p;
		this.plt = plt;
		this.fishImgs = fishImgs;
		
		boids = new ArrayList<Boid>();
		double[] w = plt.getWindow();
		for(int i=0; i<nboids; i++) {
			float x = p.random((float)w[0],(float) w[1]);
			float y = p.random((float)w[2],(float) w[3]);
			Boid b = new Fish(new PVector(x,y), mass, radius, color, p, plt, fishImgs);
			b.addBehaviour(new Separate(sacWeights[0]));
			b.addBehaviour(new Align(sacWeights[1]));
			b.addBehaviour(new Cohesion(sacWeights[2]));
			boids.add(b);
		}
		
		setEye();
	}
	
	private void setEye() {
		for(Boid b : boids) {
			b.setEye(new Eye(b, boidList2BodyList(boids)));
		}
	}
	
	public void setEye(Boid target) {
		this.player = target;
		List<Body> allTrackingBodies = new ArrayList<Body>();
		allTrackingBodies.add(target);
		for(Boid b: boids) {
			b.setEye(new Eye(b, allTrackingBodies));
		}
	}
	
	private List<Body> boidList2BodyList(List<Boid> boids){
		List<Body> bodies = new ArrayList<Body>();
		for(Boid b : boids) {
			bodies.add(b);
		}
		return bodies;
	}
	
	public void applyBehaviour(float dt) {
		for(Boid b : boids) {
			if (b == player) continue; 
			b.applyBehaviours(dt);
		}
	}
	
	public Boid getBoid(int i) {
		return boids.get(i);
	}
	
	public List<Boid> getBoids(){
		return boids;
	}
	
	public void display(PApplet p, SubPlot plt) {
		for(Boid b : boids) {
			b.display(p, plt);
		}
	}

	public void removeBoid(Boid removedBoid) {
		boids.remove(removedBoid);
		List<Body> newTrackingList = boidList2BodyList(boids); 
	    for(Boid b : boids) {
	        b.setEye(new Eye(b, newTrackingList)); 
	    }
	}
	
	public void addBoid(PVector pos) {
		Boid b = new Fish(pos, mass, radius, color, p, plt, fishImgs);
		b.addBehaviour(new Separate(sacWeights[0]));
		b.addBehaviour(new Align(sacWeights[1]));
		b.addBehaviour(new Cohesion(sacWeights[2]));
		boids.add(b);
		setEye();
	}
}
