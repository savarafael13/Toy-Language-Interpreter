package interpreter.model.type;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// A4 (§3.1): Step 1 tests for RefType, RefValue, default (0, T), and recursive equals
class RefTypeTest {

    @Test
    void refTypeDefaultValueIsNullReference() {
        IType refInt = new RefType(new IntType());
        assertEquals(new RefValue(0, new IntType()), refInt.defaultValue());
        assertEquals("Ref(int)", refInt.toString());
    }

    @Test
    void declaredRefVariableGetsDefaultZeroAddress() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        IType refRefInt = new RefType(new RefType(new IntType()));

        new VarDeclStmt("a", refRefInt).execute(state);

        assertEquals(new RefValue(0, new RefType(new IntType())),
                state.getSymTable().lookup("a"));
    }

    @Test
    void nestedRefTypesCompareInnerTypesRecursively() {
        IType refInt = new RefType(new IntType());
        IType refRefInt1 = new RefType(new RefType(new IntType()));
        IType refRefInt2 = new RefType(new RefType(new IntType()));

        assertEquals(refRefInt1, refRefInt2);
        assertEquals(refRefInt1.hashCode(), refRefInt2.hashCode());
        assertNotEquals(refRefInt1, refInt);
        assertEquals("Ref(Ref(int))", refRefInt1.toString());
    }

    @Test
    void refValueReportsItsRefTypeAndFormatsProperly() {
        RefValue val = new RefValue(1, new IntType());
        assertEquals(new RefType(new IntType()), val.getType());
        assertEquals("(1,int)", val.toString());
    }
}