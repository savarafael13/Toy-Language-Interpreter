package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;

// A4 (§2.5, Rule S13): repeats body while cond evaluates to true by re-pushing itself onto ExeStack
public record WhileStmt(IExp cond, IStmt body) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // A4 (Rule S13, §2.7): condition expression must evaluate to a BoolValue
        if (!(cond.eval(state.getSymTable(), state.getHeap()) instanceof BoolValue(boolean isTrue))) {
            throw new StatementException("condition " + cond + " is not a boolean");
        }

        // A4 (Rule S13): push WhileStmt first so body sits on top of ExeStack and executes next
        if (isTrue) {
            var stack = state.getExeStack();
            stack.push(this);
            stack.push(body);
        }

        return state;
    }

    // A4 (§2.5): parenthesized infix representation for logs and menus
    @Override
    public String toString() {
        return "(while (" + cond + ") " + body + ")";
    }
}