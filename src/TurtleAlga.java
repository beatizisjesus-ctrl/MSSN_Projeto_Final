import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class TurtleAlga {
	private float len;
    private final float angle;
    private float time;// ondulaçao nas algas
    private float waveSpeed = 1.5f;
    private float waveAmplitude = 0.25f;
    
    
    public TurtleAlga(float len, float angle) {
        this.len = len;//tamanho do passo que a turtle dá inicialmente, comprimento dos ramos
        this.angle = angle;
    }
    
    public void setPose(PVector pos, float orientation, PApplet p, SubPlot plt) {//colocar a turtle no sitio do canvas
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        p.translate(pp[0], pp[1]);
        p.rotate(-orientation);
    }

    public void scaling(float s) { //diminui o passo, se nao o desenho sai fora do canvas
    	len *= s;
    }

    public float getLen() { 
    	return len; 
    }
    public void setLen(float len) {
    	this.len = len;
    }
	
	public void update(float dt) {
	    time += dt;
	}

    
    
    public void render(LSystem Ls, PApplet p, SubPlot plt) {
        float[] lenPix = plt.getVectorCoord(len, len);//trasnformas o len(o passo) em pixeis
        int flexLevel = 0;
        for(int i = 0; i < Ls.getSequence().length(); i++) {
            char c = Ls.getSequence().charAt(i);
            float wave = PApplet.sin(time * waveSpeed + flexLevel) 
                    * waveAmplitude 
                    * flexLevel * 0.2f;
            if(c == 'F' || c == 'G') {
                p.line(0, 0, lenPix[0], 0);//F ou g anda para a frente, neste caso é o unico que desenha
                p.translate(lenPix[0], 0);
            }
            else if(c == 'f') p.translate(lenPix[0], 0); //anda sem desenhar nada
            else if(c == '+') p.rotate(angle + wave); //fazer rotação do angle positivo
            else if(c == '-') p.rotate(-angle + wave);//o mesmo mas negativo
            else if (c == '[') {
                flexLevel++; //quando entra um novo ramo, esta mais longe da base, logo é mais flexivel
                p.pushMatrix();//guarda no sistema a posição da turtle, o estado atual das transformações
            }
            else if (c == ']') {
                flexLevel--; //terminou esse ramo, logo volta para uma parte mais rigida, nao se mexe ou mexe menos
                p.popMatrix();//restaura o ultimo estado guardado
            }
            
        }
    }
}
