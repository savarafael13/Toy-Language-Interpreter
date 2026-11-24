package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;

public record PrintStmt(IExp exp) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // A4 (§2.3): pass state.getHeap() into exp.eval(...)
        state.getOut().add(exp.eval(state.getSymTable(), state.getHeap()));
        return state;
    }

    @Override
    public String toString() {
        return "print(" + exp + ")";
    }
}