package interpreter.repository;

import interpreter.model.PrgState;

/**
 * Repository layer (§1.3, §4.6).
 *
 * The Controller holds a reference of INTERFACE type (IRepository), not of
 * Repository, so the implementation can be replaced without touching the
 * controller - the same dependency-inversion idea used for the ADTs in PrgState.
 *
 * For A2 the interface has a single method. Later assignments add
 * getPrgList/setPrgList/logPrgStateExec when the repository has to hold the
 * states of several threads and write a log file (§4.6).
 */
public interface IRepository {

    /**
     * The program state the controller is currently executing.
     * Name is abbreviated ("crt" = current) to match the naming used in the
     * laboratory document and in the Controller example of §4.7.
     */
    PrgState getCrtPrg();
}