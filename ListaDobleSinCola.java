package listasAvanzadas;

public class ListaDobleSinCola {
   Nodo cabeza;

   public void pushFront(double v) {
      Nodo nuevoNodo = new Nodo(v);
      if (this.cabeza != null) {
         nuevoNodo.siguiente = this.cabeza;
         this.cabeza.anterior = nuevoNodo;
      }

      this.cabeza = nuevoNodo;
   }

   public void pushBack(double v) {
      Nodo nuevoNodo = new Nodo(v);
      if (this.cabeza == null) {
         this.cabeza = nuevoNodo;
      } else {
         Nodo actual;
         for(actual = this.cabeza; actual.siguiente != null; actual = actual.siguiente) {
         }

         actual.siguiente = nuevoNodo;
         nuevoNodo.anterior = actual;
      }
   }

   public void popFront() {
      if (this.cabeza != null) {
         if (this.cabeza.siguiente == null) {
            this.cabeza = null;
         } else {
            this.cabeza = this.cabeza.siguiente;
            this.cabeza.anterior = null;
         }

      }
   }

   public void popBack() {
      if (this.cabeza != null) {
         if (this.cabeza.siguiente == null) {
            this.cabeza = null;
         } else {
            Nodo actual;
            for(actual = this.cabeza; actual.siguiente != null; actual = actual.siguiente) {
            }

            actual.anterior.siguiente = null;
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
      Nodo nodo = this.find(v);
      if (nodo != null) {
         if (nodo == this.cabeza) {
            this.cabeza = this.cabeza.siguiente;
            if (this.cabeza != null) {
               this.cabeza.anterior = null;
            }
         } else {
            nodo.anterior.siguiente = nodo.siguiente;
            if (nodo.siguiente != null) {
               nodo.siguiente.anterior = nodo.anterior;
            }
         }

      }
   }

   public void addAfter(double objetivo, double v) {
      Nodo nodoObj = this.find(objetivo);
      if (nodoObj != null) {
         Nodo nuevo = new Nodo(v);
         nuevo.siguiente = nodoObj.siguiente;
         nuevo.anterior = nodoObj;
         if (nodoObj.siguiente != null) {
            nodoObj.siguiente.anterior = nuevo;
         }

         nodoObj.siguiente = nuevo;
      }
   }

   public void addBefore(double objetivo, double v) {
      Nodo nodoObj = this.find(objetivo);
      if (nodoObj != null) {
         if (nodoObj == this.cabeza) {
            this.pushFront(v);
         } else {
            Nodo nuevo = new Nodo(v);
            nuevo.siguiente = nodoObj;
            nuevo.anterior = nodoObj.anterior;
            nodoObj.anterior.siguiente = nuevo;
            nodoObj.anterior = nuevo;
         }

      }
   }
}
