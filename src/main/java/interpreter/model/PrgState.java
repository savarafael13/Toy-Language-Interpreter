package interpreter.model;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;

/**
 * The program state (§3.1, §4.5). One PrgState per executed program.
 *
 * It bundles the three structures of §3.1:
 *   - exeStack : the statements still to execute; the top is executed next
 *   - symTable : the declared variables and their current values
 *   - out      : the values printed so far, in order
 * plus the original program, kept so it can be displayed or re-run later (§4.5 note).
 */
public class PrgState {

    private IStack<IStmt> exeStack;
    private IDictionary<String, IValue> symTable;
    private IList<IValue> out;
    private final IStmt originalProgram;

    /**
     * The three structures are passed in already created (dependency injection),
     * so the ADT implementations can be swapped without changing PrgState.
     *
     * §3.1: "Initially: ExeStack contains the whole program; SymTable and Out are empty."
     * The push below is what guarantees the first part - and it must happen ONLY here.
     * If Main or Controller pushed the program as well, every program would execute twice.
     */
    public PrgState(IStack<IStmt> exeStack, IDictionary<String, IValue> symTable,
                    IList<IValue> out, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program;
        exeStack.push(program);
    }

    // ---------- getters: used by every statement's execute(...) ----------

    /** Statements still to execute; top = next (CompStmt/IfStmt push here). */
    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    /** Declared variables -> current values (VarDeclStmt writes, VarExp reads). */
    public IDictionary<String, IValue> getSymTable() {
        return symTable;
    }

    /** Values printed so far, in order (PrintStmt appends). */
    public IList<IValue> getOut() {
        return out;
    }

    /** final + no setter: the original program never changes during execution. */
    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    // ---------- setters: the three structures can be replaced ----------

    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    /**
     * Displayed by Controller.display after every step (§3.3, §4.7).
     * The per-structure formatting is delegated to the ADTs:
     *   MyStack    -> [int v, (v = 2; print(v))]   (top first)
     *   MyDictionary -> {v -> 0}                   (arrow format, §3.3)
     *   MyList     -> [2]
     * That is why MyDictionary overrides toString and the other two do not need to.
     */
    @Override
    public String toString() {
        return "ExeStack: " + exeStack + "\n"
                + "SymTable: " + symTable + "\n"
                + "Out: " + out;
    }
}