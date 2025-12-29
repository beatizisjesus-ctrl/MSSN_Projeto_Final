package Comportamentos;
import java.util.ArrayList;
import java.util.List;

import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Boid extends Body {

	private PShape shape;
	public DNA dna;
	public Eye eye;
	private List<Behaviour> behaviours;
	public float phiWander;
	private double[] window;
	public int index;
	private float sumWeights;

	public Boid(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, new PVector(), mass, radius, color);
		setShape(p, plt);
		dna = new DNA();
		behaviours = new ArrayList<Behaviour>();
		window = plt.getWindow();
	}

	//primeiro temos de ter todos os boids e só depois é que implementamos o olho
	public void setEye(Eye eye) {
		this.eye = eye;
	}
	
	public Eye getEye() {
		return this.eye;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	


	public void setVisionAngle(float angle) {
		dna.visionAngle = angle;
	}
	
	public void setShape(PApplet p, SubPlot plt, float radius, int color) {
		this.radius = radius;
		this.color = color;
		setShape(p,plt);
	}

	public void setShape(PApplet p, SubPlot plt) {
		float[] rr = plt.getVectorCoord(radius, radius);
		shape = p.createShape();
		shape.beginShape(); //antes de criar a forma
		shape.noStroke();
		shape.fill(color);
		shape.vertex(-rr[0], rr[0] / 2);
		shape.vertex(rr[0], 0);
		shape.vertex(-rr[0], -rr[0] / 2);
		shape.vertex(-rr[0] / 2, 0);
		shape.endShape(PConstants.CLOSE);
	}
	
	private void updateSumWeights() {
		sumWeights = 0;
		for(Behaviour beh : behaviours) {
			sumWeights += beh.getWeight();
		}
	}

	public void addBehaviour(Behaviour behaviour) {
		behaviours.add(behaviour);
		updateSumWeights();
	}

	public void removeBehaviour(Behaviour behaviour) {
		if (behaviours.contains(behaviour)) {
			behaviours.remove(behaviour);
		}
		updateSumWeights();
	}

	public void applyBehaviour(int i, float dt) {
		this.index = i;
		if(eye!= null) eye.look();
		Behaviour behaviour = behaviours.get(this.index);
		PVector vd = behaviour.getDesiredVelocity(this);
		move(dt, vd);
	}

	public void applyBehaviours(float dt) {
		if(eye!= null) eye.look();
		PVector vd = new PVector();
		for (Behaviour behaviour : behaviours) {
			PVector vdd = behaviour.getDesiredVelocity(this);
			vdd.mult(behaviour.getWeight()/sumWeights);
			vd.add(vdd);
		}
		move(dt, vd);
	}

	public void move(float dt, PVector vd) {
		
		vd.normalize().mult(dna.maxSpeed);
		PVector fs = PVector.sub(vd, vel);
		applyForce(fs.limit(dna.maxForce));
		super.move(dt);

		if (pos.x < window[0]) {
			pos.x += window[1] - window[0];
		}
		if (pos.y < window[2]) {
			pos.y += window[3] - window[2];
		}
		if (pos.x >= window[1]) {
			pos.x -= window[1] - window[0];
		}
		if (pos.y >= window[3]) {
			pos.y -= window[3] - window[2];
		}
		
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushMatrix();
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] vv = plt.getVectorCoord(vel.x, vel.y);
		PVector vaux = new PVector(vv[0], vv[1]);
		p.translate(pp[0], pp[1]); //coloca os sistema de eixos na posição do boid
		p.rotate(-vaux.heading()); //orientação do boid, heading da o angulo em radianos, que o vetor faz com o eixos do x
		p.shape(shape);//por omissao, no ponto (0,0)
		p.popMatrix();
	}

}
