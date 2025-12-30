package Comportamentos;

import processing.core.PVector;

public class Cohesion extends Behaviour{

	public Cohesion(float weight) {
		super(weight);
	}

	//Objetivo é os boids manterem-se em gurpo, um coesão de grupo
	//parecido com o align mas faz a media das posiçoes de todos os boids na visao distante incluinod o proprio boid
	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector target = me.getPos().copy();
		for(Body b : me.eye.getFarSight()) {
			target.add(b.getPos()); //soma das posiçoes de todos os boids
		}
		target.div(me.eye.getFarSight().size() + 1);//media de posições
		return PVector.sub(target, me.getPos()); //vetor deslocamento, a nova posiçao do boid é agora a media
	}
}
