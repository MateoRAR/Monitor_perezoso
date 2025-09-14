import java.util.concurrent.Semaphore;

class LectorEscritor {

    static Semaphore cerrojoLectura = new Semaphore(1);
    static Semaphore cerrojoEscritura = new Semaphore(1);
    static int numLectores = 0;

    static class Leer implements Runnable {
        @Override
        public void run() {
            try {
                //Ningún otro lector puede acceder a la sección de entrada simultáneamente
                cerrojoLectura.acquire();
                //SECCIÓN CRÍTICA
                //Soy un lector tratando de acceder a la sección crítica
                numLectores++;
                //Soy el primero?
                if (numLectores == 1) {
                	//Bloqueo a los escritores. Otros lectores pueden entrar
                    cerrojoEscritura.acquire();
                }
                //SALIDA DE LA SECCIÓN CRÍTICA
                cerrojoLectura.release();

                //Efectuar la lectura
                System.out.println("Hilo "+Thread.currentThread().getName() + " está LEYENDO");
                Thread.sleep(1500);
                System.out.println("Hilo "+Thread.currentThread().getName() + " ha TERMINADO DE LEER");

                //Ningún otro lector puede acceder a la sección de salida simultáneamente
                cerrojoLectura.acquire();
                //SECCIÓN CRÍTICA
                //Ya terminé la lectura. Un lector menos...
                numLectores--;
                //Soy el último?
                if(numLectores == 0) {
                	//Dar permiso a los escritores
                    cerrojoEscritura.release();
                }
                //SALIDA DE LA SECCIÓN CRÍTICA
                cerrojoLectura.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Escribir implements Runnable {
        @Override
        public void run() {
            try {
                cerrojoEscritura.acquire();
                System.out.println("Hilo "+Thread.currentThread().getName() + " está ESCRIBIENDO");
                Thread.sleep(2500);
                System.out.println("Hilo "+Thread.currentThread().getName() + " ha TERMINADO DE ESCRIBIR");
                cerrojoEscritura.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Leer leer = new Leer();
        Escribir escribir = new Escribir();
        Thread h1 = new Thread(escribir);
        h1.setName("hilo1");
        Thread h2 = new Thread(leer);
        h2.setName("hilo2");
        Thread h3 = new Thread(leer);
        h3.setName("hilo3");
        Thread h4 = new Thread(escribir);
        h4.setName("hilo4");
        Thread h5 = new Thread(leer);
        h5.setName("hilo5");
        h2.start();
        h1.start();
        h3.start();
        h4.start();
        h5.start();
    }
}