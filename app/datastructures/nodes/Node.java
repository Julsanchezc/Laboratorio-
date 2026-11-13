package app.datastructures.nodes;

/**
 * Node shared by all linked lists.
 *
 * <p>It has both {@code next} and {@code prev} so the same class serves singly
 * and doubly linked lists; singly linked lists simply never use {@code prev}.
 * Fields are public on purpose to keep the list code short and avoid getter
 * overhead in the benchmark.</p>
 */
public class Node {
    public double value;
    /** Next node, or {@code null} at the end of the list. */
    public Node next;
    /** Previous node (doubly linked lists only), or {@code null} at the beginning. */
    public Node prev;

    public Node(double value) {
        this.value = value;
    }
}
