package app.benchmark;

import app.core.AppLogger;
import app.datastructures.queue.ArrayQueue;

import java.util.Random;

public class QueueBenchmark {

    private static final String NAME = "ArrayQueue";
    private static volatile int sink = 0;

    public static void run(int[] sizes, int repetitions) {
        Random rand = new Random(42);
        for (int size : sizes) {
            AppLogger.getInstance().info("Queue benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runFor(size, repetitions, rand);
        }
    }

    private static void runFor(int size, int repetitions, Random rand) {
        ArrayQueue<Integer> queue = new ArrayQueue<>();

        // Warm up JIT before any measurement
        Benchmark.warmup(() -> { queue.enqueue(0); queue.dequeue(); });

        // enqueue — average per operation
        long totalEnq = Benchmark.measure(() -> {
            for (int i = 0; i < size; i++) queue.enqueue(rand.nextInt(1_000_000));
        });
        long perEnq = size > 0 ? totalEnq / size : 0;
        Benchmark.print("enqueue", perEnq);
        ResultCollector.add(NAME, "enqueue", size, perEnq);

        Benchmark.trackAverage(NAME, "front",   size, () -> sink ^= (Integer) queue.front(),    repetitions);
        Benchmark.trackAverage(NAME, "size",    size, () -> sink ^= queue.size(),               repetitions);
        Benchmark.trackAverage(NAME, "isEmpty", size, () -> sink ^= (queue.isEmpty() ? 1 : 0), repetitions);

        int target = (Integer) queue.front();
        Benchmark.trackAverage(NAME, "delete", size, () -> {
            queue.enqueue(target);
            queue.delete(target);
        }, repetitions);

        // dequeue — keep queue size stable during the average measurement
        Benchmark.trackAverage(NAME, "dequeue", size, () -> {
            sink ^= (Integer) queue.dequeue();
            queue.enqueue(rand.nextInt());
        }, repetitions);

        sink = 0;
    }
}
