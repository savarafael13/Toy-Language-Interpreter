package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// A4 (§2.4, Rule S12): mutates an already-allocated heap cell referred to by varName
public record WriteHeapStmt(String varName, IExp exp) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();

        // A4 (Rule S12 error 1): variable must be declared in SymTable
        if (!symTable.isDefined(varName))
            throw new StatementException("wH: " + varName + " is not declared");

        // A4 (Rule S12 error 2): variable must hold a RefValue
        if (!(symTable.lookup(varName) instanceof RefValue ref))
            throw new StatementException("wH: " + varName + " is not a reference");

        // A4 (Rule S12 error 3): target heap address must still exist in the heap (cannot write to 0/null)
        var heap = state.getHeap();
        if (!heap.isDefined(ref.address()))
            throw new StatementException("wH: address " + ref.address() + " is not in the heap");

        // A4 (Rule S12 error 4): new value's type must match the reference's locationType
        IValue value = exp.eval(symTable, heap);
        if (!value.getType().equals(ref.locationType()))
            throw new StatementException("wH: " + exp + " has type " + value.getType()
                    + ", expected " + ref.locationType());

        heap.update(ref.address(), value);
        return state;
    }

    // A4 (§2.4): representation for logs and menus
    @Override
    public String toString() {
        return "wH(" + varName + ", " + exp + ")";
    }
}