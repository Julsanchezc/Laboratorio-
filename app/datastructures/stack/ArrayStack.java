package app.datastructures.stack;

import java.util.Objects;

/**
 * Array-based generic stack with dynamic resizing.
 * push O(1) amortized | pop O(1) | peek O(1) | size O(1)
 * isEmpty O(1) | delete O(n)
 */
public class ArrayStack<T> implements IStack<T> {

    private static final int INITIAL_CAPACITY = 10;

    private Object[] elements;
    private int count;

    public ArrayStack() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.count = 0;
    }

    @Override
    public void push(T element) {
        if (count == elements.length) resize();
        elements[count++] = element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) return null;
        T element = (T) elements[--count];
        elements[count] = null;
        return element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) return null;
        return (T) elements[count - 1];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void delete(T element) {
        for (int i = 0; i < count; i++) {
            if (Objects.equals(elements[i], element)) {
                System.arraycopy(elements, i + 1, elements, i, count - i - 1);
                elements[--count] = null;
                return;
            }
        }
    }

    private void resize() {
        Object[] newElements = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, count);
        elements = newElements;
    }
}
