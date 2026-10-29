package interpreter.model;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.IStack;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IValue;
//program state : one prgstat eper program / the three structures 3.1 plus original program
public class PrgState {
    private IStack<IStmt> exeStack; // statements still to execute , top =next
    private IDictionary<String,IValue> symTable; // declared variables -> current values
    private IList<IValue> out; // printed values in order
    private final IStmt originalProgram;//kept so it can be displayed
    public PrgState(IStack<IStmt> exeStack , IDictionary<String , IValue> symTable,IList<IValue> out , IStmt program){
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out=out;
        this.originalProgram = program;
        //initially exestack contains whole program , this is the only place program is pushed - not main/controller
        exeStack.push(program);
    }
    public IStack<IStmt> getExeStack(){
        return exeStack;
    }
    public IDictionary<String , IValue> getSymTable(){
        return  symTable;
    }
    public IList<IValue> getOut() {
        return out;
    }
    public IStmt getOriginalProgram() {
        return originalProgram;
    }
    public void setExeStack(IStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }
    public void setSymTable(IDictionary<String, IValue> symTable) {
        this.symTable = symTable;
    }
    public void setOut(IList<IValue> out) {
        this.out = out;
    }
    //used by controler to print the state after every step
    //delegates the per structure formatting to the adts mystacks prints top first mydict prinst (v->2} mylinst prints [2]
    @Override
    public String toString(){
        return "ExeStack: " + exeStack + "\n"
                + "Symtable " + symTable + "\n"
                + "out " +out;
    }
}
