package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import java.util.ArrayList;
import java.util.List;
public class MyList<T> implements IList<T> {
    // ArrayList keeps insertion order
    //  has no fixed size grows as neede
    private final List<T> list = new ArrayList<>();
    @Override
    public void add(T v) {
        list.add(v);              // appends at the end
    }
    @Override
    public T get(int index) throws ADTException {
        // "index out of bounds" as an ADTException case.

        if (index < 0 || index >= list.size()) {
            throw new ADTException("index " + index + " out of bounds");
        }
        return list.get(index);
    }
    @Override
    public int size() {
        return list.size();
    }
    //ArrayList.toString() gives {2, 18} style [2, 18] matches how Out is displayed
    @Override
    public String toString() {
        return list.toString();
    }
}
