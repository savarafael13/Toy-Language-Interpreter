package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap; // A4 (§2.3): passed down to sub-expressions
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record ArithExp(ArithOp op, IExp left, IExp right) implements IExp {

    // A4 (§2.3): passes heap when evaluating left and right operands
    @Override
    public IValue eval(IDictionary<String, IValue> symTable, IHeap heap) throws MyException {
        if (!(left.eval(symTable, heap) instanceof IntValue(int n1)))
            throw new ExpressionException("first operand is not an integer");
        if (!(right.eval(symTable, heap) instanceof IntValue(int n2)))
            throw new ExpressionException("second operand is not an integer");

        return new IntValue(switch (op) {
            case ADD -> n1 + n2;
            case SUB -> n1 - n2;
            case MUL -> n1 * n2;
            case DIV -> {
                if (n2 == 0)
                    throw new ExpressionException("division by zero");
                yield n1 / n2;
            }
        });
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}