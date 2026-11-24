package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        IStack<IStmt> stk = state.getExeStack();
        if (stk.isEmpty()) {
            throw new StatementException("prgstate stack is empty");
        }
        IStmt crtStmt = stk.pop();
        return crtStmt.execute(state);
    }

    // A4 (§2.6): runs safeGarbageCollector after each step and logs state before and after collection
    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        while (!prg.getExeStack().isEmpty()) {
            repo.logPrgStateExec();
            oneStep(prg);
            repo.logPrgStateExec();
            prg.getHeap().setContent(GarbageCollector.safeGarbageCollector(
                    GarbageCollector.getAddrFromSymTable(prg.getSymTable().toMap().values()),
                    prg.getHeap().getContent()));
            repo.logPrgStateExec();
            display(prg);
        }
    }

    private void display(PrgState state) {
        if (displayFlag) {
            System.out.println(state);
            System.out.println("----------------------------");
        }
    }
}