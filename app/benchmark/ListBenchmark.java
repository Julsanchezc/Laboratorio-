package app.benchmark;
 
import java.util.Random;
import java.util.function.Supplier;

import app.core.AppLogger;
import app.datastructures.lists.DoublyLinkedList;
import app.datastructures.lists.DoublyLinkedListWithTail;
import app.datastructures.lists.IList;
import app.datastructures.lists.SinglyLinkedList;
import app.datastructures.lists.SinglyLinkedListWithTail;
 
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
 
        // FIX 1 — pushFront: populate first (no timer), then measure ONE extra insertion.
        // This captures the true cost of pushFront on a list of size N, not an
        // amortised average that hides O(n) growth in implementations without a head pointer.
        IList l1 = populate(factory.get(), size, rand);
        Benchmark.track(name, "pushFront", size, () -> l1.pushFront(rand.nextDouble()));
 
        // FIX 2 — pushBack: same reasoning as pushFront.
        // For lists without a tail pointer this is O(n); measuring a single call on
        // an already-full list gives the honest per-operation cost at size N.
        IList l2 = populate(factory.get(), size, rand);
        Benchmark.track(name, "pushBack", size, () -> l2.pushBack(rand.nextDouble()));
 
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
        // pushFront is always O(1) regardless of implementation (head pointer always exists),
        // so populate never becomes O(n²) even for lists without a tail pointer.
        for (int i = 0; i < size; i++) list.pushFront(rand.nextDouble());
        return list;
    }
}
 
