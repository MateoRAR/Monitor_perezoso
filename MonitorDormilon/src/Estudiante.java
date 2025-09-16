import java.util.Random;
import java.util.concurrent.Semaphore;

public class Estudiante extends Thread {
    
    private String nombre;
    private Semaphore despertarMonitor;
    private Semaphore sillasDisponibles;
    private Semaphore oficinaOcupada;
    private Semaphore ayudaFinalizada;
    private Monitor monitor;
    private Random genAleat;

    private enum Estado { PROGRAMANDO, BUSCANDO_AYUDA }
    private Estado estadoActual;
    
    public Estudiante(String nombre, Semaphore despertar, Semaphore sillas, 
                     Semaphore oficina, Semaphore ayuda, Monitor monitor, long semilla) {
        super();
        this.nombre = nombre;
        this.despertarMonitor = despertar;
        this.sillasDisponibles = sillas;
        this.oficinaOcupada = oficina;
        this.ayudaFinalizada = ayuda;
        this.monitor = monitor;
        this.genAleat = new Random(semilla + nombre.hashCode());
        this.estadoActual = Estado.PROGRAMANDO; 
    }

    public void run() {
        while (true) {
            try {
                if (estadoActual == Estado.PROGRAMANDO) {
                    programar();
                    estadoActual = Estado.BUSCANDO_AYUDA;
                } else if (estadoActual == Estado.BUSCANDO_AYUDA) {
                    if (buscarAyuda()) {
                        estadoActual = Estado.PROGRAMANDO;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("[" + nombre + "] Interrumpido: " + e.getMessage());
                break;
            }
        }
    }
        
    private void programar() throws InterruptedException {
        int tiempoProgramacion = (Math.abs(genAleat.nextInt()) % 3000) + 2000;
        System.out.println("[" + nombre + "] Programando en la sala de cómputo... (tiempo: " + 
                         tiempoProgramacion + "ms)");
        Thread.sleep(tiempoProgramacion);
        System.out.println("[" + nombre + "] Terminé de programar, necesito ayuda del monitor");
    }
    
    private boolean buscarAyuda() throws InterruptedException {
        System.out.println("[" + nombre + "] Llegando a la oficina del monitor...");

        // Intenta entrar directamente a la oficina
        if (oficinaOcupada.tryAcquire()) {
            System.out.println("[" + nombre + "] La oficina está libre, entrando...");
            
            monitor.agregarEstudiante(this);
            if (monitor.estaDurmiendo()) {
                System.out.println("[" + nombre + "] ¡El monitor está durmiendo! Despertándolo...");
                despertarMonitor.release();
            }

            System.out.println("[" + nombre + "] Esperando a ser atendido por el monitor...");
            ayudaFinalizada.acquire();
            return true;

        } else {
            // La oficina está ocupada, intenta sentarse en una silla
            System.out.println("[" + nombre + "] La oficina está ocupada, buscando silla en el corredor...");
            if (sillasDisponibles.tryAcquire()) {
                System.out.println("[" + nombre + "] Me senté en una silla, esperando que la oficina se desocupe...");
                oficinaOcupada.acquire(); // Espera pasivamente aquí hasta que la oficina se libere
                sillasDisponibles.release(); // Se levanta de la silla, la libera para otro
                
                System.out.println("[" + nombre + "] La oficina se desocupó, entrando...");
                monitor.agregarEstudiante(this);
                despertarMonitor.release(); // Le avisa al monitor que hay alguien nuevo en la cola
                
                System.out.println("[" + nombre + "] Esperando a ser atendido por el monitor...");
                ayudaFinalizada.acquire();
                return true;
            } else {
                System.out.println("[" + nombre + "] No hay sillas disponibles, me voy y regreso más tarde...");
                Thread.sleep((Math.abs(genAleat.nextInt()) % 3000) + 3000);
                System.out.println("[" + nombre + "] Regresando para buscar ayuda...");
                return false;
            }
        }
    }
    
    public String getNombre() {
        return nombre;
    }
}