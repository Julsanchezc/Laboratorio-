package listasAvanzadas;

public class ListaSimpleConCola {
   Nodo cabeza;
   Nodo cola;

   public void pushFront(double v) {
      Nodo nodo = new Nodo(v);
      if (this.cabeza == null) {
         this.cabeza = this.cola = nodo;
      } else {
         nodo.siguiente = this.cabeza;
         this.cabeza = nodo;
      }

   }

   public void pushBack(double v) {
      Nodo nodo = new Nodo(v);
      if (this.cola == null) {
         this.cabeza = this.cola = nodo;
      } else {
         this.cola.siguiente = nodo;
         this.cola = nodo;
      }

   }

   public void popFront() {
      if (this.cabeza != null) {
         if (this.cabeza == this.cola) {
            this.cabeza = this.cola = null;
         } else {
            this.cabeza = this.cabeza.siguiente;
         }

      }
   }

   public void popBack() {
      if (this.cabeza != null) {
         if (this.cabeza == this.cola) {
            this.cabeza = this.cola = null;
         } else {
            Nodo actual;
            for(actual = this.cabeza; actual.siguiente != this.cola; actual = actual.siguiente) {
            }

            actual.siguiente = null;
            this.cola = actual;
         }

      }
   }

   public Nodo find(double v) {
      for(Nodo actual = this.cabeza; actual != null; actual = actual.siguiente) {
         if (actual.valor == v) {
            return actual;
         }
      }

      return null;
   }

   public void erase(double v) {
      if (this.cabeza != null) {
         if (this.cabeza.valor == v) {
            this.cabeza = this.cabeza.siguiente;
            if (this.cabeza == null) {
               this.cola = null;
            }

         } else {
            Nodo actual;
            for(actual = this.cabeza; actual.siguiente != null && actual.siguiente.valor != v; actual = actual.siguiente) {
            }

            if (actual.siguiente != null) {
               if (actual.siguiente == this.cola) {
                  this.cola = actual;
               }

               actual.siguiente = actual.siguiente.siguiente;
            }

         }
      }
   }

   public void addAfter(double objetivo, double v) {
      Nodo nodoObjetivo = this.find(objetivo);
      if (nodoObjetivo != null) {
         Nodo nuevo = new Nodo(v);
         nuevo.siguiente = nodoObjetivo.siguiente;
         nodoObjetivo.siguiente = nuevo;
         if (nodoObjetivo == this.cola) {
            this.cola = nuevo;
         }

      }
   }

   public void addBefore(double objetivo, double v) {
      if (this.cabeza != null) {
         if (this.cabeza.valor == objetivo) {
            this.pushFront(v);
         } else {
            Nodo actual;
            for(actual = this.cabeza; actual.siguiente != null && actual.siguiente.valor != objetivo; actual = actual.siguiente) {
            }

            if (actual.siguiente != null) {
               Nodo nuevo = new Nodo(v);
               nuevo.siguiente = actual.siguiente;
               actual.siguiente = nuevo;
            }

         }
      }
   }
}
