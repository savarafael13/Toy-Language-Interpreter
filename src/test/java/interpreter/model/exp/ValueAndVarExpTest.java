package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueAndVarExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void valueExpReturnsItsIntegerValue() throws MyException {
        assertEquals(new IntValue(7), new ValueExp(new IntValue(7)).eval(symTable));
    }

    @Test
    void valueExpReturnsItsBooleanValue() throws MyException {
        assertEquals(new BoolValue(true), new ValueExp(new BoolValue(true)).eval(symTable));
    }

    @Test
    void varExpReturnsTheValueFromTheSymTable() throws MyException {
        symTable.put("a", new IntValue(3));
        assertEquals(new IntValue(3), new VarExp("a").eval(symTable));
    }

    @Test
    void undefinedVariableThrows() {
        var ex = assertThrows(ExpressionException.class, () -> new VarExp("x").eval(symTable));
        assertEquals("variable x is not defined", ex.getMessage());
    }

    @Test
    void varExpDoesNotModifyTheSymTable() throws MyException {
        symTable.put("a", new IntValue(3));
        new VarExp("a").eval(symTable);
        assertTrue(symTable.isDefined("a"));
        assertEquals(new IntValue(3), symTable.lookup("a"));
    }
}