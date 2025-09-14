// Proyecto Carrera - Clase JefeInsumos
// Sistemas Operacionales - Juan Manuel Madrid Molina

import java.util.concurrent.Semaphore;
import java.util.Random;

// Esta clase implementa al jefe de insumos. Gestiona 4 semáforos, para controlar el
// acceso de los cuatro carros de carreras al área de insumos. También tiene un semáforo
// usado por todos los carros, para notificar al jefe que su servicio concluyó.

public class JefeInsumos extends Thread {
	
	private Semaphore AreaInsumos; // Los carros lo usan para notificar su salida del área de insumos
	private Semaphore S_Agua; // Usado por el carro con agua ilimitada
	private Semaphore S_Aceite; // Usado por el carro con aceite ilimitado
	private Semaphore S_Gasolina; // Usado por el carro con gasolina ilimitada
	private Semaphore S_Llantas; // Usado por el carro con llantas ilimitadas
	private Random GenAleat; // Generador de números aleatorios
	
	// Constructor de la clase - Inicializa todos los datos requeridos
	public JefeInsumos(Semaphore areasum, Semaphore agua, Semaphore aceite, Semaphore gasolina, Semaphore llantas, long semilla) {
		super();
		AreaInsumos = areasum;
		S_Agua = agua;
		S_Aceite = aceite;
		S_Gasolina = gasolina;
		S_Llantas = llantas;
		GenAleat = new Random(semilla);
	}
	
	// Método principal del hilo
	public void run() {
		// Ciclo infinito
		while (true) {
			try {
				// Esperar a que no haya carros en el área de insumos
				AreaInsumos.acquire();
				// Seleccionar insumos a poner en el área de insumos
				switch (Math.abs(GenAleat.nextInt()) % 4) {
				case 0: System.out.println("[JEFE INSUMOS] Pongo aceite, gasolina y llantas en el área de insumos.");
						S_Agua.release();
						break;
				case 1: System.out.println("[JEFE INSUMOS] Pongo agua, gasolina y llantas en el área de insumos");
						S_Aceite.release();
						break;
				case 2: System.out.println("[JEFE INSUMOS] Pongo agua, aceite y llantas en el área de insumos");
						S_Gasolina.release();
						break;
				case 3: System.out.println("[JEFE INSUMOS] Pongo agua, aceite y gasolina en el área de insumos");
						S_Llantas.release();
						break;
					}
				} catch (InterruptedException e) {
					}
			}
		}
	}