package ListaDobleconCola;

public class Nodo {
   public double valor;
   public Nodo siguiente;
   public Nodo anterior;

   public Nodo(double valor) {
      this.valor = valor;
      this.siguiente = null;
      this.anterior = null;
   }
}
