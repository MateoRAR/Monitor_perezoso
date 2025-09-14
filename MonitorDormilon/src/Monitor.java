// Proyecto Monitor Dormilón - Clase Monitor
// Sistemas Operacionales - Monitor Dormilón

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

// Esta clase implementa al monitor que duerme cuando no hay estudiantes
// y atiende a los estudiantes que llegan a pedir ayuda
public class Monitor extends Thread {
    
    private Semaphore despertarMonitor;     // Semáforo para despertar al monitor
    private Semaphore sillasDisponibles;    // Semáforo para controlar las 3 sillas
    private Semaphore oficinaOcupada;       // Semáforo para controlar el acceso a la oficina
    private ConcurrentLinkedQueue<Estudiante> colaEspera; // Cola de estudiantes esperando
    private Random genAleat;                // Generador de números aleatorios
    private boolean durmiendo;              // Estado del monitor
    
    // Constructor de la clase
    public Monitor(Semaphore despertar, Semaphore sillas, Semaphore oficina, long semilla) {
        super();
        despertarMonitor = despertar;
        sillasDisponibles = sillas;
        oficinaOcupada = oficina;
        colaEspera = new ConcurrentLinkedQueue<>();
        genAleat = new Random(semilla);
        durmiendo = true;
    }
    
    // Método para agregar estudiante a la cola de espera
    public void agregarEstudiante(Estudiante estudiante) {
        colaEspera.offer(estudiante);
    }
    
    // Método principal del hilo del monitor
    public void run() {
        while (true) {
            try {
                // El monitor duerme hasta que un estudiante lo despierte
                if (durmiendo) {
                    System.out.println("[MONITOR] Zzz... durmiendo en la oficina...");
                    despertarMonitor.acquire(); // Esperar a ser despertado
                    durmiendo = false;
                    System.out.println("[MONITOR] ¡Despierto! ¿Quién me necesita?");
                }
                
                // Atender estudiantes en la cola
                while (!colaEspera.isEmpty()) {
                    Estudiante estudiante = colaEspera.poll();
                    if (estudiante != null) {
                        atenderEstudiante(estudiante);
                    }
                }
                
                // Si no hay más estudiantes, el monitor puede volver a dormir
                if (colaEspera.isEmpty()) {
                    durmiendo = true;
                    System.out.println("[MONITOR] No hay más estudiantes, volviendo a dormir...");
                }
                
            } catch (InterruptedException e) {
                System.out.println("[MONITOR] Interrumpido: " + e.getMessage());
            }
        }
    }
    
    // Método para atender a un estudiante
    private void atenderEstudiante(Estudiante estudiante) {
        try {
            System.out.println("[MONITOR] Atendiendo a " + estudiante.getNombre() + "...");
            
            // Simular tiempo de atención (1-3 segundos)
            int tiempoAtencion = (Math.abs(genAleat.nextInt()) % 2000) + 1000;
            Thread.sleep(tiempoAtencion);
            
            System.out.println("[MONITOR] Terminé de ayudar a " + estudiante.getNombre() + 
                             " (tiempo: " + tiempoAtencion + "ms)");
            
            // Liberar la oficina para el siguiente estudiante
            oficinaOcupada.release();
            
        } catch (InterruptedException e) {
            System.out.println("[MONITOR] Interrumpido mientras atendía: " + e.getMessage());
        }
    }
    
    // Método para verificar si el monitor está durmiendo
    public boolean estaDurmiendo() {
        return durmiendo;
    }
}
