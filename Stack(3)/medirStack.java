public static void medirStack(int n) {
    StackArray<Integer> stack = new StackArray<>();
    
    // Medir PUSH
    long inicio = System.nanoTime(); // Usamos nanosegundos por precisión [cite: 56, 83]
    for (int i = 0; i < n; i++) {
        stack.push(i);
    }
    long fin = System.nanoTime();
    System.out.println("Push para " + n + ": " + (fin - inicio) / 1000.0 + " us");
    
    // Medir DELETE (el más lento O(n)) [cite: 43]
    inicio = System.nanoTime();
    stack.delete(n / 2); // Eliminamos un elemento intermedio
    fin = System.nanoTime();
    System.out.println("Delete para " + n + ": " + (fin - inicio) / 1000.0 + " us");
}
public static void main(String[] args) {
    int[] tamaños = {1000, 5000, 10000, 50000, 100000}; // Tamaños crecientes [cite: 57]
    for (int n : tamaños) {
        medirStack(n);
    }
}