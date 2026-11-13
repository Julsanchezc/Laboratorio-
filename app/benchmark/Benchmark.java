package app.benchmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Timing utilities shared by every benchmark.
 *
 * <h2>How an operation is measured ({@link #trackOp})</h2>
 * <ol>
 *   <li>The structure is built with exactly N elements.</li>
 *   <li>The operation is timed in isolation; after each timed block an <em>untimed</em>
 *       restore step brings the structure back to size N, so every sample measures
 *       the same situation.</li>
 *   <li>This is repeated {@code samples} times and the <b>median</b> is reported,
 *       which ignores outliers caused by garbage collection or the operating system.</li>
 * </ol>
 *
 * <p>{@link System#nanoTime()} has limited resolution (100 ns on many Windows machines),
 * so a single O(1) call (~10 ns) cannot be timed alone. When one call is too fast to
 * measure, each timed block runs the operation once on each of several identical copies
 * of the structure (and, for operations that can be safely repeated, a few times in a
 * row while the size changes by at most 0.1 %). The block time is then divided by the
 * number of calls. Slow operations (O(n) on large N) are always timed one call at a time.</p>
 *
 * <p>Read-only operations (peek, size, isEmpty, front) use the same method with a
 * factory that returns the same structure and an empty restore step.</p>
 *
 * <p>Benchmarks run a full warm-up pass first (see {@link #setRecording}) so the JIT
 * compiler has already optimized the code before anything is recorded.</p>
 */
public final class Benchmark {

    /** An operation applied to a structure; {@code sample} lets it pick a different target per sample. */
    @FunctionalInterface
    public interface Op<T> {
        void apply(T structure, int sample);
    }

    /** Extra runs used to estimate how long one call takes; they use sample indices after the real ones. */
    public static final int CALIBRATION_RUNS = 3;

    /** A single call slower than this is timed alone (it is well above the timer resolution). */
    private static final long SLOW_CALL_NS = 20_000;

    /** Desired number of calls per timed block for fast operations. */
    private static final int TARGET_CALLS = 1_000;

    /** Memory guard: copies × N never exceeds this many elements. */
    private static final long MAX_ELEMENTS = 2_000_000;

    /** When false (warm-up pass), measurements are neither printed nor recorded. */
    private static boolean recording = true;

    private Benchmark() {}

    public static void setRecording(boolean enabled) {
        recording = enabled;
    }

    /** Prints a section header only during the recorded pass. */
    public static void header(String text) {
        if (recording) System.out.println(text);
    }

    // ── Measurement ───────────────────────────────────────────────────────────

    /**
     * Measures one operation on structures of size {@code size} and records the
     * median cost per call in nanoseconds.
     *
     * @param factory    builds a new structure holding exactly {@code size} elements
     * @param repeatable true if the operation can run several times in a row and be
     *                   undone by running {@code restore} the same number of times
     *                   (e.g. pushFront/popFront); false for one-shot operations
     *                   (e.g. erase of a specific value)
     * @param timed      the operation being measured
     * @param restore    untimed step that undoes {@code timed}
     */
    public static <T> long trackOp(String structure, String operation, int size, int samples,
                                   Supplier<T> factory, boolean repeatable,
                                   Op<T> timed, Op<T> restore) {
        List<T> copies = new ArrayList<>();
        copies.add(factory.get());

        // 1. Calibration: how long do one call and one restore take?
        long[] timedNs = new long[CALIBRATION_RUNS];
        long[] restoreNs = new long[CALIBRATION_RUNS];
        for (int i = 0; i < CALIBRATION_RUNS; i++) {
            T s = copies.get(0);
            long start = System.nanoTime();
            timed.apply(s, samples + i);
            timedNs[i] = System.nanoTime() - start;
            start = System.nanoTime();
            restore.apply(s, samples + i);
            restoreNs[i] = System.nanoTime() - start;
        }
        boolean slowCall = median(timedNs) >= SLOW_CALL_NS;
        boolean slowRestore = median(restoreNs) >= SLOW_CALL_NS;

        // 2. Plan: calls in a row per copy (batch), number of copies, and whether to restore.
        //    - Slow call: time it alone, one sample at a time.
        //    - Fast call with a cheap restore: batches of up to N/1000 calls, restored after each sample.
        //    - Fast call with an EXPENSIVE restore (e.g. pushBack on a singly linked list with
        //      tail: undoing it needs an O(n) popBack): skip the restore and let the structure
        //      grow, keeping the total change under 0.1 % of N across all samples.
        boolean useRestore = slowCall || !slowRestore || !repeatable;
        int batch;
        if (slowCall || !repeatable) {
            batch = 1;
        } else if (useRestore) {
            batch = Math.max(1, Math.min(TARGET_CALLS, size / 1_000));
        } else {
            batch = Math.max(1, Math.min(TARGET_CALLS, size / 1_000 / samples));
        }
        long maxCopies = Math.max(1, MAX_ELEMENTS / Math.max(1, size));
        int nCopies = slowCall ? 1 : (int) Math.min(maxCopies, Math.max(1, TARGET_CALLS / batch));
        while (copies.size() < nCopies) copies.add(factory.get());

        // 3. Samples: timed block, then (normally) an untimed restore.
        long[] perCall = new long[samples];
        for (int sample = 0; sample < samples; sample++) {
            long start = System.nanoTime();
            for (T s : copies) {
                for (int b = 0; b < batch; b++) timed.apply(s, sample);
            }
            long elapsed = System.nanoTime() - start;
            if (useRestore) {
                for (T s : copies) {
                    for (int b = 0; b < batch; b++) restore.apply(s, sample);
                }
            }
            perCall[sample] = elapsed / ((long) copies.size() * batch);
        }

        long result = median(perCall);
        record(structure, operation, size, result, "median");
        return result;
    }

    /**
     * Measures the amortized cost of an operation that is done many times in a row,
     * such as filling a structure with N elements (the occasional resize is spread over
     * all the insertions). {@code fill} builds and fills one new structure. For small N
     * several structures are filled per sample so the timed block is long enough to
     * measure; the median over {@code samples} is reported.
     */
    public static long trackAmortized(String structure, String operation, int size,
                                      int samples, Runnable fill) {
        int rounds = Math.max(1, TARGET_CALLS / Math.max(1, size));
        long[] perOp = new long[samples];
        for (int sample = 0; sample < samples; sample++) {
            long start = System.nanoTime();
            for (int r = 0; r < rounds; r++) fill.run();
            perOp[sample] = (System.nanoTime() - start) / ((long) rounds * Math.max(1, size));
        }
        long result = median(perOp);
        record(structure, operation, size, result, "amortized");
        return result;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    static long median(long[] values) {
        long[] sorted = values.clone();
        Arrays.sort(sorted);
        int mid = sorted.length / 2;
        return sorted.length % 2 == 1 ? sorted[mid] : (sorted[mid - 1] + sorted[mid]) / 2;
    }

    private static void record(String structure, String operation, int size, long ns, String kind) {
        if (!recording) return;
        System.out.printf("  %-26s %,d ns%n", operation + " (" + kind + "):", ns);
        ResultCollector.add(structure, operation, size, ns);
    }
}
