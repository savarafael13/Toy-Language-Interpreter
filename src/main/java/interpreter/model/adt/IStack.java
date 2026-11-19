package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.List; // A3 (§2.3): needed for toList()

public interface IStack<T> {
    void push(T v);
    T pop() throws ADTException;
    boolean isEmpty();

    // A3 (§2.3, §2.6): returns elements with top of the stack first for log files
    List<T> toList();
}