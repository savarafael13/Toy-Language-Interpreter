package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// A3 (§3.1, §3.2): Step 4 tests for openRFile, readFile, closeRFile and all S8-S10 errors
class FileStmtTest {

    @TempDir
    Path dir;

    private PrgState state;
    private Path numbersFile;
    private ValueExp fileExp;
    private StringValue fileKey;

    @BeforeEach
    void setUp() throws Exception {
        numbersFile = dir.resolve("numbers.in");
        Files.writeString(numbersFile, "15\n50\n");

        fileKey = new StringValue(numbersFile.toString());
        fileExp = new ValueExp(fileKey);

        state = PrgState.of(new NopStmt());
        state.getExeStack().pop();
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @AfterEach
    void closeAnyOpenReaders() throws IOException {
        for (BufferedReader reader : state.getFileTable().toMap().values()) {
            reader.close();
        }
    }

    // --- Rule S8: openRFile ---

    @Test
    void openRFileAddsEntryToFileTable() throws MyException {
        new OpenRFileStmt(fileExp).execute(state);
        assertTrue(state.getFileTable().isDefined(fileKey));
    }

    @Test
    void openRFileWithNonStringThrows() {
        var nonString = new ValueExp(new IntValue(42));
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(nonString).execute(state));
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(fileExp).execute(state);
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(fileExp).execute(state));
    }

    @Test
    void openRFileWithMissingFileThrows() {
        var missing = new ValueExp(new StringValue(dir.resolve("does_not_exist.in").toString()));
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state));
    }

    // --- Rule S9: readFile ---

    @Test
    void readFileReadsNumbersInOrderThenZeroAtEof() throws MyException {
        new OpenRFileStmt(fileExp).execute(state);

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(15), state.getSymTable().lookup("x"));

        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(50), state.getSymTable().lookup("x"));

        // Past EOF -> returns 0
        new ReadFileStmt(fileExp, "x").execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void readFileWithUndeclaredVariableThrows() throws MyException {
        new OpenRFileStmt(fileExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "undeclared").execute(state));
    }

    @Test
    void readFileWithNonIntVariableThrows() throws MyException {
        new VarDeclStmt("s", new StringType()).execute(state);
        new OpenRFileStmt(fileExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "s").execute(state));
    }

    @Test
    void readFileWithNonStringExpressionThrows() {
        var nonString = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(nonString, "x").execute(state));
    }

    @Test
    void readFileWhenFileIsNotOpenThrows() {
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(fileExp, "x").execute(state));
    }

    @Test
    void readFileWithNonIntegerLineThrows() throws Exception {
        Path badFile = dir.resolve("bad.in");
        Files.writeString(badFile, "not_a_number\n");
        var badExp = new ValueExp(new StringValue(badFile.toString()));

        new OpenRFileStmt(badExp).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badExp, "x").execute(state));
    }

    // --- Rule S10: closeRFile ---

    @Test
    void closeRFileRemovesEntryFromFileTable() throws MyException {
        new OpenRFileStmt(fileExp).execute(state);
        assertTrue(state.getFileTable().isDefined(fileKey));

        new CloseRFileStmt(fileExp).execute(state);
        assertFalse(state.getFileTable().isDefined(fileKey));
    }

    @Test
    void closeRFileWhenFileIsNotOpenThrows() {
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(fileExp).execute(state));
    }

    @Test
    void closeRFileWithNonStringExpressionThrows() {
        var nonString = new ValueExp(new IntValue(1));
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(nonString).execute(state));
    }
}