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
}