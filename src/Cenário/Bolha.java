package Cenário;
import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Bolha {

    private PVector pos;
    private float speed;
    private float faseOscilacao;
    private float AmpOscilacao; 
    private PImage img;

   

    public Bolha(PApplet parent, PImage bolhaImg) {
    	
    	this.img = bolhaImg;
        float x = parent.random(-2f, 2f); // Posição inicial aleatória ao longo do fundo do mar
        float y = parent.random(-2f, -1.5f); // Começa sempre debaixo de água (perto da areia)

        pos = new PVector(x, y); // Posição da bolha no mundo (coordenadas do SubPlot)

        speed = parent.random(0.2f, 0.5f); // Velocidade vertical (quanto mais alto, mais rápido sobe)
        faseOscilacao = parent.random(PApplet.TWO_PI); // Fase da oscilação lateral (cada bolha começa num sitio diferente da onda sinusoidal horizzontal, tem fases diferentes)
        AmpOscilacao = parent.random(0.05f, 0.15f); // Amplitude da oscilação lateral (quanto a bolha se desvia para os lados)
	}

	public void update(float dt) {
        pos.y += speed * dt;           // sobe
        faseOscilacao += dt;               // oscila, acrescenta à fase o tempo que passou desde o ultimo frame
        // Movimento lateral suave (simula corrente da água)
        pos.x += PApplet.sin(faseOscilacao) * AmpOscilacao * dt;
    }

    public void display(PApplet p, SubPlot plt) {
        float[] pix = plt.getPixelCoord(pos.x, pos.y); // Converte coordenadas do mundo para píxeis
        p.imageMode(PApplet.CENTER); //Usamos CENTER porque queremos que a posição da bolha corresponda ao centro da imagem.
        p.image(img, pix[0], pix[1], 10, 10); // Converte coordenadas do mundo para píxeis
    }

    // Verifica se a bolha saiu da água (chegou ao topo do mundo)
    public boolean outOfWater() {
        return pos.y > 2f;
    }
}
