package Comportamentos;
import processing.core.PVector;

public class Pursuit extends Behaviour{

	public Pursuit(float weight) {
		super(weight);
	}

	//o pursuit antecipa para onde é qu eo target vai
	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.eye.target;
		PVector d = bodyTarget.getVel().mult(me.dna.deltaTPursuit);// vamos buscar a velocidade do target* variação de tempo escolhida
		PVector target = PVector.add(bodyTarget.getPos(), d); // target passa a ser o proximo sitio onde ele vai
		return PVector.sub(target, me.getPos()); //vetor de deslocamento
	}

}