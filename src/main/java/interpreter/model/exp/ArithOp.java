package interpreter.model.exp;

/**
 * §4.2: operators as an enum, not int codes or characters.
 * toString() gives the symbol, so ArithExp.toString() can print "2 + (3 * 5)".
 */
public enum ArithOp {
    ADD("+"), SUB("-"), MUL("*"), DIV("/");

    private final String symbol;

    ArithOp(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}