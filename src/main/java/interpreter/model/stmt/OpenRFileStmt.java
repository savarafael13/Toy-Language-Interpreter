package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// A3 (§2.4, Rule S8): opens a text file for reading and registers its BufferedReader in FileTable
public record OpenRFileStmt(IExp exp) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // A3 (Rule S8 error 1): file path expression must evaluate to a StringValue
        if (!(exp.eval(state.getSymTable()) instanceof StringValue fileName))
            throw new StatementException("openRFile: " + exp + " is not a string");

        var fileTable = state.getFileTable();
        // A3 (Rule S8 error 2): prevent opening the same file twice simultaneously
        if (fileTable.isDefined(fileName))
            throw new StatementException("openRFile: " + fileName + " is already open");

        try {
            // A3 (§2.4): intentionally no try-with-resources — the reader must stay open
            // across steps until ReadFileStmt reads from it and CloseRFileStmt closes it
            var reader = new BufferedReader(new FileReader(fileName.val()));
            fileTable.put(fileName, reader);
        } catch (IOException e) {
            // A3 (§2.4, Rule S8 error 3): stop Java IOException at the model boundary
            throw new StatementException("openRFile: cannot open " + fileName + ": " + e.getMessage());
        }

        return state;
    }

    // A3 (§2.4, §2.5): infix representation for logs and menus
    @Override
    public String toString() {
        return "openRFile(" + exp + ")";
    }
}