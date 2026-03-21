
public interface MyStack<T> {
    void push(T x);      // Inserta en la cima [cite: 35]
    T pop();             // Elimina y retorna la cima [cite: 37]
    T peek();            // Retorna la cima sin eliminar [cite: 39]
    boolean isEmpty();   // Verifica si está vacía [cite: 41]
    int size();          // Retorna cantidad de elementos [cite: 42]
    void delete(T n);    // Elimina la primera aparición de n [cite: 43]
}
    
