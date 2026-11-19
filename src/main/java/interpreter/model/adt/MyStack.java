package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List; // A3 (§2.3): needed for toList()

public class MyStack<T> implements IStack<T> {
    // ArrayDeque push/pop act on the head (LIFO)
    private final Deque<T> stack = new ArrayDeque<>();

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public T pop() throws ADTException {
        if (stack.isEmpty()) {
            throw new ADTException("pop from an empty stack");
        }
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    // A3 (§2.3): ArrayDeque iterates head-first, so List.copyOf preserves top-first order
    @Override
    public List<T> toList() {
        return List.copyOf(stack);
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}