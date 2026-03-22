package listasAvanzadas;

public class ListaDobleConCola {
   Nodo cabeza;
   Nodo cola;

   public void pushFront(double v) {
      Nodo nuevoNodo = new Nodo(v);
      if (this.cabeza == null) {
         this.cabeza = this.cola = nuevoNodo;
      } else {
         nuevoNodo.siguiente = this.cabeza;
         this.cabeza.anterior = nuevoNodo;
         this.cabeza = nuevoNodo;
      }

   }

   public void pushBack(double v) {
      Nodo nuevoNodo = new Nodo(v);
      if (this.cola == null) {
         this.cabeza = this.cola = nuevoNodo;
      } else {
         nuevoNodo.anterior = this.cola;
         this.cola.siguiente = nuevoNodo;
         this.cola = nuevoNodo;
      }

   }

   public void popFront() {
      if (this.cabeza != null) {
         if (this.cabeza == this.cola) {
            this.cabeza = this.cola = null;
         } else {
            this.cabeza = this.cabeza.siguiente;
            this.cabeza.anterior = null;
         }

      }
   }

   public void popBack() {
      if (this.cola != null) {
         if (this.cabeza == this.cola) {
            this.cabeza = this.cola = null;
         } else {
            this.cola = this.cola.anterior;
            this.cola.siguiente = null;
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
            this.popFront();
         } else if (nodo == this.cola) {
            this.popBack();
         } else {
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
         }

      }
   }

   public void addAfter(double objetivo, double v) {
      Nodo nodoObj = this.find(objetivo);
      if (nodoObj != null) {
         if (nodoObj == this.cola) {
            this.pushBack(v);
         } else {
            Nodo nuevo = new Nodo(v);
            nuevo.siguiente = nodoObj.siguiente;
            nuevo.anterior = nodoObj;
            nodoObj.siguiente.anterior = nuevo;
            nodoObj.siguiente = nuevo;
         }

      }
   }

   public void addBefore(double objetivo, double v) {
      Nodo nodoObj = this.find(objetivo);
      if (nodoObj != null) {
         if (nodoObj == this.cabeza) {
            this.pushFront(v);
         } else {
            Nodo nuevo = new Nodo(v);
            nuevo.anterior = nodoObj.anterior;
            nuevo.siguiente = nodoObj;
            nodoObj.anterior.siguiente = nuevo;
            nodoObj.anterior = nuevo;
         }

      }
   }
}
