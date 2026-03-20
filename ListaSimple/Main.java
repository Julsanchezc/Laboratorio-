package ListaSimple;

public class Main 
{
    public static void main(String[] args) 
    {
        int[] sizes = {10,100,10000,100000};
        for (int size: sizes)
        {
            ListaSimple lista = new ListaSimple();
            long start = System.nanoTime();
            for (int i = 0; i < size; i++) lista.pushFront(i);
            long end = System.nanoTime();
            System.out.println("Size " + size + " - pushFront: " + (end - start) / 1e6 + " ms");

            ListaSimple lista2 = new ListaSimple();
            start = System.nanoTime();
            for (int i = 0; i < size; i++) lista2.pushBack(i);
            end = System.nanoTime();
            System.out.println("Size " + size + " - pushBack: " + (end - start) / 1e6 + " ms");
            System.out.println("------------------------------------");
        
        }
    }
}
