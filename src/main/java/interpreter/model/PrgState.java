package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;    // A4 (§2.2): fifth runtime structure
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;   // A4 (§2.2): used by PrgState.of(...)
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.util.TreeMap;              // A4 (§2.2): sorts heap addresses in log output

public class PrgState {

    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private IDictionary<StringValue, BufferedReader> fileTable;
    // A4 (§1.2, §2.2): fifth runtime structure holding dynamically allocated values
    private IHeap heap;
    private final IStmt originalProgram;

    // A4 (§2.2): updated constructor accepting the Heap alongside the A3 structures
    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out, IDictionary<StringValue, BufferedReader> fileTable,
                    IHeap heap, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = program;
        exeStack.push(program);
    }

    // A4 (§2.2): factory creates a fresh MyHeap automatically so existing callers stay unchanged
    public static PrgState of(IStmt program) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new MyDictionary<>(),
                new MyHeap(),
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

    public IDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    // A4 (§2.2): getter for Heap used by expressions, heap statements, GC, and logging
    public IHeap getHeap() {
        return heap;
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

    public void setFileTable(IDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    // A4 (§2.2): setter for Heap
    public void setHeap(IHeap heap) {
        this.heap = heap;
    }

    public String toLogString() {
        var sb = new StringBuilder();
        sb.append("ExeStack:\n");
        exeStack.toList().forEach(s -> sb.append(s).append('\n'));
        sb.append("SymTable:\n");
        symTable.toMap().forEach((k, v) ->
                sb.append(k).append(" --> ").append(v).append('\n'));
        sb.append("Out:\n");
        out.toList().forEach(v -> sb.append(v).append('\n'));
        sb.append("FileTable:\n");
        fileTable.toMap().keySet().forEach(f -> sb.append(f.val()).append('\n'));
        // A4 (§2.2): fifth log section, sorted by address via TreeMap for deterministic logs
        sb.append("Heap:\n");
        new TreeMap<>(heap.getContent()).forEach((addr, val) ->
                sb.append(addr).append(" --> ").append(val).append('\n'));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\n"
                + "SymTable: " + symTable + "\n"
                + "Out: " + out + "\n"
                + "FileTable: " + fileTable + "\n"
                // A4 (§1.2): include Heap in console step display
                + "Heap: " + heap;
    }
}