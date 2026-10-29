package interpreter.model.type;

import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypesAndValuesTest {

    @Test
    void intTypeDefaultValueIsZero() {
        assertEquals(new IntValue(0), new IntType().defaultValue());
    }

    @Test
    void boolTypeDefaultValueIsFalse() {
        assertEquals(new BoolValue(false), new BoolType().defaultValue());
    }

    @Test
    void sameTypesAreEqual() {
        assertEquals(new IntType(), new IntType());
        assertEquals(new BoolType(), new BoolType());
    }

    @Test
    void differentTypesAreNotEqual() {
        assertNotEquals(new IntType(), new BoolType());
    }

    @Test
    void intValueKnowsItsType() {
        assertEquals(new IntType(), new IntValue(42).getType());
    }

    @Test
    void boolValueKnowsItsType() {
        assertEquals(new BoolType(), new BoolValue(true).getType());
    }

    @Test
    void valuesWithSameContentAreEqual() {
        assertEquals(new IntValue(7), new IntValue(7));
        assertNotEquals(new IntValue(7), new IntValue(8));
    }

    @Test
    void valuesHaveReadableToString() {
        assertEquals("42", new IntValue(42).toString());
        assertEquals("true", new BoolValue(true).toString());
        assertEquals("int", new IntType().toString());
        assertEquals("bool", new BoolType().toString());
    }
}