package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.RepositoryException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    // A3 (§3, §3.2): temporary directory so log files created during tests never touch project root
    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        // A3 (§2.6, §3): pass a temporary log file path to Repository
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        assertEquals(new IntValue(2), run(Examples.EX1).getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        assertEquals(new IntValue(2), run(Examples.EX3).getOut().get(0));
    }

    @Test
    void allStepEmptiesTheExeStack() throws MyException {
        assertTrue(run(Examples.EX1).getExeStack().isEmpty());
    }

    @Test
    void allStepLeavesDeclaredVariablesInTheSymTable() throws MyException {
        PrgState state = run(Examples.EX2);
        assertEquals(new IntValue(18), state.getSymTable().lookup("b"));
    }

    @Test
    void oneStepOnAnEmptyStackThrows() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        var repo = new Repository(state, dir.resolve("empty_log.txt").toString());
        Controller controller = new Controller(repo, false);
        controller.oneStep(state);
        assertTrue(state.getExeStack().isEmpty());
        var ex = assertThrows(StatementException.class, () -> controller.oneStep(state));
        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
    }

    // A3 (§3.1, §3.2): Step 5 test verifying the four required log sections appear in order
    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:");
        int b = log.indexOf("SymTable:");
        int c = log.indexOf("Out:");
        int d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }

    // A3 (§2.6, §2.8): verifies IOException while opening the log file becomes RepositoryException
    @Test
    void invalidLogPathThrowsRepositoryException() {
        PrgState state = PrgState.of(Examples.EX1);
        // Passing a directory path instead of a file path triggers IOException on FileWriter
        var repo = new Repository(state, dir.toString());
        var controller = new Controller(repo, false);
        assertThrows(RepositoryException.class, controller::allStep);
    }
}