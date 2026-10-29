package interpreter.model.exp;
//operators as an enum not int/chars / tostring gives the symbol so arithexp.tostring can produce 2_ (3*%( with no switch
public enum Arith0p {
    ADD("+") , SUB("-") , MUL("*") ,DIV("/");
    private final String symbol;
    Arith0p(String symbol){
        this.symbol= symbol;
    }
    @Override
    public String toString(){
        return symbol;
    }

}
