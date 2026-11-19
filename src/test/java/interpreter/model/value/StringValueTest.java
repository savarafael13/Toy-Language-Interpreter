package interpreter.model.value;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// A3 (§3.1): Step 1 tests for StringType default value and StringValue equals/hashCode
class StringValueTest {

    @Test
    void stringTypeDefaultValueIsEmptyString() {
        var type = new StringType();
        assertEquals(new StringValue(""), type.defaultValue());
        assertEquals("string", type.toString());
    }

    @Test
    void declaredStringVariableGetsDefaultEmptyString() throws MyException {
        // Uses A2 4-arg PrgState constructor for now; Step 3 migrates to PrgState.of(...)
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt()
        );

        new VarDeclStmt("varf", new StringType()).execute(state);

        assertEquals(new StringValue(""), state.getSymTable().lookup("varf"));
    }

    @Test
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