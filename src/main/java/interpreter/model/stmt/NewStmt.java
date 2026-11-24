package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// A4 (§2.4, Rule S11): allocates a value on the heap and updates varName to point to the new address
public record NewStmt(String varName, IExp exp) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();

        // A4 (Rule S11 error 1): variable must be declared in SymTable
        if (!symTable.isDefined(varName))
            throw new StatementException("new: " + varName + " is not declared");

        // A4 (Rule S11 error 2): variable must hold a RefValue (0, T) or (addr, T)
        if (!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("new: " + varName + " is not a reference");

        // A4 (Rule S11 error 3): evaluated expression type must match the reference's inner locationType
        IValue value = exp.eval(symTable, state.getHeap());
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("new: " + exp + " has type " + value.getType()
                    + ", expected " + ref.locationType());

        // A4 (Rule S11): store value at a fresh 1-based heap address and update SymTable entry
        int address = state.getHeap().allocate(value);
        symTable.update(varName, new RefValue(address, ref.locationType()));
        return state;
    }

    // A4 (§2.4): infix representation for logs and menus
    @Override
    public String toString() {
        return "new(" + varName + ", " + exp + ")";
    }
}