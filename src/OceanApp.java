import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
import processing.core.PVector;


public class OceanApp implements IProcessingApp {
	
	private double [] window = {-2, 2, -2, 2}; //espaço físico(tela pc) onde o programa aparece (xmin, xmax, ymin, ymax), mundo
	private float[] viewport = {0,0 , 1,1}; //é uma área dentro da janela onde realmente se desenha ou visualiza a cena. (x,y em que começa neste caso 0,0, e 1,1,ocupa 100% da janela)
	private SubPlot plt;
	private OceanSet oceanSet;
	private List<Alga> algas; //lista de algas
	
	
	
	public void setup(PApplet parent) {
		plt = new SubPlot (window, viewport, parent.width, parent.height); 
		oceanSet = new OceanSet(parent, plt);
		algas = new ArrayList<>();
    }
	
	
	public void draw(PApplet parent, float dt) {
		oceanSet.display(parent, plt, dt);
		for(Alga a : algas) {// a cada alga criada e introduzida na lista chama-se a funçao de crescer e mostrar a mesma atraves da turtle
            a.grow(dt);
            a.display(parent, plt);
        }
	}
	
	public void keyPressed(PApplet parent) {
		if (parent.key == 'a' || parent.key == 'A') {

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
	}


	@Override
	public void mousePressed(PApplet parent) {
		// TODO Auto-generated method stub
		
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
