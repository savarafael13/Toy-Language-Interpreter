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

    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
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

    // A4 (§2.2): updated to verify all five log sections appear in exact order
    @Test
    void logFileContainsTheFiveSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:");
        int b = log.indexOf("SymTable:");
        int c = log.indexOf("Out:");
        int d = log.indexOf("FileTable:");
        int e = log.indexOf("Heap:");
        assertTrue(0 <= a && a < b && b < c && c < d && d < e);
    }

    @Test
    void invalidLogPathThrowsRepositoryException() {
        PrgState state = PrgState.of(Examples.EX1);
        var repo = new Repository(state, dir.toString());
        var controller = new Controller(repo, false);
        assertThrows(RepositoryException.class, controller::allStep);
    }

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(new IntValue(15), s.getOut().get(0));
        assertEquals(new IntValue(50), s.getOut().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithEmptyFilePrintsZerosAndClosesTheFile() throws Exception {
        Path emptyIn = dir.resolve("empty.in");
        Files.writeString(emptyIn, "");
        PrgState s = run(Examples.fileExample(emptyIn.toString()));
        assertEquals(new IntValue(0), s.getOut().get(0));
        assertEquals(new IntValue(0), s.getOut().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void example5PrintsFive() throws MyException {
        PrgState s = run(Examples.EX5);
        assertEquals(1, s.getOut().size());
        assertEquals(new IntValue(5), s.getOut().get(0));
    }
}