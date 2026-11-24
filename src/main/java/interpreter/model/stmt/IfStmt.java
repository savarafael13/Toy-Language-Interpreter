package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;

public record IfStmt(IExp exp, IStmt thenS, IStmt elseS) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // A4 (§2.3): pass state.getHeap() into exp.eval(...)
        if (!(exp.eval(state.getSymTable(), state.getHeap()) instanceof BoolValue(boolean cond))) {
            throw new StatementException("conditional expr is not a boolean");
        }
        state.getExeStack().push(cond ? thenS : elseS);
        return state;
    }

    @Override
    public String toString() {
        return "(if " + exp + " then " + thenS + " else " + elseS + ")";
    }
}