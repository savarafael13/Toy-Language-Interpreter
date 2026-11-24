package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.Map;

// A4 (§2.2): heap interface mapping 1-based integer addresses to dynamically allocated IValues
public interface IHeap {
    // A4 (§2.2): stores value at the next free address (1, 2, 3...) and returns that address
    int allocate(IValue value);

    // A4 (§2.2): returns the value stored at address, or throws ADTException if missing
    IValue get(int address) throws ADTException;

    // A4 (§2.2): overwrites an existing heap location, or throws ADTException if missing
    void update(int address, IValue value) throws ADTException;

    boolean isDefined(int address);

    // A4 (§2.2): returns an unmodifiable snapshot of the heap
    Map<Integer, IValue> getContent();

    // A4 (§2.2, §2.6): replaces heap contents after garbage collection
    void setContent(Map<Integer, IValue> content);
}