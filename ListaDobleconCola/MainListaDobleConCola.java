package ListaDobleconCola;

import java.util.Random;

public class MainListaDobleConCola {

    public static volatile double dummy = 0;

    public static ListaDobleConCola crearLista(int n, Random rand) {
        ListaDobleConCola l = new ListaDobleConCola();
        for (int i = 0; i < n; i++) {
            l.pushBack(rand.nextDouble());
        }
        return l;
    }

    public static void main(String[] args) {

        int[] sizes = {10, 100, 10000, 1000000, 100000000};
        Random rand = new Random();

        for (int n : sizes) {

            System.out.println("\nTamaño: " + n);

            ListaDobleConCola ldc;

            // pushFront (O(1))
            ldc = new ListaDobleConCola();
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                ldc.pushFront(rand.nextDouble());
            }
            long fin = System.nanoTime();
            System.out.println("pushFront: " + (fin - inicio) + " ns");

            // pushBack (O(1))
            ldc = new ListaDobleConCola();
            inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                ldc.pushBack(rand.nextDouble());
            }
            fin = System.nanoTime();
            System.out.println("pushBack: " + (fin - inicio) + " ns");

            int repeticiones = 100;
            double objetivo;

            // find (O(n))
            ldc = crearLista(n, rand);
            objetivo = rand.nextDouble();
            ldc.pushBack(objetivo);

            inicio = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                dummy += (ldc.find(objetivo) != null ? 1 : 0);
            }
            fin = System.nanoTime();
            System.out.println("find (promedio): " + ((fin - inicio) / repeticiones) + " ns");

            // addAfter
            ldc = crearLista(n, rand);
            objetivo = rand.nextDouble();
            ldc.pushBack(objetivo);

            inicio = System.nanoTime();
            ldc.addAfter(objetivo, rand.nextDouble());
            fin = System.nanoTime();
            System.out.println("addAfter: " + (fin - inicio) + " ns");

            // addBefore
            ldc = crearLista(n, rand);
            objetivo = rand.nextDouble();
            ldc.pushBack(objetivo);

            inicio = System.nanoTime();
            ldc.addBefore(objetivo, rand.nextDouble());
            fin = System.nanoTime();
            System.out.println("addBefore: " + (fin - inicio) + " ns");

            // popFront (O(1))
            ldc = crearLista(n, rand);

            inicio = System.nanoTime();
            ldc.popFront();
            fin = System.nanoTime();
            System.out.println("popFront: " + (fin - inicio) + " ns");

            // popBack (O(1))
            ldc = crearLista(n, rand);

            inicio = System.nanoTime();
            ldc.popBack();
            fin = System.nanoTime();
            System.out.println("popBack: " + (fin - inicio) + " ns");

            // erase
            ldc = crearLista(n, rand);
            objetivo = rand.nextDouble();
            ldc.pushBack(objetivo);

            inicio = System.nanoTime();
            ldc.erase(objetivo);
            fin = System.nanoTime();
            System.out.println("erase: " + (fin - inicio) + " ns");

            dummy = 0;
        }
    }
}