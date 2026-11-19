package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.StringValue;

import java.io.IOException;

// A3 (§2.4, Rule S10): closes an open BufferedReader and removes its entry from FileTable
public record CloseRFileStmt(IExp exp) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // A3 (Rule S10 error 1): file expression must evaluate to a StringValue
        if (!(exp.eval(state.getSymTable()) instanceof StringValue fileName))
            throw new StatementException("closeRFile: " + exp + " is not a string");

        var fileTable = state.getFileTable();
        // A3 (Rule S10 error 2): file must currently be registered in FileTable
        if (!fileTable.isDefined(fileName))
            throw new StatementException("closeRFile: " + fileName + " is not open");

        try {
            // A3 (§2.4, Rule S10): close the underlying reader first, then remove the key
            fileTable.lookup(fileName).close();
            fileTable.remove(fileName);
        } catch (IOException e) {
            // A3 (§2.4, Rule S10 error 3): convert Java IOException into StatementException
            throw new StatementException("closeRFile: error closing " + fileName + ": " + e.getMessage());
        }

        return state;
    }

    // A3 (§2.5, §2.6): infix representation matching log4.txt excerpt ("closeRFile(varf)")
    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}