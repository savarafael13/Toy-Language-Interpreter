package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// A4 (§2.4, Rule E6): dereferences a RefValue and returns the value stored at its heap address
public record ReadHeapExp(IExp exp) implements IExp {

    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        // A4 (Rule E6 error 1, §2.7): inner expression must evaluate to a RefValue
        if (!(exp.eval(symTable, heap) instanceof RefValue ref))
            throw new ExpressionException("rH: " + exp + " is not a reference");

        // A4 (Rule E6 error 2, §2.6, §2.7): address must exist in the heap (0/null or collected throws)
        if (!heap.isDefined(ref.address()))
            throw new ExpressionException("rH: address " + ref.address() + " is not in the heap");

        return heap.get(ref.address());
    }

    // A4 (§2.4): representation for logs and menus
    @Override
    public String toString() {
        return "rH(" + exp + ")";
    }
}