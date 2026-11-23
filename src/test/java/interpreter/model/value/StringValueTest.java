package interpreter.model.value;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {

    @Test
    void stringTypeDefaultValueIsEmptyString() {
        var type = new StringType();
        assertEquals(new StringValue(""), type.defaultValue());
        assertEquals("string", type.toString());
    }

    @Test
    void declaredStringVariableGetsDefaultEmptyString() throws MyException {
        // A3  checkts that varf   and  symtable initializes it to  string value and . it tests vardeclstmt  properly delegates stringtype.defaultvalue
        PrgState state = PrgState.of(new NopStmt());

        new VarDeclStmt("varf", new StringType()).execute(state);

        assertEquals(new StringValue(""), state.getSymTable().lookup("varf"));
    }

    @Test
//
    // takes 2 new string objects "testin.in" into stringvalue and checks they are equal and produce the same hashcode
    void equalStringValuesHaveSameHashCode() {
        var first = new StringValue(new String("test.in"));
        var second = new StringValue(new String("test.in"));
        var different = new StringValue("other.in");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, different);
        assertEquals("\"test.in\"", first.toString());
    }
}