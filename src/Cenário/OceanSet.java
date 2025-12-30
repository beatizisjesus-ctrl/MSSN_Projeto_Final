package Cenário;
import java.util.ArrayList;
import java.util.List;

import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;

public class OceanSet {
	private PImage fundo;
	private PImage bolhaImg;
	private List<Bolha> bolhas;
	
	
	public OceanSet(PApplet parent, SubPlot plt) {
        fundo = parent.loadImage("mar.jpg");
        bolhaImg = parent.loadImage("bolha.png");
        bolhas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            bolhas.add(new Bolha(parent, bolhaImg));
        }
    }

	public void display (PApplet parent, SubPlot plt, float dt) {
		parent.imageMode(PApplet.CORNER); 
		// MAR
		 parent.image(fundo, 0, 0, parent.width, parent.height);
		 //B0LHAS
		 for (int i = bolhas.size() - 1; i >= 0; i--) {
		        Bolha b = bolhas.get(i);
		        // Atualiza a posição da bolha (movimento vertical + oscilação lateral)
		        b.update(dt);
		        b.display(parent, plt);

		        if (b.outOfWater()) {
		            bolhas.set(i, new Bolha(parent, bolhaImg));
		        }
		    }
        // AREIA 
        parent.fill(194, 178, 128); 
        float[] pAreia = plt.getBox(-2, -2, 4, 0.5f); //recebe posição e dimensões no mundo, converte para pixels e é feito para desenhar retângulos
        parent.rect(pAreia[0], pAreia[1], pAreia[2], pAreia[3]);
	}

}
