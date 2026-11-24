package interpreter.model.stmt;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// A4 (§3.1, §3.2): Step 5 tests for WhileStmt (0, 1, and n iterations + non-boolean condition)
class WhileStmtTest {

    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, dir.resolve("while_log.txt").toString());
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void loopRunsZeroTimesWhenConditionIsInitiallyFalse() throws MyException {
        // int v; v = 0; (while (v > 0) v = v - 1); print(v) -> prints 0
        IStmt program = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(0))),
                        new CompStmt(new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                new AssignStmt("v", new ArithExp(ArithOp.SUB,
                                        new VarExp("v"), new ValueExp(new IntValue(1))))),
                                new PrintStmt(new VarExp("v")))));

        PrgState state = run(program);
        assertEquals(List.of(new IntValue(0)), state.getOut().toList());
    }

    @Test
    void loopRunsOnceWhenConditionBecomesFalseAfterFirstBody() throws MyException {
        // int v; v = 1; (while (v > 0) (print(v); v = v - 1)); print(v) -> prints 1, 0
        IStmt program = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(1))),
                        new CompStmt(new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp(ArithOp.SUB,
                                                new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));

        PrgState state = run(program);
        assertEquals(List.of(new IntValue(1), new IntValue(0)), state.getOut().toList());
    }

    @Test
    void loopRunsMultipleTimes() throws MyException {
        // int v; v = 3; (while (v > 0) (print(v); v = v - 1)); print(v) -> prints 3, 2, 1, 0
        IStmt program = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(3))),
                        new CompStmt(new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp(ArithOp.SUB,
                                                new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));

        PrgState state = run(program);
        assertEquals(
                List.of(new IntValue(3), new IntValue(2), new IntValue(1), new IntValue(0)),
                state.getOut().toList()
        );
    }

    @Test
    void whilePushesSelfThenBodyOntoStackWhenConditionIsTrue() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().pop();

        IStmt body = new NopStmt();
        IStmt loop = new WhileStmt(new ValueExp(new BoolValue(true)), body);
        loop.execute(state);

        // Top of stack must be body, followed underneath by the WhileStmt itself
        assertEquals(List.of(body, loop), state.getExeStack().toList());
    }

    @Test
    void whileWithNonBooleanConditionThrows() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        state.getExeStack().pop();

        var intCond = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> intCond.execute(state));

        var strCond = new WhileStmt(new ValueExp(new StringValue("true")), new NopStmt());
        assertThrows(StatementException.class, () -> strCond.execute(state));
    }

    @Test
    void toStringFormatsLoopSyntax() {
        IStmt loop = new WhileStmt(
                new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
                new PrintStmt(new VarExp("v"))
        );
        assertEquals("(while ((v > 0)) print(v))", loop.toString());
    }
}