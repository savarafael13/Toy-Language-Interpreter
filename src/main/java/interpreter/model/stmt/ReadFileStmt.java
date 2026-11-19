package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

// A3 (§2.4, Rule S9): reads one integer line from an open file in FileTable into an int variable
public record ReadFileStmt(IExp exp, String varName) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();

        // A3 (Rule S9 error 1): target variable must be declared in SymTable and have type int
        if (!symTable.isDefined(varName))
            throw new StatementException("readFile: variable " + varName + " is not declared");
        if (!symTable.lookup(varName).getType().equals(new IntType()))
            throw new StatementException("readFile: variable " + varName + " is not of type int");

        // A3 (Rule S9 error 2): file expression must evaluate to a StringValue
        if (!(exp.eval(symTable) instanceof StringValue fileName))
            throw new StatementException("readFile: " + exp + " is not a string");

        var fileTable = state.getFileTable();
        // A3 (Rule S9 error 3): file must already be open in FileTable
        if (!fileTable.isDefined(fileName))
            throw new StatementException("readFile: " + fileName + " is not open");

        try {
            BufferedReader reader = fileTable.lookup(fileName);
            String line = reader.readLine();
            // A3 (Rule S9, §2.4): null (end of file) yields 0; otherwise parse the stripped integer line
            int value = (line == null) ? 0 : Integer.parseInt(line.strip());
            symTable.update(varName, new IntValue(value));
        } catch (IOException | NumberFormatException e) {
            // A3 (§2.4, Rule S9 errors 4-5): convert Java I/O and parse errors into StatementException
            throw new StatementException("readFile: error reading " + fileName + ": " + e.getMessage());
        }

        return state;
    }

    // A3 (§2.5, §2.6): infix representation matching log4.txt excerpt ("readFile(varf, varc)")
    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}