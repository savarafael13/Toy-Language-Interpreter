package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;    // A4 (§2.3)
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;   // A4 (§2.3)
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap; // A4 (§2.3)

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap(); // A4 (§2.3)
    }

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @Test
    void andIsTrueOnlyWhenBothTrue() throws MyException {
        assertEquals(new BoolValue(true),
                new LogicExp(LogicOp.AND, bool(true), bool(true)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new LogicExp(LogicOp.AND, bool(true), bool(false)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new LogicExp(LogicOp.AND, bool(false), bool(true)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new LogicExp(LogicOp.AND, bool(false), bool(false)).eval(symTable, heap));
    }

    @Test
    void orIsFalseOnlyWhenBothFalse() throws MyException {
        assertEquals(new BoolValue(true),
                new LogicExp(LogicOp.OR, bool(true), bool(false)).eval(symTable, heap));
        assertEquals(new BoolValue(true),
                new LogicExp(LogicOp.OR, bool(false), bool(true)).eval(symTable, heap));
        assertEquals(new BoolValue(false),
                new LogicExp(LogicOp.OR, bool(false), bool(false)).eval(symTable, heap));
        assertEquals(new BoolValue(true),
                new LogicExp(LogicOp.OR, bool(true), bool(true)).eval(symTable, heap));
    }

    @Test
    void nonBooleanFirstOperandThrows() {
        IExp e = new LogicExp(LogicOp.AND, new ValueExp(new IntValue(1)), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("first operand is not a boolean", ex.getMessage());
    }

    @Test
    void nonBooleanSecondOperandThrows() {
        IExp e = new LogicExp(LogicOp.OR, bool(true), new ValueExp(new IntValue(0)));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }

    @Test
    void rightOperandIsStillEvaluatedWhenLeftDecidesTheResult() {
        IExp e = new LogicExp(LogicOp.OR, bool(true), new VarExp("missing"));
        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }
}