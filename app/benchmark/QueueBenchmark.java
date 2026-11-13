package app.benchmark;

import app.core.AppLogger;
import app.datastructures.queue.ArrayQueue;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Benchmarks {@link ArrayQueue}.
 *
 * <p>Random data lies in [0, 1 000 000); negative numbers are used as sentinels
 * so they can never collide with the data.</p>
 */
public class QueueBenchmark {

    private static final String NAME = "ArrayQueue";
    private static final int WARMUP_SIZE = 10_000;
    private static volatile int sink = 0;

    public static void run(int[] sizes, int samples) {
        Random rand = new Random(42);

        Benchmark.setRecording(false);
        for (int round = 0; round < 3; round++) runFor(WARMUP_SIZE, samples, rand);
        Benchmark.setRecording(true);

        for (int size : sizes) {
            AppLogger.getInstance().info("Queue benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runFor(size, samples, rand);
        }
    }

    private static void runFor(int size, int samples, Random rand) {
        int[] data = randomData(size, rand);   // generated outside the timer

        // enqueue — fills a new structure with N elements; reports the amortized cost
        // per call (the occasional resize of the array is spread over all of them).
        Benchmark.trackAmortized(NAME, "enqueue", size, samples, () -> {
            ArrayQueue<Integer> fresh = new ArrayQueue<>();
            for (int x : data) fresh.enqueue(x);
        });

        // Read-only O(1) operations on a structure holding N elements.
        ArrayQueue<Integer> queue = new ArrayQueue<>();
        for (int x : data) queue.enqueue(x);
        Benchmark.trackOp(NAME, "front",   size, samples, () -> queue, true, (s, i) -> sink ^= s.front(), (s, i) -> {});
        Benchmark.trackOp(NAME, "size",    size, samples, () -> queue, true, (s, i) -> sink ^= s.size(), (s, i) -> {});
        Benchmark.trackOp(NAME, "isEmpty", size, samples, () -> queue, true, (s, i) -> sink ^= (s.isEmpty() ? 1 : 0), (s, i) -> {});

        // dequeue — undone (untimed) by enqueuing an element back.
        Supplier<ArrayQueue<Integer>> filled = () -> {
            ArrayQueue<Integer> q = new ArrayQueue<>();
            for (int x : data) q.enqueue(x);
            return q;
        };
        Benchmark.trackOp(NAME, "dequeue", size, samples, filled, true,
                (q, i) -> sink ^= q.dequeue(), (q, i) -> q.enqueue(1));

        // delete — worst case: the target is at the BACK, so delete() must scan all
        // N elements from the front before finding it → O(n).
        // One sentinel per sample (-1, -2, ...) is enqueued after the data.
        int sentinels = samples + Benchmark.CALIBRATION_RUNS;
        Supplier<ArrayQueue<Integer>> withSentinels = () -> {
            ArrayQueue<Integer> q = new ArrayQueue<>();
            for (int x : data) q.enqueue(x);
            for (int i = 1; i <= sentinels; i++) q.enqueue(-i);
            return q;
        };
        Benchmark.trackOp(NAME, "delete", size, samples, withSentinels, false,
                (q, i) -> q.delete(-(i + 1)), (q, i) -> {});

        sink = 0;
    }

    private static int[] randomData(int size, Random rand) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) data[i] = rand.nextInt(1_000_000);
        return data;
    }
}
