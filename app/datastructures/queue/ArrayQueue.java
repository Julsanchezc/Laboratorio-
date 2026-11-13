package app.datastructures.queue;

import java.util.Objects;

/**
 * Queue implemented with a circular dynamic array.
 *
 * The circular array avoids shifting elements on dequeue by maintaining
 * two pointers (head, tail) that wrap around using modular arithmetic.
 * When the array is full it doubles in capacity (amortized O(1) enqueue).
 *
 * Complexity:
 *   enqueue  O(1) amortized
 *   dequeue  O(1)
 *   front    O(1)
 *   isEmpty  O(1)
 *   size     O(1)
 *   delete   O(n)
 */
public class ArrayQueue<T> implements IQueue<T> {

    private static final int INITIAL_CAPACITY = 10;

    private Object[] elements;
    private int head;   // index of the front element
    private int tail;   // index where the next element will be inserted
    private int count;

    public ArrayQueue() {
        elements = new Object[INITIAL_CAPACITY];
        head  = 0;
        tail  = 0;
        count = 0;
    }

    /** Inserts an element at the back of the queue. O(1) amortized. */
    @Override
    public void enqueue(T element) {
        if (count == elements.length) resize();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        count++;
    }

    /** Removes and returns the front element. Returns null if empty. O(1). */
    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) return null;
        T element = (T) elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        count--;
        return element;
    }

    /** Returns the front element without removing it. Returns null if empty. O(1). */
    @Override
    @SuppressWarnings("unchecked")
    public T front() {
        if (isEmpty()) return null;
        return (T) elements[head];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    /**
     * Removes the first occurrence of element. O(n).
     * After the match, remaining elements are shifted forward circularly.
     */
    @Override
    public void delete(T element) {
        for (int i = 0; i < count; i++) {
            int idx = (head + i) % elements.length;
            if (Objects.equals(elements[idx], element)) {
                // Shift every subsequent element one position forward
                for (int j = i; j < count - 1; j++) {
                    int curr = (head + j)     % elements.length;
                    int next = (head + j + 1) % elements.length;
                    elements[curr] = elements[next];
                }
                tail = (tail - 1 + elements.length) % elements.length;
                elements[tail] = null;
                count--;
                return;
            }
        }
    }

    /**
     * Doubles capacity and linearises the circular layout so head == 0.
     * This keeps future index arithmetic correct after the resize.
     */
    private void resize() {
        Object[] newElements = new Object[elements.length * 2];
        for (int i = 0; i < count; i++) {
            newElements[i] = elements[(head + i) % elements.length];
        }
        elements = newElements;
        head = 0;
        tail = count;
    }
}
