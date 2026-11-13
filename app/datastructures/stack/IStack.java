package app.datastructures.stack;

/**
 * LIFO stack: the last element pushed is the first one popped.
 *
 * @param <T> element type
 */
public interface IStack<T> {

    /** Pushes {@code element} on top of the stack. */
    void push(T element);

    /** Removes and returns the top element, or {@code null} if the stack is empty. */
    T pop();

    /** Returns the top element without removing it, or {@code null} if the stack is empty. */
    T peek();

    boolean isEmpty();

    int size();

    /** Removes the first occurrence of {@code element}, keeping the order of the rest. O(n). */
    void delete(T element);
}
