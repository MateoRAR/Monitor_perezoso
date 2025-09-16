// Archivo: Monitor.java (Versión Final Definitiva)
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Monitor extends Thread {
    
    private Semaphore despertarMonitor;
    private Semaphore oficinaOcupada;
    private Semaphore ayudaFinalizada;
    private ConcurrentLinkedQueue<Estudiante> colaEspera;
    private Random genAleat;
    private boolean durmiendo;
    
    public Monitor(Semaphore despertar, Semaphore oficina, Semaphore ayuda, long semilla) {
        super();
        this.despertarMonitor = despertar;
        this.oficinaOcupada = oficina;
        this.ayudaFinalizada = ayuda;
        this.colaEspera = new ConcurrentLinkedQueue<>();
        this.genAleat = new Random(semilla);
        this.durmiendo = false;
    }
    
    public void agregarEstudiante(Estudiante estudiante) {
        colaEspera.offer(estudiante);
    }
    
    public void run() {
        while (true) {
            try {
                // Si la cola está vacía, se duerme. Simple y efectivo.
                if (colaEspera.isEmpty()) {
                    durmiendo = true;
                    System.out.println("[MONITOR] No hay estudiantes en la cola, voy a dormir...");
                    despertarMonitor.acquire();
                    durmiendo = false;
                    System.out.println("[MONITOR] ¡Despierto! ¿Quién me necesita?");
                } else {
                    // Si hay estudiantes, atiende a uno.
                    Estudiante estudiante = colaEspera.poll();
                    if (estudiante != null) {
                        atenderEstudiante(estudiante);
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("[MONITOR] Interrumpido: " + e.getMessage());
                break;
            }
        }
    }
    
    private void atenderEstudiante(Estudiante estudiante) {
        try {
            System.out.println("[MONITOR] Atendiendo a " + estudiante.getNombre() + "...");
            int tiempoAtencion = (Math.abs(genAleat.nextInt()) % 2000) + 1000;
            Thread.sleep(tiempoAtencion);
            System.out.println("[MONITOR] Terminé de ayudar a " + estudiante.getNombre() + 
                             " (tiempo: " + tiempoAtencion + "ms)");
            
            ayudaFinalizada.release();
            oficinaOcupada.release();
            
        } catch (InterruptedException e) {
            System.out.println("[MONITOR] Interrumpido mientras atendía: " + e.getMessage());
        }
    }
    
    public boolean estaDurmiendo() {
        return durmiendo;
    }
}