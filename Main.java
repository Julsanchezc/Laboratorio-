package listasAvanzadas;

public class Main {
   public static void main(String[] args) {
      long[] sizes = new long[]{10L, 100L, 10000L, 100000L, 100000000L, 1000000000L};
      long[] var2 = sizes;
      int var3 = sizes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long n = var2[var4];
         System.out.println("\nLISTA SIMPLE CON COLA\n");
         ListaSimpleConCola lsc = new ListaSimpleConCola();
         long inicio = System.nanoTime();

         int i;
         for(i = 0; (long)i < n; ++i) {
            lsc.pushFront((double)i);
         }

         long fin = System.nanoTime();
         System.out.println("pushFront: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();

         for(i = 0; (long)i < n; ++i) {
            lsc.pushBack((double)i);
         }

         fin = System.nanoTime();
         System.out.println("pushBack: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         lsc.find((double)(n / 2L));
         fin = System.nanoTime();
         System.out.println("find: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         lsc.addAfter((double)(n / 2L), 999.0D);
         fin = System.nanoTime();
         System.out.println("addAfter: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         lsc.addBefore((double)(n / 2L), 888.0D);
         fin = System.nanoTime();
         System.out.println("addBefore: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         lsc.popFront();
         fin = System.nanoTime();
         System.out.println("popFront: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         lsc.popBack();
         fin = System.nanoTime();
         System.out.println("popBack: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         lsc.erase((double)(n / 4L));
         fin = System.nanoTime();
         System.out.println("erase: " + (double)(fin - inicio) / 1000000.0D + " us");
         System.out.println("\nLISTA DOBLE SIN COLA\n");
         ListaDobleSinCola ldsc = new ListaDobleSinCola();
         inicio = System.nanoTime();

         int i;
         for(i = 0; (long)i < n; ++i) {
            ldsc.pushFront((double)i);
         }

         fin = System.nanoTime();
         System.out.println("pushFront: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();

         for(i = 0; (long)i < n; ++i) {
            ldsc.pushBack((double)i);
         }

         fin = System.nanoTime();
         System.out.println("pushBack: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldsc.find((double)(n / 2L));
         fin = System.nanoTime();
         System.out.println("find: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldsc.addAfter((double)(n / 2L), 999.0D);
         fin = System.nanoTime();
         System.out.println("addAfter: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldsc.addBefore((double)(n / 2L), 888.0D);
         fin = System.nanoTime();
         System.out.println("addBefore: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldsc.popFront();
         fin = System.nanoTime();
         System.out.println("popFront: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldsc.popBack();
         fin = System.nanoTime();
         System.out.println("popBack: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldsc.erase((double)(n / 4L));
         fin = System.nanoTime();
         System.out.println("erase: " + (double)(fin - inicio) / 1000000.0D + " us");
         System.out.println("\nLISTA DOBLE CON COLA\n");
         ListaDobleConCola ldc = new ListaDobleConCola();
         inicio = System.nanoTime();

         int i;
         for(i = 0; (long)i < n; ++i) {
            ldc.pushFront((double)i);
         }

         fin = System.nanoTime();
         System.out.println("pushFront: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();

         for(i = 0; (long)i < n; ++i) {
            ldc.pushBack((double)i);
         }

         fin = System.nanoTime();
         System.out.println("pushBack: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldc.find((double)(n / 2L));
         fin = System.nanoTime();
         System.out.println("find: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldc.addAfter((double)(n / 2L), 999.0D);
         fin = System.nanoTime();
         System.out.println("addAfter: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldc.addBefore((double)(n / 2L), 888.0D);
         fin = System.nanoTime();
         System.out.println("addBefore: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldc.popFront();
         fin = System.nanoTime();
         System.out.println("popFront: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldc.popBack();
         fin = System.nanoTime();
         System.out.println("popBack: " + (double)(fin - inicio) / 1000000.0D + " us");
         inicio = System.nanoTime();
         ldc.erase((double)(n / 4L));
         fin = System.nanoTime();
         System.out.println("erase: " + (double)(fin - inicio) / 1000000.0D + " us");
      }

   }
}
