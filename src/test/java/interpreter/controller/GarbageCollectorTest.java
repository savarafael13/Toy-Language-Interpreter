package interpreter.controller;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// A4 (§3.1, §3.2): Step 6 tests for unsafe vs. safe garbage collection and transitive heap reachability
class GarbageCollectorTest {

    // The state of Ex9 after new(v, 30): v -> (3, int), a -> (2, Ref(int))
    private final List<IValue> symTable = List.of(
            new RefValue(3, new IntType()),
            new RefValue(2, new RefType(new IntType()))
    );

    private final Map<Integer, IValue> heap = Map.of(
            1, new IntValue(20),
            2, new RefValue(1, new IntType()),
            3, new IntValue(30),
            4, new IntValue(7)
    );

    @Test
    void keepsTheLocationsReachableThroughTheHeap() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);
        // 4 is garbage; 1 is reachable indirectly through 2 -> (1, int)
        assertEquals(Set.of(1, 2, 3), kept.keySet());
    }

    @Test
    void unsafeCollectorDropsIndirectlyReachableAddress() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, heap);
        assertEquals(Set.of(2, 3), kept.keySet());
        assertFalse(kept.containsKey(1));
    }

    @Test
    void reachableAddressesFollowsMultiHopChainsAndIgnoresNull() {
        Map<Integer, IValue> multiHopHeap = Map.of(
                1, new IntValue(99),
                2, new RefValue(1, new IntType()),
                3, new RefValue(2, new RefType(new IntType())),
                4, new IntValue(555)
        );
        Set<Integer> roots = Set.of(0, 3); // 0 is null, 3 points to 2 which points to 1
        assertEquals(Set.of(0, 1, 2, 3), GarbageCollector.reachableAddresses(roots, multiHopHeap));

        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, multiHopHeap);
        assertEquals(Set.of(1, 2, 3), kept.keySet());
    }
}