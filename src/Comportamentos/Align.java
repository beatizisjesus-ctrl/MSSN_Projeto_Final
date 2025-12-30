package Comportamentos;

import processing.core.PVector;

public class Align extends Behaviour{

	public Align(float weight) {
		super(weight);
	}

	//Imita os outros boids para ter uma velocidade parecida
	//A velociadade do boid, vai ser uma media de todas as velocidades dos boids que estao na visao distante, incluido a velocidade do meu boid
	
	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector vd = me.getVel().copy();
		for(Body b : me.eye.getFarSight()) {
			vd.add(b.getVel());//soma-se todas as velocidades
		}
		return vd.div(me.eye.getFarSight().size() + 1);//+1 pq tbm usei a minha velovidade ( do boid), mas nao estou na lista do farSight
	}

}
