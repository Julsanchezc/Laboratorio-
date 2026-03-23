package app.benchmark;

import app.core.AppLogger;
import app.datastructures.stack.ArrayStack;

import java.util.Random;

public class StackBenchmark {

    private static final String NAME = "ArrayStack";
    private static volatile int sink = 0;

    public static void run(int[] sizes, int repetitions) {
        Random rand = new Random(42);
        for (int size : sizes) {
            AppLogger.getInstance().info("Stack benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runFor(size, repetitions, rand);
        }
    }

    private static void runFor(int size, int repetitions, Random rand) {
        ArrayStack<Integer> stack = new ArrayStack<>();

        // Warm up JIT before any measurement
        Benchmark.warmup(() -> { stack.push(0); stack.pop(); });

        // push — average per operation
        long totalPush = Benchmark.measure(() -> {
            for (int i = 0; i < size; i++) stack.push(rand.nextInt(1_000_000));
        });
        long perPush = size > 0 ? totalPush / size : 0;
        Benchmark.print("push", perPush);
        ResultCollector.add(NAME, "push", size, perPush);

        Benchmark.trackAverage(NAME, "peek",    size, () -> sink ^= stack.peek(),             repetitions);
        Benchmark.trackAverage(NAME, "size",    size, () -> sink ^= stack.size(),             repetitions);
        Benchmark.trackAverage(NAME, "isEmpty", size, () -> sink ^= (stack.isEmpty() ? 1 : 0), repetitions);

        int target = stack.peek();
        Benchmark.trackAverage(NAME, "delete", size, () -> {
            stack.push(target);
            stack.delete(target);
        }, repetitions);

        // pop — keep stack size stable during the average measurement
        Benchmark.trackAverage(NAME, "pop", size, () -> {
            sink ^= stack.pop();
            stack.push(rand.nextInt());
        }, repetitions);

        sink = 0;
    }
}
