package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.model.type.IntType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// A4 (§3.1): Step 2 tests for MyHeap sequential allocation and missing-address errors
class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateAssignsAddressesStartingFromOne() throws ADTException {
        int a1 = heap.allocate(new IntValue(20));
        int a2 = heap.allocate(new RefValue(1, new IntType()));
        int a3 = heap.allocate(new IntValue(30));

        assertEquals(1, a1);
        assertEquals(2, a2);
        assertEquals(3, a3);
        assertEquals(new IntValue(20), heap.get(1));
        assertEquals(new RefValue(1, new IntType()), heap.get(2));
        assertEquals(new IntValue(30), heap.get(3));
    }

    @Test
    void getOfMissingOrZeroAddressThrows() {
        assertThrows(ADTException.class, () -> heap.get(0));
        var ex = assertThrows(ADTException.class, () -> heap.get(1));
        assertTrue(ex.getMessage().contains("1"));
    }

    @Test
    void updateModifiesExistingAndThrowsOnMissing() throws ADTException {
        int addr = heap.allocate(new IntValue(20));
        heap.update(addr, new IntValue(30));
        assertEquals(new IntValue(30), heap.get(addr));

        assertThrows(ADTException.class, () -> heap.update(99, new IntValue(1)));
    }

    @Test
    void isDefinedGetContentAndSetContentWork() throws ADTException {
        assertFalse(heap.isDefined(1));
        int addr = heap.allocate(new IntValue(10));
        assertTrue(heap.isDefined(addr));

        var copy = heap.getContent();
        assertEquals(new IntValue(10), copy.get(addr));
        assertThrows(UnsupportedOperationException.class, () -> copy.put(2, new IntValue(5)));

        heap.setContent(Map.of(5, new IntValue(99)));
        assertFalse(heap.isDefined(1));
        assertEquals(new IntValue(99), heap.get(5));
    }
}