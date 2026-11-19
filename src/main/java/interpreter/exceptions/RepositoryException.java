package interpreter.exceptions;

// A3 (§2.6, §2.8): interpreter exception thrown when opening or writing the log file fails
public class RepositoryException extends MyException {
    public RepositoryException(String message) {
        super(message);
    }
}