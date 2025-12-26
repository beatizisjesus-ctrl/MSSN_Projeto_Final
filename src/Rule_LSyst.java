
public class Rule_LSyst {
	private final char symbol;//simbolo que desencadeia a regra
    private final String string;//simbolo vai ser substituido por string

    public Rule_LSyst(char symbol, String string) {
        this.symbol = symbol;
        this.string = string;
    }

    public char getSymbol() { return symbol; }
    public String getString() { return string; }

}
