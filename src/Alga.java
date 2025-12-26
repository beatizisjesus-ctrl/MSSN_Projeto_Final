
import processing.core.PApplet;
import processing.core.PVector;

public class Alga {
	 	private LSystem Ls;
	    private TurtleAlga turtle;
	    private PVector pos;
	    private final int nSeasons;//iterações
	    private float len, growthRate, timeStamp, tNextSeason; //len é o tamanho atual e referenceLen é o tamanho que queremos que fique a tree, para sabermos a taxa de cresciemnto
	    private final float scalingFactor, breakBetweenSeasons; 
	    

	
	public Alga (String axiom, Rule_LSyst[] ruleset, PVector pos,
            float referenceLen, float angle, int nIterations,
            float scaleFactor, float breakSeasons, PApplet p) {

	    Ls = new LSystem(axiom, ruleset);
	    len = 0; //tamanho do segmento na primeira iteracao
	    growthRate = referenceLen/breakSeasons;//isto foi feito para as trees irem crescendo ao longo do tempo: taxa de crescimento
	    turtle = new TurtleAlga(0, angle);
	
	    this.pos = pos;// posiçao da semente
	    nSeasons = nIterations;//numero de iterações que uma ree precisa para crescer
	    this.scalingFactor = scaleFactor;//passo, o quanto diminui de uma iteraçao para outra
	    this.breakBetweenSeasons = breakSeasons;//intervalo entre iteracoes
	    timeStamp = p.millis()/1000f; //converte o tempo para segundos
	    tNextSeason = timeStamp + breakBetweenSeasons; //tempo em que a proxima iteraçao acontece

}
	

	
	public void grow(float dt) {//tempo atual, em que o metodo grow é chamado 
        timeStamp += dt;//incrementa-se o tempo que vai passando de uma iteraçao para outra
        if(timeStamp < tNextSeason) { // Se ainda nao está na altura de fazer uma iteração/estaçao
            len += growthRate*dt; //cresce x metros por segundo  
            turtle.setLen(len);
        } else if(Ls.getGen() < nSeasons) {// se a tree estiver num numero de iteraçao menor do que o numero de iteraçao que precisa para estar adulta, ou seja se ainda nao cresceu tudo 
        	Ls.nextGen(); 
            len *= scalingFactor;
            growthRate *= scalingFactor;
            turtle.setLen(len);
            tNextSeason = timeStamp + breakBetweenSeasons;
        }
        turtle.update(dt);
    }

	public void display(PApplet parent, SubPlot plt) {

        //para mostrar as algas criadas
        parent.pushMatrix();// antes de renderizar guarda o sistema de coordenadas
        parent.stroke(0, 80,0);
        turtle.setPose(pos, (float) Math.PI/2, parent, plt);
        turtle.render(Ls, parent, plt);
        parent.popMatrix();// no fim repoe tudo como estava, para estra tudo pronto para a proxima arvore
		
	}
}
