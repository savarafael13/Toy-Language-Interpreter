package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

/**
 * Controller layer (§4.7).
 *
 * Holds the repository as INTERFACE type, so the storage implementation can be
 * replaced without changing this class.
 *
 * Note the constructor shape: Controller(IRepository, boolean displayFlag).
 * The display flag is what §4.7 means by "display(prg) // only if the display
 * flag is on" - the tests pass false so that running them does not spam stdout,
 * the view passes true so the user sees every intermediate state.
 */
public class Controller {

    private final IRepository repo;
    private boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public void setDisplayFlag(boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    /**
     * One-step execution: pop the top statement of the ExeStack and execute it
     * (that is exactly one rule out of S1-S6).
     *
     * S7: "a further step after execution ended is an error:
     * 'execution stack is empty'" - hence the explicit check. Without it, the
     * ADTException from MyStack.pop would surface instead of the
     * StatementException that §4.10 assigns to this situation.
     *
     * Returns the state (same object in A2) because later assignments use the
     * return value for thread interleaving.
     */
    public PrgState oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty())
            throw new StatementException("program state stack is empty");
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    /**
     * Complete execution: repeat steps until the ExeStack is empty (S7).
     * Displays the state before the first step and after every step, so the
     * trace starts with the whole program on the stack - as in the §3.3 table.
     */
    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        display(prg);
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            display(prg);
        }
    }

    /** §4.7: prints the program state after each step when the display flag is set. */
    private void display(PrgState state) {
        if (displayFlag) {
            System.out.println(state);
            System.out.println("----------------------------");
        }
    }
}