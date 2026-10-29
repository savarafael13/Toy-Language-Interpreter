package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    private IList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void addGetAndSizeWork() throws ADTException {
        list.add(1);
        list.add(2);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    void emptyListHasSizeZero() {
        assertEquals(0, list.size());
    }

    @Test
    void getAboveLastIndexThrows() {
        list.add(1);
        assertThrows(ADTException.class, () -> list.get(1));
    }

    @Test
    void getNegativeIndexThrows() {
        assertThrows(ADTException.class, () -> list.get(-1));
    }

    @Test
    void toStringKeepsInsertionOrder() {
        list.add(1);
        list.add(2);
        assertEquals("[1, 2]", list.toString());
    }
}