package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

// A4 (§2.2): HashMap-backed heap with monotonically increasing 1-based addresses (0 = null)
public class MyHeap implements IHeap {
    private Map<Integer, IValue> heap = new HashMap<>();
    private int nextFree = 1; // address 0 means null

    @Override
    public int allocate(IValue value) {
        int address = nextFree++;
        heap.put(address, value);
        return address;
    }

    // A4 (§2.2, §2.7): missing heap address throws ADTException
    @Override
    public IValue get(int address) throws ADTException {
        if (!heap.containsKey(address)) {
            throw new ADTException("address " + address + " is not in the heap");
        }
        return heap.get(address);
    }

    // A4 (§2.2, §2.7): missing heap address throws ADTException
    @Override
    public void update(int address, IValue value) throws ADTException {
        if (!heap.containsKey(address)) {
            throw new ADTException("address " + address + " is not in the heap");
        }
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return Map.copyOf(heap);
    }

    @Override
    public void setContent(Map<Integer, IValue> content) {
        heap = new HashMap<>(content);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<Integer, IValue> e : new TreeMap<>(heap).entrySet()) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(e.getKey()).append(" -> ").append(e.getValue());
        }
        return sb.append("}").toString();
    }
}