package Comportamentos;

import processing.core.PVector;

public class Separate extends Behaviour{

	public Separate(float weight) {
		super(weight);
	}

	//Objetivo é separar dos boids próximos para não chocar
	
	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector vd = new PVector();
		for (Body b : me.eye.getNearSight()) {
			PVector r = PVector.sub(me.getPos(), b.getPos()); //para todos os boids perto, o boid vai para os sentdo contrario
			float d = r.mag(); //calcula-se o modulo desse vetor
			r.div(d*d); // o vetor tem que ser proporcinalmente inverso à distanica, ao dividirmos o vetor1 vez pela distanica, o modulo fica 1, por isso dividimos 2 vezes, em vez de dividir o vetor 2 vezes pelo modulo, faz-se isto(/d*d) que é igual
			vd.add(r);//e isso é o novo vetor com a velcidade desejada
		}
		return vd;
	}

}
