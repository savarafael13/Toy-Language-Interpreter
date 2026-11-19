package interpreter.model.exp;

// A3 (§2.2): enum for the six relational operators, matching ArithOp's design
public enum RelOp {
    LT("<"),
    LE("<="),
    EQ("=="),
    NE("!="),
    GT(">"),
    GE(">=");

    private final String symbol;

    RelOp(String symbol) {
        this.symbol = symbol;
    }

    // A3 (§2.2, §2.5): prints the operator symbol in infix expressions (e.g. "(a < b)")
    @Override
    public String toString() {
        return symbol;
    }
}