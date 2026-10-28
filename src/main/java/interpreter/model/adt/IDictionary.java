package interpreter.model.adt;
import interpreter.exceptions.ADTException;

public interface IDictionary<K , V>{
    //needed by assignstmt " not declared" and vardeclstmt "already declared"
    boolean isDefined(K key);
    //if key missing exception
    V lookup(K key) throws ADTException;
    // used by vardeclstmt to insert def value
    void put(K key , V value);
    //used by assignstmt seperate from put so "assign to undeclared var " can be reported
    void update( K key , V value) throws ADTException;
}
