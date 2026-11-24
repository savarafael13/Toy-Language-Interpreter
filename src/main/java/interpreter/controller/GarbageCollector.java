package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// A4 (§2.6): stream-based garbage collector removing unreachable heap addresses
public final class GarbageCollector {

    private GarbageCollector() {
    }

    /** The addresses that occur directly in the RefValues of the SymTable. */
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).address())
                .collect(Collectors.toSet());
    }

    /** UNSAFE: keeps only the heap entries referred to directly from the SymTable. */
    public static Map<Integer, IValue> unsafeGarbageCollector(Set<Integer> symTableAddr,
                                                              Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // A4 (§2.6): fixed-point reachability traversal from SymTable roots through nested RefValues in the heap
    public static Set<Integer> reachableAddresses(Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> reachable = new HashSet<>(roots);
        boolean changed = true;
        while (changed) {
            Set<Integer> next = reachable.stream()
                    .map(heap::get)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).address())
                    .collect(Collectors.toSet());
            changed = reachable.addAll(next);
        }
        return reachable;
    }

    // A4 (§2.6): SAFE collector keeping all heap entries reachable directly or transitively from SymTable
    public static Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> reachable = reachableAddresses(symTableAddr, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}