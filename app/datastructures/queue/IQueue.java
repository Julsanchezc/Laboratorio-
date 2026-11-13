package app.datastructures.queue;

/**
 * FIFO queue: the first element enqueued is the first one dequeued.
 *
 * @param <T> element type
 */
public interface IQueue<T> {

    /** Adds {@code element} at the back of the queue. */
    void enqueue(T element);

    /** Removes and returns the front element, or {@code null} if the queue is empty. */
    T dequeue();

    /** Returns the front element without removing it, or {@code null} if the queue is empty. */
    T front();

    boolean isEmpty();

    int size();

    /** Removes the first occurrence of {@code element}, keeping the order of the rest. O(n). */
    void delete(T element);
}
