public static void medirStack(int n) {
    StackArray<Integer> stack = new StackArray<>();
    
    // Medir PUSH
    long inicio = System.nanoTime(); // Usamos nanosegundos por precisión 
    for (int i = 0; i < n; i++) {
        stack.push(i);
    }
    long fin = System.nanoTime();
    //us es nanosegundo
    System.out.println("Push para " + n + ": " + (fin - inicio) / 1000.0 + " us");
    
    inicio = System.nanoTime();
    stack.delete(n / 2); 
    fin = System.nanoTime();
    System.out.println("Delete para " + n + ": " + (fin - inicio) / 1000.0 + " us");
}
public static void main(String[] args) {
    int[] tamaños = {1000, 5000, 10000, 50000, 100000}; 
    for (int n : tamaños) {
        medirStack(n);
    }
}
