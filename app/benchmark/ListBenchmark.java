package app.benchmark;

import app.core.AppLogger;
import app.datastructures.lists.*;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Benchmarks the four linked-list implementations.
 *
 * <p>Every operation is measured on lists that already hold exactly N random values
 * (see {@link Benchmark#trackOp}), so the result is the real cost of one call at
 * size N and O(1) and O(n) behaviours are clearly separated.</p>
 *
 * <p>Random data lies in [0, 1). The sentinels {@link #TARGET} and {@link #NEW_VALUE}
 * are outside that range, so searches always hit exactly the intended node.</p>
 */
public class ListBenchmark {

    /** Size used for the unrecorded warm-up pass that lets the JIT compile the code. */
    private static final int WARMUP_SIZE = 10_000;

    /** Value placed at the END of the list: worst case for find, erase, addBefore and addAfter. */
    private static final double TARGET = 2.0;

    /** Value inserted by addBefore/addAfter and removed again by the restore step. */
    private static final double NEW_VALUE = -1.0;

    private static volatile double sink = 0;

    public static void run(int[] sizes, int samples) {
        Random rand = new Random(42);

        // Warm-up: run everything on a medium list without recording anything.
        Benchmark.setRecording(false);
        for (int round = 0; round < 3; round++) runAll(WARMUP_SIZE, samples, rand);
        Benchmark.setRecording(true);

        for (int size : sizes) {
            AppLogger.getInstance().info("List benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runAll(size, samples, rand);
        }
    }

    private static void runAll(int size, int samples, Random rand) {
        runFor("SinglyLinkedList",         SinglyLinkedList::new,         size, samples, rand);
        runFor("SinglyLinkedListWithTail", SinglyLinkedListWithTail::new, size, samples, rand);
        runFor("DoublyLinkedList",         DoublyLinkedList::new,         size, samples, rand);
        runFor("DoublyLinkedListWithTail", DoublyLinkedListWithTail::new, size, samples, rand);
    }

    private static void runFor(String name, Supplier<IList> impl, int size, int samples, Random rand) {
        Benchmark.header("\n  [" + name + "]");

        // Lists with N random values.
        Supplier<IList> plain = () -> populate(impl.get(), size, rand);
        // Lists with N random values followed by TARGET at the very end.
        Supplier<IList> withTarget = () -> {
            IList l = populate(impl.get(), size, rand);
            l.pushBack(TARGET);
            return l;
        };
        double v = 0.5;

        // Insertions and removals at both ends (each one undone by its opposite, untimed).
        Benchmark.trackOp(name, "pushFront", size, samples, plain, true,
                (l, s) -> l.pushFront(v), (l, s) -> l.popFront());
        Benchmark.trackOp(name, "pushBack", size, samples, plain, true,
                (l, s) -> l.pushBack(v), (l, s) -> l.popBack());
        Benchmark.trackOp(name, "popFront", size, samples, plain, true,
                (l, s) -> l.popFront(), (l, s) -> l.pushFront(v));
        Benchmark.trackOp(name, "popBack", size, samples, plain, true,
                (l, s) -> l.popBack(), (l, s) -> l.pushBack(v));

        // Search-based operations: TARGET is the last node → full traversal.
        Benchmark.trackOp(name, "find", size, samples, withTarget, true,
                (l, s) -> sink += (l.find(TARGET) != null ? 1 : 0), (l, s) -> {});
        Benchmark.trackOp(name, "erase", size, samples, withTarget, false,
                (l, s) -> l.erase(TARGET), (l, s) -> l.pushBack(TARGET));
        Benchmark.trackOp(name, "addBefore", size, samples, withTarget, false,
                (l, s) -> l.addBefore(TARGET, NEW_VALUE), (l, s) -> l.erase(NEW_VALUE));
        Benchmark.trackOp(name, "addAfter", size, samples, withTarget, false,
                (l, s) -> l.addAfter(TARGET, NEW_VALUE), (l, s) -> l.erase(NEW_VALUE));

        sink = 0;
    }

    /**
     * Fills the list with {@code size} random values using pushFront, which is O(1)
     * in every implementation, so populating never becomes O(n²).
     */
    private static IList populate(IList list, int size, Random rand) {
        for (int i = 0; i < size; i++) list.pushFront(rand.nextDouble());
        return list;
    }
}
