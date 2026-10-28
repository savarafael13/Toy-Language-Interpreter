package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyDictionary<K , V> implements IDictionary<K,V> {
// The lab comment says "wraps a HashMap". LinkedHashMap is used instead so
    // that toString() keeps insertion order: with HashMap, two variables would
    // print in an unpredictable order and the §3.3 trace would not be reproducible
    private final Map<K,V> map = new LinkedHashMap<>();
    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }
    @Override
    public V lookup(K key) throws ADTException{
        // VarExp is what translates "missing key" into "variable X is not defined" (E2
        if (!map.containsKey(key)){
            throw new ADTException("key " + key + "is not in the dict");

        }
        return map.get(key);
    }
    @Override
    public void put(K key , V value) {
        map.put(key , value );
    }
    @Override
    public void update ( K key , V value) throws ADTException{
        //SAME as put but refuses unknown keys
        //so s2 can report variable was not declared
        if(!map.containsKey(key)){
            throw new ADTException("key " + key + "is not in the dict");

        }
        map.put(key,value);
    }

    // shows the symtable as v-> 2 . map.tostring() would give v=2 so the arrow format has to be built by hand
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder("{");
        for (Map.Entry<K,V> e : map.entrySet()){
            if ( sb.length()>1){
                sb.append(", ");
            }
            sb.append(e.getKey()).append(" -> ").append(e.getValue());
        }
        return sb.append("}").toString();
    }

}
