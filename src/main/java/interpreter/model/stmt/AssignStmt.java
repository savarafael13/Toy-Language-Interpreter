package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;

public record AssignStmt(String id, IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        // S2 error 1: undeclared variable.
        if (!symTable.isDefined(id))
            throw new StatementException("variable " + id + " was not declared");
        IValue val = exp.eval(symTable);
        // S2 error 2: compare the value's type against the DECLARED type from the symtable.
        // Not against val.getType() itself - that would always pass silently.
        // Works only because types are records, so new IntType().equals(new IntType()).
        if (!val.getType().equals(symTable.lookup(id).getType()))
            throw new StatementException("declared type of variable " + id
                    + " and type of the assigned expression do not match");
        symTable.update(id, val);
        return state;
    }

    @Override
    public String toString() {
        return id + " = " + exp;
    }
}