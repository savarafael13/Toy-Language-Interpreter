package interpreter.model.adt;
import interpreter.exceptions.ADTException;
import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<T> implements IStack<T> {
    //array deque  push/pop act on the head
    private final Deque<T> stack = new ArrayDeque<>();
    @Override
    public void push(T v) {
        stack.push(v);
    }
    @Override
    public T pop() throws ADTException {
        // 4.4 operations that cant be performed throw exp
        if(stack.isEmpty()){
            throw new ADTException("pop from an empty stack");
        }
        return stack.pop(); // removes and returnes head = LIFO
    }
    @Override
    public boolean isEmpty(){
        return stack.isEmpty();
    }
    // arraydeque.tostring prints head first - override is not needed - but dont repace the deque with an array list whose order would be reversed
    @Override
    public String toString(){
        return stack.toString();
    }
}
