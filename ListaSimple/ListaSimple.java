package ListaSimple;

public class ListaSimple
{
    Nodo cabeza;
    
    public void pushFront(double v)
    {
        Nodo newNodo = new Nodo(v);
        newNodo.siguiente = cabeza;
        cabeza = newNodo;
    }
    public void popFront()
    {
        if(cabeza != null)
        {
            cabeza = cabeza.siguiente;
        }
    }
    public Nodo find(double v)
    {
        Nodo actual = cabeza;
        while (actual != null)
        {
            if (actual.valor == v) return actual;
            actual = actual.siguiente;
        }
        return null;
    }
    public void pushBack (double v)
    {
        Nodo newNodo = new Nodo(v);
        if (cabeza == null)
        {
            cabeza = newNodo;
            return;
        }
        Nodo actual = cabeza;
        while (actual.siguiente != null)
        {
            actual = actual.siguiente;
        }
        actual.siguiente = newNodo;
    }
    public void popBack()
    {
        if (cabeza == null)return;
        if (cabeza.siguiente == null)
        {
            cabeza = null;
            return;
        }
        Nodo actual = cabeza;
        while (actual.siguiente.siguiente != null)
        {
            actual = actual.siguiente;
        }
        actual.siguiente = null;
    }
    public void borrar(double v)
    {
        if (cabeza == null)return;
        if (cabeza.valor == v)
        {
            cabeza = cabeza.siguiente;
            return;
        }
        Nodo actual = cabeza;
        while (actual.siguiente != null && actual.siguiente.valor != v)
        {
            actual = actual.siguiente;
        }
        if (actual.siguiente != null)
        {
            actual.siguiente = actual.siguiente.siguiente;
        }
       
    }
     public void addAfter(double referencia, double v)
        {
            Nodo nodo = find (referencia);
            if (nodo != null)
            {
                Nodo newNodo = new Nodo(v);
                newNodo.siguiente = nodo.siguiente;
                nodo.siguiente = newNodo;
            }
        }
}
