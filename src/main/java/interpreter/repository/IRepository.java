package interpreter.repository;

import interpreter.exceptions.MyException; // A3 (§2.6): needed by logPrgStateExec()
import interpreter.model.PrgState;

public interface IRepository {

    PrgState getCrtPrg();

    // A3 (§2.6): appends the current program state snapshot to the log file
    void logPrgStateExec() throws MyException;
}