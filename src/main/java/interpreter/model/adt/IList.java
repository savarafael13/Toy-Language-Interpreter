package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.List; // A3 (§2.3): needed for toList()

public interface IList<T> {
    void add(T v);
    T get(int index) throws ADTException;
    int size();

    // A3 (§2.3, §2.6): returns elements in insertion order for log files
    List<T> toList();
}