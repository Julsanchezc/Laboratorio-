package app.benchmark;

import app.core.AppLogger;
import app.datastructures.stack.ArrayStack;

import java.util.Random;
import java.util.function.Supplier;

/**
 * Benchmarks {@link ArrayStack}.
 *
 * <p>Random data lies in [0, 1 000 000); negative numbers are used as sentinels
 * so they can never collide with the data.</p>
 */
public class StackBenchmark {

    private static final String NAME = "ArrayStack";
    private static final int WARMUP_SIZE = 10_000;
    private static volatile int sink = 0;

    public static void run(int[] sizes, int samples) {
        Random rand = new Random(42);

        Benchmark.setRecording(false);
        for (int round = 0; round < 3; round++) runFor(WARMUP_SIZE, samples, rand);
        Benchmark.setRecording(true);

        for (int size : sizes) {
            AppLogger.getInstance().info("Stack benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runFor(size, samples, rand);
        }
    }

    private static void runFor(int size, int samples, Random rand) {
        int[] data = randomData(size, rand);   // generated outside the timer

        // push — fills a new structure with N elements; reports the amortized cost
        // per call (the occasional resize of the array is spread over all of them).
        Benchmark.trackAmortized(NAME, "push", size, samples, () -> {
            ArrayStack<Integer> fresh = new ArrayStack<>();
            for (int x : data) fresh.push(x);
        });

        // Read-only O(1) operations on a structure holding N elements.
        ArrayStack<Integer> stack = new ArrayStack<>();
        for (int x : data) stack.push(x);
        Benchmark.trackOp(NAME, "peek",    size, samples, () -> stack, true, (s, i) -> sink ^= s.peek(), (s, i) -> {});
        Benchmark.trackOp(NAME, "size",    size, samples, () -> stack, true, (s, i) -> sink ^= s.size(), (s, i) -> {});
        Benchmark.trackOp(NAME, "isEmpty", size, samples, () -> stack, true, (s, i) -> sink ^= (s.isEmpty() ? 1 : 0), (s, i) -> {});

        // pop — undone (untimed) by pushing an element back.
        Supplier<ArrayStack<Integer>> filled = () -> {
            ArrayStack<Integer> s = new ArrayStack<>();
            for (int x : data) s.push(x);
            return s;
        };
        Benchmark.trackOp(NAME, "pop", size, samples, filled, true,
                (s, i) -> sink ^= s.pop(), (s, i) -> s.push(1));

        // delete — worst case: the target is at the BOTTOM. delete() finds it at index 0,
        // but then every element above it must shift one position down → O(n).
        // Sample i deletes the sentinel -(i + 1). Sentinels are pushed before the data in
        // the same order they will be deleted (calibration runs first, then samples 0, 1, ...),
        // so the one being deleted is always at the bottom.
        Supplier<ArrayStack<Integer>> withSentinels = () -> {
            ArrayStack<Integer> s = new ArrayStack<>();
            for (int c = 0; c < Benchmark.CALIBRATION_RUNS; c++) s.push(-(samples + c + 1));
            for (int i = 0; i < samples; i++) s.push(-(i + 1));
            for (int x : data) s.push(x);
            return s;
        };
        Benchmark.trackOp(NAME, "delete", size, samples, withSentinels, false,
                (s, i) -> s.delete(-(i + 1)), (s, i) -> {});

        sink = 0;
    }

    private static int[] randomData(int size, Random rand) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) data[i] = rand.nextInt(1_000_000);
        return data;
    }
}
