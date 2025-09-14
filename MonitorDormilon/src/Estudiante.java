// Proyecto Monitor Dormilón - Clase Estudiante
// Sistemas Operacionales - Monitor Dormilón

import java.util.Random;
import java.util.concurrent.Semaphore;

// Esta clase implementa un estudiante que alterna entre programar
// y buscar ayuda del monitor
public class Estudiante extends Thread {
    
    private String nombre;                  // Nombre del estudiante
    private Semaphore despertarMonitor;     // Semáforo para despertar al monitor
    private Semaphore sillasDisponibles;    // Semáforo para controlar las 3 sillas
    private Semaphore oficinaOcupada;       // Semáforo para controlar el acceso a la oficina
    private Monitor monitor;                // Referencia al monitor
    private Random genAleat;                // Generador de números aleatorios
    private boolean necesitaAyuda;          // Estado del estudiante
    
    // Constructor de la clase
    public Estudiante(String nombre, Semaphore despertar, Semaphore sillas, 
                     Semaphore oficina, Monitor monitor, long semilla) {
        super();
        this.nombre = nombre;
        despertarMonitor = despertar;
        sillasDisponibles = sillas;
        oficinaOcupada = oficina;
        this.monitor = monitor;
        genAleat = new Random(semilla + nombre.hashCode()); // Semilla única por estudiante
        necesitaAyuda = false;
    }
    
    // Método principal del hilo del estudiante
    public void run() {
        while (true) {
            try {
                // Simular tiempo programando en la sala de cómputo
                programar();
                
                // Después de programar, necesitar ayuda
                necesitaAyuda = true;
                buscarAyuda();
                
            } catch (InterruptedException e) {
                System.out.println("[" + nombre + "] Interrumpido: " + e.getMessage());
            }
        }
    }
    
    // Método para simular programación
    private void programar() throws InterruptedException {
        int tiempoProgramacion = (Math.abs(genAleat.nextInt()) % 3000) + 2000; // 2-5 segundos
        System.out.println("[" + nombre + "] Programando en la sala de cómputo... (tiempo: " + 
                         tiempoProgramacion + "ms)");
        Thread.sleep(tiempoProgramacion);
        System.out.println("[" + nombre + "] Terminé de programar, necesito ayuda del monitor");
    }
    
    // Método para buscar ayuda del monitor
    private void buscarAyuda() throws InterruptedException {
        System.out.println("[" + nombre + "] Llegando a la oficina del monitor...");
        
        // Intentar obtener acceso a la oficina
        if (oficinaOcupada.tryAcquire()) {
            // La oficina está libre, puedo entrar directamente
            System.out.println("[" + nombre + "] La oficina está libre, entrando...");
            
            if (monitor.estaDurmiendo()) {
                // El monitor está durmiendo, despertarlo
                System.out.println("[" + nombre + "] ¡El monitor está durmiendo! Despertándolo...");
                despertarMonitor.release();
            }
            
            // Agregarme a la cola de espera del monitor
            monitor.agregarEstudiante(this);
            
        } else {
            // La oficina está ocupada, intentar sentarse en una silla
            System.out.println("[" + nombre + "] La oficina está ocupada, buscando silla en el corredor...");
            
            if (sillasDisponibles.tryAcquire()) {
                // Hay silla disponible, sentarse y esperar
                System.out.println("[" + nombre + "] Me senté en una silla del corredor, esperando...");
                
                // Esperar en la silla
                esperarEnSilla();
                
            } else {
                // No hay sillas disponibles, irse y regresar más tarde
                System.out.println("[" + nombre + "] No hay sillas disponibles, me voy y regreso más tarde...");
                regresarMasTarde();
            }
        }
    }
    
    // Método para esperar en la silla del corredor
    private void esperarEnSilla() throws InterruptedException {
        // Simular tiempo de espera en la silla
        int tiempoEspera = (Math.abs(genAleat.nextInt()) % 2000) + 1000; // 1-3 segundos
        Thread.sleep(tiempoEspera);
        
        // Liberar la silla
        sillasDisponibles.release();
        System.out.println("[" + nombre + "] Me levanté de la silla, intentando entrar a la oficina...");
        
        // Intentar entrar a la oficina nuevamente
        buscarAyuda();
    }
    
    // Método para simular irse y regresar más tarde
    private void regresarMasTarde() throws InterruptedException {
        // Simular tiempo fuera (3-6 segundos)
        int tiempoFuera = (Math.abs(genAleat.nextInt()) % 3000) + 3000;
        System.out.println("[" + nombre + "] Regresando en " + tiempoFuera + "ms...");
        Thread.sleep(tiempoFuera);
        
        // Regresar a buscar ayuda
        buscarAyuda();
    }
    
    // Getter para el nombre
    public String getNombre() {
        return nombre;
    }
}
