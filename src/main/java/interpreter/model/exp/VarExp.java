package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap; // A4 (§2.3): updated eval signature
import interpreter.model.value.IValue;

public record VarExp(String id) implements IExp {

    // A4 (§2.3): accepts heap parameter to match IExp.eval
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        if (!symTable.isDefined(id)) {
            throw new ExpressionException("variable " + id + " is not defined");
        }
        return symTable.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }
}