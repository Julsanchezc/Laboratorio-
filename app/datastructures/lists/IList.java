package app.datastructures.lists;

import app.datastructures.nodes.Node;

/**
 * Common contract for every linked-list implementation in the lab.
 *
 * <p>All implementations store {@code double} values and share the same
 * operations, so the benchmark can measure them interchangeably. Complexity
 * depends on the implementation (with or without tail pointer, single or double
 * links); see each class for its own table.</p>
 */
public interface IList {

    /** Inserts {@code value} at the beginning of the list. */
    void pushFront(double value);

    /** Inserts {@code value} at the end of the list. */
    void pushBack(double value);

    /** Removes the first element. Does nothing if the list is empty. */
    void popFront();

    /** Removes the last element. Does nothing if the list is empty. */
    void popBack();

    /** Returns the first node holding {@code value}, or {@code null} if it is not found. */
    Node find(double value);

    /** Removes the first occurrence of {@code value}. Does nothing if it is not found. */
    void erase(double value);

    /** Inserts {@code value} right after the first node holding {@code target}, if it exists. */
    void addAfter(double target, double value);

    /** Inserts {@code value} right before the first node holding {@code target}, if it exists. */
    void addBefore(double target, double value);
}
