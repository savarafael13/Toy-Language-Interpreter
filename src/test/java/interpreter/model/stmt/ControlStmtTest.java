package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclAndAssignTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        state.getExeStack().pop();
    }

    @Test
    void intDeclarationStoresDefaultZero() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void boolDeclarationStoresDefaultFalse() throws MyException {
        new VarDeclStmt("a", new BoolType()).execute(state);
        assertEquals(new BoolValue(false), state.getSymTable().lookup("a"));
    }

    @Test
    void redeclaringAVariableThrows() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        var ex = assertThrows(StatementException.class,
                () -> new VarDeclStmt("v", new IntType()).execute(state));
        assertEquals("variable v is already declared", ex.getMessage());
    }

    @Test
    void varDeclPushesNothingOntoTheStack() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void assignmentStoresTheEvaluatedValue() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        new AssignStmt("v", new ArithExp(ArithOp.ADD,
                new ValueExp(new IntValue(2)), new ValueExp(new IntValue(3)))).execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("v"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        var ex = assertThrows(StatementException.class,
                () -> new AssignStmt("v", new ValueExp(new IntValue(2))).execute(state));
        assertEquals("variable v was not declared", ex.getMessage());
    }

    @Test
    void assigningBooleanToIntVariableThrows() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        var ex = assertThrows(StatementException.class,
                () -> new AssignStmt("v", new ValueExp(new BoolValue(true))).execute(state));
        assertEquals("declared type of variable v and type of the assigned expression do not match",
                ex.getMessage());
    }

    @Test
    void assigningIntToBoolVariableThrows() throws MyException {
        new VarDeclStmt("a", new BoolType()).execute(state);
        assertThrows(StatementException.class,
                () -> new AssignStmt("a", new ValueExp(new IntValue(1))).execute(state));
    }

    @Test
    void assignmentPropagatesExpressionErrors() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        assertThrows(ExpressionException.class,
                () -> new AssignStmt("v", new VarExp("undefined")).execute(state));
    }
}