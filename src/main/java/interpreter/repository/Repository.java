package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {

    private final List<PrgState> prgList = new ArrayList<>();
    // A3 (§2.6): path to the log file passed when constructing the repository
    private final String logFilePath;

    // A3 (§2.6): updated constructor taking both the initial state and the log file path
    public Repository(PrgState state, String logFilePath) {
        prgList.add(state);
        this.logFilePath = logFilePath;
    }

    @Override
    public PrgState getCrtPrg() {
        return prgList.get(0);
    }

    // A3 (§2.6): opens the log file in append mode with try-with-resources so every
    // logged step is flushed immediately even if a later step throws an exception
    @Override
    public void logPrgStateExec() throws MyException {
        try (var logFile = new PrintWriter(
                new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println(getCrtPrg().toLogString());
        } catch (IOException e) {
            throw new RepositoryException("cannot write the log file " + logFilePath
                    + ": " + e.getMessage());
        }
    }
}