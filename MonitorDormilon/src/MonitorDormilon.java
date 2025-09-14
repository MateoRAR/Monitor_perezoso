// Proyecto Monitor Dormilón - Clase Principal
// Sistemas Operacionales - Monitor Dormilón

import java.util.concurrent.Semaphore;

// Esta es la clase ejecutable del proyecto Monitor Dormilón
// Coordina las actividades del monitor y los estudiantes usando semáforos
public class MonitorDormilon {
    
    public static void main(String[] args) {
        // Revisar que se reciba el número de estudiantes como parámetro
        if (args.length != 2) {
            System.out.println("Sintaxis: MonitorDormilon num_estudiantes semilla_aleatoria");
            System.out.println("Ejemplo: MonitorDormilon 5 12345");
            System.exit(1);
        }
        
        int numEstudiantes = Integer.parseInt(args[0]);
        long semilla = Long.parseLong(args[1]);
        
        System.out.println("=== SIMULACIÓN DEL MONITOR DORMILÓN ===");
        System.out.println("Número de estudiantes: " + numEstudiantes);
        System.out.println("Semilla aleatoria: " + semilla);
        System.out.println("=====================================\n");
        
        // Crear los semáforos necesarios
        Semaphore despertarMonitor = new Semaphore(0, true);  // Para despertar al monitor
        Semaphore sillasDisponibles = new Semaphore(3, true); // 3 sillas en el corredor
        Semaphore oficinaOcupada = new Semaphore(1, true);    // Solo 1 estudiante en la oficina
        
        // Crear el monitor
        Monitor monitor = new Monitor(despertarMonitor, sillasDisponibles, oficinaOcupada, semilla);
        
        // Crear array de estudiantes
        Estudiante[] estudiantes = new Estudiante[numEstudiantes];
        
        // Crear e inicializar los estudiantes
        for (int i = 0; i < numEstudiantes; i++) {
            String nombreEstudiante = "ESTUDIANTE " + (i + 1);
            estudiantes[i] = new Estudiante(nombreEstudiante, despertarMonitor, 
                                          sillasDisponibles, oficinaOcupada, monitor, semilla);
        }
        
        // Iniciar el monitor
        System.out.println("Iniciando el monitor...");
        monitor.start();
        
        // Esperar un poco antes de iniciar los estudiantes
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error al esperar: " + e.getMessage());
        }
        
        // Iniciar todos los estudiantes
        System.out.println("Iniciando " + numEstudiantes + " estudiantes...\n");
        for (int i = 0; i < numEstudiantes; i++) {
            estudiantes[i].start();
            
            // Pequeña pausa entre el inicio de cada estudiante para simular llegadas escalonadas
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Error al esperar: " + e.getMessage());
            }
        }
        
        System.out.println("\n=== SIMULACIÓN INICIADA ===");
        System.out.println("Presiona Ctrl+C para detener la simulación\n");
        
        // El programa principal termina aquí, pero los hilos continúan ejecutándose
        // En un entorno real, podrías agregar lógica para detener la simulación
        // después de un tiempo determinado o con una señal específica
    }
}
