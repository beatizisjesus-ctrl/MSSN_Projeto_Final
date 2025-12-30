package Comportamentos;

public class DNA {
	public float maxSpeed;
	public float maxForce;
	public float visionDistance;
	public float visionSafeDistance;
	public float visionAngle;
	public float deltaTPursuit;
	public float radiusArrive;
	public float deltaTWander;
	public float radiusWander;
	public float deltaPhiWander;
	
	public DNA() {
		//Physics
		maxSpeed = random(1f, 3f); 
		maxForce = random(4f, 7f);
		//Vision
		visionDistance = random(1.5f, 2.5f); 
		visionSafeDistance = 0.25f * visionDistance;
		visionAngle = (float)Math.PI * 0.3f; 
		//Pursuit
		deltaTPursuit = random(0.5f,1f);// variaçao de tempo escolhida para fazer pursuit
		//Arrive
		radiusArrive = random(3,5);
		//Wander
		deltaTWander = random(.3f, .6f); //projeçao do ponto, no fram seguinte
		radiusWander = random(1f,3f); // raio do circulo feito, no ponto projetado
		deltaPhiWander = (float)Math.PI/8; //angulo feito com o eixo x, é na area dada por este angulo que se vai obter um ponto aleatorimanete
	}
	
	public DNA(DNA dna, boolean mutate) {
		maxSpeed = dna.maxSpeed;
		maxForce = dna.maxForce;
		
		visionDistance = dna.visionDistance;
		visionSafeDistance = dna.visionSafeDistance;
		visionAngle = dna.radiusArrive;
		
		deltaTPursuit = dna.deltaTPursuit;
		
		deltaTWander = dna.deltaTWander;
		radiusWander = dna.radiusWander;
		deltaPhiWander = dna.deltaPhiWander;
		
		if(mutate) mutate();
	}
	
	
	private void mutate() {
		maxSpeed += random(-0.2f, 0.2f);
		maxSpeed = Math.max(0, maxSpeed);
	}
	
	public static float random(float min, float max) {
		return(float)(min + (max-min)*Math.random());
	}
}
