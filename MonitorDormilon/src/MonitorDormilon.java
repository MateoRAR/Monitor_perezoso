import java.util.concurrent.Semaphore;

public class MonitorDormilon {
    private static final int NUM_SILLAS = 3;

    public static void main(String[] args) {
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
        
        Semaphore despertarMonitor = new Semaphore(0, true);
        Semaphore sillasDisponibles = new Semaphore(NUM_SILLAS, true);
        Semaphore oficinaOcupada = new Semaphore(1, true);
        Semaphore ayudaFinalizada = new Semaphore(0, true);
        
        // Constructor del Monitor corregido (ya no necesita sillasDisponibles)
        Monitor monitor = new Monitor(despertarMonitor, oficinaOcupada, ayudaFinalizada, semilla);
        
        Estudiante[] estudiantes = new Estudiante[numEstudiantes];
        
        for (int i = 0; i < numEstudiantes; i++) {
            String nombreEstudiante = "ESTUDIANTE " + (i + 1);
            estudiantes[i] = new Estudiante(nombreEstudiante, despertarMonitor, 
                                          sillasDisponibles, oficinaOcupada, ayudaFinalizada, monitor, semilla);
        }
        
        System.out.println("Iniciando el monitor...");
        monitor.start();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error al esperar: " + e.getMessage());
        }
        
        System.out.println("Iniciando " + numEstudiantes + " estudiantes...\n");
        for (int i = 0; i < numEstudiantes; i++) {
            estudiantes[i].start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Error al esperar: " + e.getMessage());
            }
        }
        
        System.out.println("\n=== SIMULACIÓN INICIADA ===");
        System.out.println("Presiona Ctrl+C para detener la simulación\n");
    }
}