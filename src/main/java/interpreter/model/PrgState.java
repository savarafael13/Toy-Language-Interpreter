package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary; // A3 (§2.3): used by PrgState.of(...)
import interpreter.model.adt.MyList;       // A3 (§2.3): used by PrgState.of(...)
import interpreter.model.adt.MyStack;      // A3 (§2.3): used by PrgState.of(...)
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue; // A3 (§1.2, §2.3): FileTable key type

import java.io.BufferedReader;             // A3 (§1.2, §2.3): FileTable value type

/**
 * The program state. Bundles the four runtime structures:
 *   - exeStack  : statements still to execute (top = next)
 *   - symTable  : declared variables and their current values
 *   - out       : values printed so far, in order
 *   - fileTable : A3 (§1.2) open file names mapped to their BufferedReaders
 */
public class PrgState {

    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    // A3 (§1.2, §2.3): fourth structure tracking currently open files
    private IDictionary<StringValue, BufferedReader> fileTable;
    private final IStmt originalProgram;

    // A3 (§2.3): updated constructor accepting the FileTable alongside the A2 structures
    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out, IDictionary<StringValue, BufferedReader> fileTable,
                    IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = program;
        exeStack.push(program);
    }

    // A3 (§2.3): static factory creating a PrgState with fresh empty structures
    public static PrgState of(IStmt program) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                program
        );
    }

    // getters

    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    // A3 (§2.3): getter for FileTable used by openRFile, readFile, closeRFile, and logging
    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    // setters

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    // A3 (§2.3): setter for FileTable
    public void setFileTable(IDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\n"
                + "SymTable: " + symTable + "\n"
                + "Out: " + out + "\n"
                // A3 (§1.2): include FileTable in console step display
                + "FileTable: " + fileTable;
    }
}