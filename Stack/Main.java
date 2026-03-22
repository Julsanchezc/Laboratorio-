import java.util.Random;

public class Main {

    // Variable para evitar optimización del compilador
    private static volatile int dummy = 0;

    public static void Medir(int n) {
        StackArray<Integer> stack = new StackArray<>();
        Random random = new Random(42);

        System.out.println("--- Pruebas para tamaño N = " + n + " ---");

        // 1. PUSH
        long inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            stack.push(random.nextInt(1000000));
        }
        long fin = System.nanoTime();
        System.out.println("Push (" + n + "): " + (fin - inicio) + " ns");

        int repeticiones = 10000;

        // 2. PEEK
        inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            dummy ^= stack.peek(); // evita optimización
        }
        fin = System.nanoTime();
        System.out.println("Peek promedio: " + (fin - inicio) / (double) repeticiones + " ns");

        // 3. SIZE
        inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            dummy ^= stack.size(); // evita optimización
        }
        fin = System.nanoTime();
        System.out.println("Size promedio: " + (fin - inicio) / (double) repeticiones + " ns");

        // 4. ISEMPTY
        inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            dummy ^= (stack.isEmpty() ? 1 : 0); // evita optimización
        }
        fin = System.nanoTime();
        System.out.println("IsEmpty promedio: " + (fin - inicio) / (double) repeticiones + " ns");

        // 5. DELETE (O(n))
        int valor = stack.peek();
        inicio = System.nanoTime();
        stack.delete(valor);
        fin = System.nanoTime();
        System.out.println("Delete: " + (fin - inicio) + " ns");

        // 6. POP
        inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) {
            dummy ^= stack.pop(); // evita optimización
            stack.push(random.nextInt()); // mantener tamaño
        }
        fin = System.nanoTime();
        System.out.println("Pop promedio: " + (fin - inicio) / (double) repeticiones + " ns");

        System.out.println("--------------------------------------\n");
    }

    public static void main(String[] args) {
        int[] tamaños = {10, 100, 10000, 1000000, 100000000};
        for (int n : tamaños) {
            Medir(n);
        }
    }
}

