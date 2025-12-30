package Cenário;
public class LSystem {
	private String sequence;
    private final Rule_LSyst[] ruleset;//array de objetos Rule, que definem como cada símbolo é substituído.
    private int gen;//contador da geração atual do L-system, para saber a geraçao em que esta

    public LSystem(String axiom, Rule_LSyst[] ruleset) {
        sequence = axiom; //sequencia inicial do Lsystem, como o ponto de partida e a apartir daí 
        //aplicamos repetidamente as regras de substituição para gerar novas sequências.
        this.ruleset = ruleset; //conjunto de regras de substituição.
        gen = 0;
    }
    public int getGen() {
    	return gen;
    }
    public String getSequence() {
    	return sequence; 
    }

    
    public void nextGen() {
        gen++;
        String nextGen = "";
        //percorre cada caracter da sequencia atual e verifica se algum destes caracteres corresponde a uma regra
        //Se sim substitui, se nao mantem-seo caractere de "c", ou seja o proprio caractere
        for(int i = 0; i < sequence.length(); i++) {

            char c = sequence.charAt(i);
            String replace = "" + c;

            for (Rule_LSyst r : ruleset) {
                if (c == r.getSymbol()) {
                    replace = r.getString();
                    break;
                }
            }
            nextGen += replace;
        }

        this.sequence = nextGen;
    }
}

