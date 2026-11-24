package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap; // A4 (§2.3): needed so rH(exp) can read the heap
import interpreter.model.value.IValue;

public interface IExp {
    // A4 (§2.3): updated signature passing IHeap alongside SymTable
    IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException;
}