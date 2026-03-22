package ListaSimple;

import java.util.Random;

public class Main {

    public static volatile double dummy = 0;

    public static ListaSimple crearLista(int size, Random rand) {
        ListaSimple lista = new ListaSimple();
        for (int i = 0; i < size; i++) {
            lista.pushBack(rand.nextDouble());
        }
        return lista;
    }

    public static void main(String[] args) {

        int[] sizes = {10, 100, 10000, 1000000, 100000000};
        Random rand = new Random();
        int repeticiones = 100;

        for (int size : sizes) {

            System.out.println("\n--- PRUEBAS PARA TAMAÑO: " + size + " ---");

            ListaSimple lista;
            double objetivo;

            // -------- pushFront --------
            lista = new ListaSimple();
            long start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                lista.pushFront(rand.nextDouble());
            }
            long end = System.nanoTime();
            System.out.println("pushFront: " + formatTime(start, end) + " ns");

            // -------- pushBack --------
            lista = new ListaSimple();
            start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                lista.pushBack(rand.nextDouble());
            }
            end = System.nanoTime();
            System.out.println("pushBack:  " + formatTime(start, end) + " ns");

            // -------- popFront --------
            lista = crearLista(size, rand);
            start = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                lista.popFront();
            }
            end = System.nanoTime();
            System.out.println("popFront (prom): " + (formatTime(start, end) / repeticiones) + " ns");

            // -------- popBack --------
            lista = crearLista(size, rand);
            start = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                lista.popBack();
            }
            end = System.nanoTime();
            System.out.println("popBack (prom):  " + (formatTime(start, end) / repeticiones) + " ns");

            // -------- find --------
            lista = crearLista(size, rand);
            objetivo = rand.nextDouble();
            lista.pushBack(objetivo);

            start = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                dummy += (lista.find(objetivo) != null ? 1 : 0);
            }
            end = System.nanoTime();
            System.out.println("find (prom):     " + (formatTime(start, end) / repeticiones) + " ns");

            // -------- addAfter --------
            lista = crearLista(size, rand);
            objetivo = rand.nextDouble();
            lista.pushBack(objetivo);

            start = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                lista.addAfter(objetivo, rand.nextDouble());
            }
            end = System.nanoTime();
            System.out.println("addAfter (prom): " + (formatTime(start, end) / repeticiones) + " ns");

            // -------- addBefore --------
            lista = crearLista(size, rand);
            objetivo = rand.nextDouble();
            lista.pushBack(objetivo);

            start = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                lista.addBefore(objetivo, rand.nextDouble());
            }
            end = System.nanoTime();
            System.out.println("addBefore (prom): " + (formatTime(start, end) / repeticiones) + " ns");

            // -------- borrar --------
            lista = crearLista(size, rand);
            objetivo = rand.nextDouble();
            lista.pushBack(objetivo);

            start = System.nanoTime();
            for (int i = 0; i < repeticiones; i++) {
                lista.borrar(objetivo);
            }
            end = System.nanoTime();
            System.out.println("borrar (prom):   " + (formatTime(start, end) / repeticiones) + " ns");

            dummy = 0;

            System.out.println("------------------------------------\n");
        }
    }

    private static long formatTime(long start, long end) {
        return (end - start); 
    }
}
