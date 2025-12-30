package Apps;
import java.util.ArrayList;
import java.util.List;

import Cenário.Alga;
import Cenário.ImageEffect;
import Cenário.OceanSet;
import Cenário.Rule_LSyst;
import globais.IProcessingApp;
import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.*;


public abstract class OceanAppBase implements IProcessingApp {
	
	protected double [] window = {-2, 2, -2, 2}; //espaço físico(tela pc)
	protected float[] viewport = {0,0 , 1,1}; //área de desenho
	protected SubPlot plt;
	protected OceanSet oceanSet;
	protected List<Alga> algas; //lista de algas
	protected SoundFile sound, killSound; // efeitos sonoros
	protected PImage sharkMouthOpenImg, sharkMouthCloseImg; // Imagens do tubarão
	protected PImage[] fishImgs; // Imagens dos peixes grandes 
	protected PImage[] images; // Imagens do efeito de sangue
	protected List<ImageEffect> activeImg, imgToRemove;
	
	
	public void setup(PApplet parent) {
		plt = new SubPlot (window, viewport, parent.width, parent.height); 
		oceanSet = new OceanSet(parent, plt);
		algas = new ArrayList<>();
		activeImg = new ArrayList<>();
		
		// Carregar imagens e sons comuns
		sound = new SoundFile(parent, "data/Jaws-theme-song.wav");
		sound.loop(); 
		killSound = new SoundFile(parent, "data/smoke-bomb-6761.wav");
		
		sharkMouthCloseImg = parent.loadImage("sharkMouthClose.png");
		sharkMouthOpenImg = parent.loadImage("sharkMouthOpen.png");
		sharkMouthCloseImg.resize(230, 0); 
		sharkMouthOpenImg.resize(230, 0);
		
		fishImgs = new PImage[3];
		images = new PImage[3];
		for (int i = 0; i < 3; i++) {
			int fileIndex = i + 1;
			fishImgs[i] = parent.loadImage("fish" + fileIndex + ".png");
			fishImgs[i].resize(60, 0); 
			images[i] = parent.loadImage("sangue" + fileIndex + ".png");
			images[i].resize(60, 0);
		}

        // Lógica de setup específica da subclasse
		setupSpecifics(parent);
    }
	
	// Método a ser implementado por cada subclasse para inicializar o seu mundo específico
	protected abstract void setupSpecifics(PApplet parent);
	
	
	public void draw(PApplet parent, float dt) {
		// Desenho Comum: Cenário, Algas e Bounding Box
		oceanSet.display(parent, plt, dt);
		for(Alga a : algas) {
            a.grow(dt);
            a.display(parent, plt);
        }
		
		float[] bb = plt.getBoundingBox();
		parent.fill(255, 84);
		parent.rect(bb[0], bb[1], bb[2], bb[3]);
		
		// Desenho e lógica Específicos da subclasse
		drawSpecifics(parent, dt);
		
		// Desenho Comum: Efeitos de Imagem (Sangue)
		imgToRemove = new ArrayList<>();
		for (ImageEffect image : activeImg) {
			image.display(parent, plt);
			if (image.isFinished()) {
				imgToRemove.add(image);
			}
		}
		activeImg.removeAll(imgToRemove);
	}
	
	// Método a ser implementado por cada subclasse para a sua lógica de draw
	protected abstract void drawSpecifics(PApplet parent, float dt);
	
	
	public void keyPressed(PApplet parent) {
		// Lógica Comum: Adicionar Algas ('m' ou 'M')
		if (parent.key == 'm' || parent.key == 'M') {

	        float x = parent.random(-2f, 2f); // X aleatório ao longo da areia
	        float y = -1.75f; // Y fixo na areia

	        PVector pos = new PVector(x, y);

	        Rule_LSyst[] ruleset = new Rule_LSyst[1];
	        ruleset[0] = new Rule_LSyst('F', "F[+F]F[-F]F");
	        Alga alga = new Alga("F", ruleset, pos, 0.05f, PApplet.radians(25f), 4, 0.5f, 1f, parent);

	        algas.add(alga);
	    }
		
		// Lógica de keyPressed específica da subclasse
		keyPressedSpecifics(parent);
	}
	
	protected void keyPressedSpecifics(PApplet parent) {
		// Implementação opcional na subclasse
	}

	@Override
	public void mousePressed(PApplet parent) {
		// Implementação na subclasse
	}


	@Override
	public void mouseReleased(PApplet parent) {
		// Implementação na subclasse
	}


	@Override
	public void mouseDragged(PApplet parent) {
		// Implementação na subclasse
	}
}