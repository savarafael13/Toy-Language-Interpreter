package interpreter.repository;

import interpreter.model.PrgState;

import java.util.ArrayList;
import java.util.List;

/**
 * §4.6: "Class Repository implements it and stores the program states in a
 * List<PrgState> - later the repository will hold the states of several threads."
 *
 * Today the list always has exactly one element, but it is a List from the
 * start so that the multi-threaded assignment later only adds methods, and
 * does not force a change of the field type.
 */
public class Repository implements IRepository {

    private final List<PrgState> prgList = new ArrayList<>();

    /**
     * The state is handed to the constructor: by the time a Repository exists,
     * the PrgState has already been built (and therefore already has the whole
     * program pushed onto its ExeStack - see PrgState's constructor).
     */
    public Repository(PrgState state) {
        prgList.add(state);
    }

    /**
     * Returns the current (only) program state.
     * Not a copy: Controller mutates this very object, step by step, and those
     * mutations are what the view displays and the tests assert on.
     */
    @Override
    public PrgState getCrtPrg() {
        return prgList.get(0);
    }
}