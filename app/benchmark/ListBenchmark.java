package app.benchmark;

import app.core.AppLogger;
import app.datastructures.lists.*;

import java.util.Random;
import java.util.function.Supplier;

public class ListBenchmark {

    private static volatile double sink = 0;

    public static void run(int[] sizes, int repetitions) {
        Random rand = new Random(42);
        for (int size : sizes) {
            AppLogger.getInstance().info("List benchmark — size: " + size);
            System.out.println("\n=== SIZE: " + size + " ===");
            runFor("SinglyLinkedList",         SinglyLinkedList::new,         size, repetitions, rand);
            runFor("SinglyLinkedListWithTail", SinglyLinkedListWithTail::new, size, repetitions, rand);
            runFor("DoublyLinkedList",         DoublyLinkedList::new,         size, repetitions, rand);
            runFor("DoublyLinkedListWithTail", DoublyLinkedListWithTail::new, size, repetitions, rand);
        }
    }

    private static void runFor(String name, Supplier<IList> factory,
                                int size, int repetitions, Random rand) {
        System.out.println("\n  [" + name + "]");

        // pushFront and pushBack are measured for N operations then divided by N
        // so the reported value is average nanoseconds per operation, comparable
        // with all other single-operation measurements.

        // Warm up JIT before any measurement
        IList warmupList = factory.get();
        Benchmark.warmup(() -> { warmupList.pushFront(rand.nextDouble()); warmupList.popFront(); });

        IList l1 = factory.get();
        long totalPF = Benchmark.measure(() -> {
            for (int i = 0; i < size; i++) l1.pushFront(rand.nextDouble());
        });
        long perPF = size > 0 ? totalPF / size : 0;
        Benchmark.print("pushFront", perPF);
        ResultCollector.add(name, "pushFront", size, perPF);

        IList l2 = factory.get();
        long totalPB = Benchmark.measure(() -> {
            for (int i = 0; i < size; i++) l2.pushBack(rand.nextDouble());
        });
        long perPB = size > 0 ? totalPB / size : 0;
        Benchmark.print("pushBack", perPB);
        ResultCollector.add(name, "pushBack", size, perPB);

        IList l3 = populate(factory.get(), size, rand);
        Benchmark.track(name, "popFront", size, l3::popFront);

        IList l4 = populate(factory.get(), size, rand);
        Benchmark.track(name, "popBack", size, l4::popBack);

        IList l5 = populate(factory.get(), size, rand);
        double t1 = rand.nextDouble();
        l5.pushBack(t1);
        Benchmark.trackAverage(name, "find", size,
                () -> sink += (l5.find(t1) != null ? 1 : 0), repetitions);

        IList l6 = populate(factory.get(), size, rand);
        double t2 = rand.nextDouble();
        l6.pushBack(t2);
        Benchmark.track(name, "erase", size, () -> l6.erase(t2));

        IList l7 = populate(factory.get(), size, rand);
        double t3 = rand.nextDouble();
        l7.pushBack(t3);
        Benchmark.track(name, "addBefore", size, () -> l7.addBefore(t3, rand.nextDouble()));

        IList l8 = populate(factory.get(), size, rand);
        double t4 = rand.nextDouble();
        l8.pushBack(t4);
        Benchmark.track(name, "addAfter", size, () -> l8.addAfter(t4, rand.nextDouble()));

        sink = 0;
    }

    private static IList populate(IList list, int size, Random rand) {
        for (int i = 0; i < size; i++) list.pushFront(rand.nextDouble());
        return list;
    }
}
