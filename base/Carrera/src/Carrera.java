// Proyecto Carrera - Clase Carrera
// Sistemas Operacionales - Juan Manuel Madrid Molina

import java.util.concurrent.Semaphore;

// Esta es la clase ejecutable del proyecto Carrera (concurrencia y sincronización
// usando semáforos

public class Carrera {
	public static void main(String[] args) {
		// Revisar que se recibe la semilla como parámetro
		if (args.length != 1) {
			System.out.println("Sintaxis: Carrera semilla_aleatoria");
			System.exit(1);
		}
		
		long Semilla=Long.parseLong(args[0]);
		
		// Instanciar los semáforos requeridos
		Semaphore AreaInsumos;
		Semaphore S_Agua;
		Semaphore S_Aceite;
		Semaphore S_Gasolina;
		Semaphore S_Llantas;
		
		// Instanciar los carros de carreras
		Carro Carro_Agua, Carro_Aceite, Carro_Gasolina, Carro_Llantas;
		// Instanciar el jefe de suministros
		JefeInsumos Jefe;
		
		AreaInsumos = new Semaphore(1, true); // Todos semáforos binarios
		S_Agua = new Semaphore(1, true);
		S_Aceite = new Semaphore(1, true);
		S_Gasolina = new Semaphore(1, true);
		S_Llantas = new Semaphore(1, true);
		
		Jefe = new JefeInsumos(AreaInsumos, S_Agua, S_Aceite, S_Gasolina, S_Llantas, Semilla);
		Carro_Agua = new Carro(AreaInsumos, S_Agua, "CARRO 1 (Agua)", Semilla);
		Carro_Aceite = new Carro(AreaInsumos, S_Aceite, "CARRO 2 (Aceite)", Semilla);
		Carro_Gasolina = new Carro(AreaInsumos, S_Gasolina, "CARRO 3 (Gasolina)", Semilla);
		Carro_Llantas = new Carro(AreaInsumos, S_Llantas, "CARRO 4 (Llantas)", Semilla);
		
		S_Agua.drainPermits();     // Cuando se inicia el programa, los carros no tienen
		S_Aceite.drainPermits();   // permiso para acceder al área de insumos... solamente
		S_Gasolina.drainPermits(); // el jefe de insumos puede acceder.
		S_Llantas.drainPermits();
		
		Jefe.start();   // Iniciar los cinco hilos
		Carro_Agua.start();
		Carro_Aceite.start();
		Carro_Gasolina.start();
		Carro_Llantas.start();
	}
}
