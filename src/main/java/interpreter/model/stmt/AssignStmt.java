package interpreter.model.stmt;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
public record AssignStmt(String id , IExp exp) implements IStmt {
    var symTable = state.getSymTable
}
