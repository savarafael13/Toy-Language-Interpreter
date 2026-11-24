package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;    // A4 (§2.3)
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;   // A4 (§2.3)
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {

    private IDictionary<String, IValue> symTable;
    private IHeap heap; // A4 (§2.3)

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap(); // A4 (§2.3)
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @Test
    void lessThanEvaluatesTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LT, num(2), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LT, num(5), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LT, num(7), num(5)).eval(symTable, heap));
    }

    @Test
    void lessOrEqualEvaluatesTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LE, num(2), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.LE, num(5), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.LE, num(6), num(5)).eval(symTable, heap));
    }

    @Test
    void equalEvaluatesTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.EQ, num(4), num(4)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.EQ, num(4), num(5)).eval(symTable, heap));
    }

    @Test
    void notEqualEvaluatesTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.NE, num(4), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.NE, num(4), num(4)).eval(symTable, heap));
    }

    @Test
    void greaterThanEvaluatesTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GT, num(7), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GT, num(5), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GT, num(2), num(5)).eval(symTable, heap));
    }

    @Test
    void greaterOrEqualEvaluatesTrueAndFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GE, num(7), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(true),
                new RelationalExp(RelOp.GE, num(5), num(5)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new RelationalExp(RelOp.GE, num(4), num(5)).eval(symTable, heap));
    }

    @Test
    void nonIntegerFirstOperandThrows() {
        IExp boolFirst = new RelationalExp(RelOp.LT, new ValueExp(new BoolValue(true)), num(1));
        var ex1 = assertThrows(ExpressionException.class, () -> boolFirst.eval(symTable, heap));
        assertEquals("first operand is not an integer", ex1.getMessage());

        IExp strFirst = new RelationalExp(RelOp.EQ, new ValueExp(new StringValue("a")), num(1));
        var ex2 = assertThrows(ExpressionException.class, () -> strFirst.eval(symTable, heap));
        assertEquals("first operand is not an integer", ex2.getMessage());
    }

    @Test
    void nonIntegerSecondOperandThrows() {
        IExp boolSecond = new RelationalExp(RelOp.GT, num(1), new ValueExp(new BoolValue(false)));
        var ex1 = assertThrows(ExpressionException.class, () -> boolSecond.eval(symTable, heap));
        assertEquals("second operand is not an integer", ex1.getMessage());

        IExp strSecond = new RelationalExp(RelOp.NE, num(1), new ValueExp(new StringValue("b")));
        var ex2 = assertThrows(ExpressionException.class, () -> strSecond.eval(symTable, heap));
        assertEquals("second operand is not an integer", ex2.getMessage());
    }

    @Test
    void toStringFormatsInfix() {
        IExp expr = new RelationalExp(RelOp.LT, new VarExp("a"), new VarExp("b"));
        assertEquals("(a < b)", expr.toString());
    }
}