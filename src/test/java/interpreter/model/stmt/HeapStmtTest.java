package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// A4 (§3.1, §3.2): Step 4 tests for NewStmt (S11), ReadHeapExp (E6), WriteHeapStmt (S12), and all errors
class HeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
        // Declare "Ref int v;" -> initialized to (0, int)
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);
    }

    // --- Rule S11: new(v, exp) ---

    @Test
    void newAllocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void newWithUndeclaredVariableThrows() {
        var exp = new ValueExp(new IntValue(20));
        assertThrows(StatementException.class, () -> new NewStmt("missing", exp).execute(state));
    }

    @Test
    void newWithNonReferenceVariableThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        var exp = new ValueExp(new IntValue(20));
        assertThrows(StatementException.class, () -> new NewStmt("x", exp).execute(state));
    }

    @Test
    void newWithAValueOfTheWrongTypeThrows() {
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new NewStmt("v", exp).execute(state));
    }

    // --- Rule E6: rH(exp) ---

    @Test
    void readHeapReturnsValueAndSupportsNestedReferences() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new VarDeclStmt("a", new RefType(new RefType(new IntType()))).execute(state);
        new NewStmt("a", new VarExp("v")).execute(state);

        assertEquals(new IntValue(20),
                new ReadHeapExp(new VarExp("v")).eval(state.getSymTable(), state.getHeap()));
        assertEquals(new IntValue(20),
                new ReadHeapExp(new ReadHeapExp(new VarExp("a"))).eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void readHeapWithNonReferenceExpressionThrows() {
        var nonRef = new ValueExp(new IntValue(10));
        assertThrows(ExpressionException.class,
                () -> new ReadHeapExp(nonRef).eval(state.getSymTable(), state.getHeap()));
    }

    @Test
    void readHeapWithNullOrMissingAddressThrows() {
        // v is still (0, int) before new(v, ...) is called
        assertThrows(ExpressionException.class,
                () -> new ReadHeapExp(new VarExp("v")).eval(state.getSymTable(), state.getHeap()));
    }

    // --- Rule S12: wH(v, exp) ---

    @Test
    void writeHeapUpdatesExistingLocation() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);

        assertEquals(new IntValue(30), state.getHeap().get(1));
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
    }

    @Test
    void writeHeapWithUndeclaredVariableThrows() {
        var exp = new ValueExp(new IntValue(30));
        assertThrows(StatementException.class, () -> new WriteHeapStmt("missing", exp).execute(state));
    }

    @Test
    void writeHeapWithNonReferenceVariableThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        var exp = new ValueExp(new IntValue(30));
        assertThrows(StatementException.class, () -> new WriteHeapStmt("x", exp).execute(state));
    }

    @Test
    void writeHeapWithNullOrMissingAddressThrows() {
        // v has address 0 (not in heap)
        var exp = new ValueExp(new IntValue(30));
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", exp).execute(state));
    }

    @Test
    void writeHeapWithWrongValueTypeThrows() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        var wrongType = new ValueExp(new BoolValue(false));
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", wrongType).execute(state));
    }
}