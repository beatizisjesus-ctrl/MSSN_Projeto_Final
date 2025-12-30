package Comportamentos;
import processing.core.PVector;

public class Wander extends Behaviour{

	public Wander(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector center = me.getPos().copy(); // ponto atual
		center.add(me.getVel().copy().mult(me.dna.deltaTWander)); //projeção do ponto, tornando-se  o centro do círculo, o ponto atual na vai para la, o centro serve apenas para encontrar o target
		PVector target = new PVector(me.dna.radiusWander * (float)Math.cos(me.phiWander), 
				me.dna.radiusWander * (float)Math.sin(me.phiWander));//cria-se um novo ponto (target)dentro da area dada pelo angulo
		target.add(center); //o target passa de deslocamento relativo para posição absoluta
		me.phiWander += 2*(Math.random()-0.5) * me.dna.deltaPhiWander; //determina o intervlalo em que o angulo pode variar
		return PVector.sub(target, me.getPos()); //indica para onde deve acelerar, mover-se, é o vetor de deslocamento
	}
}
