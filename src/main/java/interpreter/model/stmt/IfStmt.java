package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record IfStmt(IExp cond, IStmt thenStmt, IStmt elseStmt) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue c = cond.eval(state.getSymTable());
        // §4.10: a non-boolean condition is a StatementException, not an ExpressionException.
        if (!(c instanceof BoolValue(boolean b)))
            throw new StatementException("the condition of the if is not a boolean");
        // S5: push exactly ONE branch - never both.
        state.getExeStack().push(b ? thenStmt : elseStmt);
        return state;
    }

    @Override
    public String toString() {
        return "(if " + cond + " then " + thenStmt + " else " + elseStmt + ")";
    }
}