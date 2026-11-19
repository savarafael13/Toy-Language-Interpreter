package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.Map; // A3 (§2.3): needed for toMap()

public interface IDictionary<K, V> {
    // needed by assignstmt "not declared" and vardeclstmt "already declared"
    boolean isDefined(K key);

    // if key missing exception
    V lookup(K key) throws ADTException;

    // used by vardeclstmt to insert default value
    void put(K key, V value);

    // used by assignstmt separate from put so "assign to undeclared var" can be reported
    void update(K key, V value) throws ADTException;

    // A3 (§2.3, Rule S10): used by closeRFile to remove an entry; missing key -> ADTException
    void remove(K key) throws ADTException;

    // A3 (§2.3, §2.6): read-only snapshot (Map.copyOf) used by PrgState.toLogString()
    Map<K, V> toMap();
}