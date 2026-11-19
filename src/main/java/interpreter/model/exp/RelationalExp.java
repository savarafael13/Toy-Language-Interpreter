package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

// A3 (§2.2, Rule E5): evaluates two integer expressions and yields a BoolValue
// so relational expressions can be used directly as IfStmt conditions
public record RelationalExp(RelOp op, IExp left, IExp right) implements IExp {

    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        // A3 (Rule E5): evaluate left first, then right; both must be IntValue
        if (!(left.eval(symTable) instanceof IntValue(int n1)))
            throw new ExpressionException("first operand is not an integer");
        if (!(right.eval(symTable) instanceof IntValue(int n2)))
            throw new ExpressionException("second operand is not an integer");

        // A3 (Rule E5): apply the relational comparison and wrap the result in BoolValue
        return new BoolValue(switch (op) {
            case LT -> n1 < n2;
            case LE -> n1 <= n2;
            case EQ -> n1 == n2;
            case NE -> n1 != n2;
            case GT -> n1 > n2;
            case GE -> n1 >= n2;
        });
    }

    // A3 (§2.2, §2.5): infix representation matching ArithExp and LogicExp
    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}