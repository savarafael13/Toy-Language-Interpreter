package interpreter.model.stmt;

import interpreter.model.PrgState;

public record NopStmt() implements IStmt {
    @Override
    public PrgState execute(PrgState state) {
        return state;              // S6: nothing changes
    }

    @Override
    public String toString() {
        return "nop";
    }
}