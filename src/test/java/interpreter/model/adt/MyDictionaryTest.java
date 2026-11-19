package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private IDictionary<String, Integer> dict;

    @BeforeEach
    void setUp() {
        dict = new MyDictionary<>();
    }

    @Test
    void putThenLookupReturnsValue() throws ADTException {
        dict.put("a", 2);
        assertEquals(2, dict.lookup("a"));
    }

    @Test
    void updateChangesExistingValue() throws ADTException {
        dict.put("a", 2);
        dict.update("a", 5);
        assertEquals(5, dict.lookup("a"));
    }

    @Test
    void lookupOfMissingKeyThrows() {
        var ex = assertThrows(ADTException.class, () -> dict.lookup("x"));
        assertTrue(ex.getMessage().contains("x"));
    }

    @Test
    void updateOfMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.update("x", 1));
    }

    @Test
    void isDefinedReflectsPresence() {
        assertFalse(dict.isDefined("a"));
        dict.put("a", 2);
        assertTrue(dict.isDefined("a"));
    }

    @Test
    void toStringUsesArrowFormat() {
        dict.put("v", 2);
        assertEquals("{v -> 2}", dict.toString());
    }

    // A3 (§2.3): tests for remove() and toMap()
    @Test
    void removeDeletesExistingKeyAndThrowsWhenMissing() throws ADTException {
        dict.put("a", 10);
        dict.remove("a");
        assertFalse(dict.isDefined("a"));
        assertThrows(ADTException.class, () -> dict.remove("a"));
    }

    @Test
    void toMapReturnsUnmodifiableCopy() {
        dict.put("a", 1);
        var copy = dict.toMap();
        assertEquals(1, copy.get("a"));
        assertThrows(UnsupportedOperationException.class, () -> copy.put("b", 2));
    }
}