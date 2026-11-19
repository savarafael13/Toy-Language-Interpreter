package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyDictionary<K, V> implements IDictionary<K, V> {
    // LinkedHashMap is used so that toString() keeps insertion order
    private final Map<K, V> map = new LinkedHashMap<>();

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws ADTException {
        if (!map.containsKey(key)) {
            throw new ADTException("key " + key + " is not in the dict");
        }
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void update(K key, V value) throws ADTException {
        if (!map.containsKey(key)) {
            throw new ADTException("key " + key + " is not in the dict");
        }
        map.put(key, value);
    }

    // A3 (§2.3, §2.8): deletes key from the dictionary or throws ADTException if absent
    @Override
    public void remove(K key) throws ADTException {
        if (!map.containsKey(key)) {
            throw new ADTException("key " + key + " is not in the dict");
        }
        map.remove(key);
    }

    // A3 (§2.3): returns an unmodifiable copy so callers (like logging) cannot mutate state
    @Override
    public Map<K, V> toMap() {
        return Map.copyOf(map);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, V> e : map.entrySet()) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(e.getKey()).append(" -> ").append(e.getValue());
        }
        return sb.append("}").toString();
    }
}