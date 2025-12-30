package Apps;
import java.util.ArrayList;
import java.util.List;

import Cenário.ImageEffect;
import Comportamentos.Body;
import Comportamentos.Boid;
import Comportamentos.Eye;
import Comportamentos.Flock;
import Comportamentos.Shark;
import processing.core.PApplet;
import processing.core.PVector;


public class OceanApp extends OceanAppBase {
	
	private Flock flock;
	private Boid player, target;
	private float[] sacWeights = { 1f, 1f, 1f };
	private Eye eye;
	private List<Body> allTrackingBodiesFish,allTrackingBodiesShark, visibleBoids;
	private List<Boid> boidsList;
	private boolean wIsOn, aIsOn, sIsOn, dIsOn;
	
	
	@Override
	protected void setupSpecifics(PApplet parent) {
		// Inicialização de Flock e Player (Shark)
		flock = new Flock(50, 10f, .1f, parent.color(0, 0, 255), sacWeights, parent, plt, fishImgs);
		player = new Shark(new PVector(), 10f, .15f, parent.color(0255, 0, 0), parent, plt, sharkMouthCloseImg, sharkMouthOpenImg);
		
		// Configuração dos Eyes para o Flock
		allTrackingBodiesFish = new ArrayList<Body>();
		allTrackingBodiesFish.add(player);
		boidsList = new ArrayList<Boid>(flock.getBoids());
		for(Boid b: boidsList) {
			eye = new Eye(b, allTrackingBodiesFish);
			b.setEye(eye);
		}
		
		// Configuração do Eye para o Player (Shark)
		allTrackingBodiesShark = new ArrayList<Body>(flock.getBoids());
		eye = new Eye(player, allTrackingBodiesShark);
		player.setEye(eye);
		
		wIsOn = false;
		aIsOn = false; 
		sIsOn = false; 
		dIsOn = false;
    }
	
	
	@Override
	protected void drawSpecifics(PApplet parent, float dt) {
		// Lógica de caça
		visibleBoids = getVisibleBoids();
		int visibleCount = visibleBoids.size();
		
		if (visibleCount > 0 && visibleCount < 4) {
			target = (Boid) visibleBoids.get(0);
			player.getEye().target = target;
			kill(target, player);
		} 
		
		playerMove(dt);
		flock.applyBehaviour(dt);
		
		// Desenho
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
				
				ImageEffect image = new ImageEffect(target.getPos(), target.getRadius()*3, images, 10, killSound);
				activeImg.add(image);
				
				flock.removeBoid(target);
				this.allTrackingBodiesFish.remove(target);
				killer.setEye(new Eye(killer, this.allTrackingBodiesShark));
			}
		}
	}
	
	@Override
	protected void keyPressedSpecifics(PApplet parent) {
		// Lógica de movimento do jogador
		if(parent.key == 'w' || parent.key == 'W') {
			wIsOn = true; aIsOn = false; sIsOn = false; dIsOn = false;
		}
		if(parent.key == 'a' || parent.key == 'a') {
			aIsOn = true; wIsOn = false; sIsOn = false; dIsOn = false;
		}
		if(parent.key == 's' || parent.key == 'S') {
			sIsOn = true; wIsOn = false; aIsOn = false; dIsOn = false;
		}
		if(parent.key == 'd' || parent.key == 'D') {
			dIsOn = true; wIsOn = false; aIsOn = false; sIsOn = false; 
		}
	}


	@Override
	public void mousePressed(PApplet parent) {
		double[] ww = plt.getWorldCoord(parent.mouseX, parent.mouseY);
		flock.addBoid(new PVector((float)ww[0],(float) ww[1]));
		
		// Atualizar o Eye do Player para incluir o novo Boid
		allTrackingBodiesShark = new ArrayList<Body>(flock.getBoids());
	    player.setEye(new Eye(player, allTrackingBodiesShark));
	}
}