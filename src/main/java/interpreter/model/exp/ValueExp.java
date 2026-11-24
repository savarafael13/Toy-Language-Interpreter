package interpreter.model.exp;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap; // A4 (§2.3): updated eval signature
import interpreter.model.value.IValue;

public record ValueExp(IValue val) implements IExp {

    // A4 (§2.3): accepts heap parameter to match IExp.eval
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}