// Proyecto Carrera - Clase Carro
// Sistemas Operacionales - Juan Manuel Madrid Molina

import java.util.concurrent.Semaphore;
import java.util.Random;

// La clase Carro implementa un carro de carreras genérico como un hilo. Cada carro recibe
// un semáforo en particular, que le indica que puede ingresar al área de insumos.

public class Carro extends Thread {
	
	private Semaphore AreaInsumos;     // Semáforo del jefe de insumos (lo despierta)
	private Semaphore Insumo;          // El semáforo específico que permite acceder al área de insumos
	private String Nombre;             // El nombre del carro
	private Random GenAleat;           // Un generador de números aleatorios
	
	// Constructor de la clase. Inicializa todos los datos requeridos
	public Carro(Semaphore areasum, Semaphore insumo, String nombre, long semilla) {
		super();
		AreaInsumos= areasum;
		Insumo = insumo;
		Nombre = nombre;
		GenAleat = new Random(semilla);
	}
	
	// Método principal del hilo
	public void run() {
		// Correr continuamente
		while (true) {
			try {
				System.out.println("- ["+Nombre+"] Llegué a pits, esperando insumos...");
				// Obtener el semáforo específico para entrar al área de insumos
				Insumo.acquire();
				System.out.println("- ["+Nombre+"] Tengo los insumos!");
				// Esperar un tiempo aleatorio mientras se le hace servicio al carro
				sleep(Math.abs(GenAleat.nextInt()) % 1000);
				// Finalizó el servicio, notificar al jefe de insumos
				AreaInsumos.release();
				System.out.println("- ["+Nombre+"] Salí de pits, corriendo en pista...");
				// Esperar un tiempo aleatorio mientras el carro corre en pista
				sleep(Math.abs(GenAleat.nextInt()) % 1000);
			} catch (InterruptedException e) {
			}
		}
	}
}

