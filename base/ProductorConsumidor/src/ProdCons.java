// Implementación de productor y conmsumidor en Java
// Emplea semáforos para sincronización

import java.util.concurrent.Semaphore; 

class Cola { 
	// an item 
	int item; 

	// semConsumidor inicializado con 0 permisos 
	// para asegurarnos de que insertar() se ejecuta primero 
	static Semaphore semConsumidor = new Semaphore(0); 

	static Semaphore semProductor = new Semaphore(1); 

	// Obtener ítem del buffer
	void obtener() 
	{ 
		try { 
			// Antes de poder obtener el ítem, 
			// necesitamos el permiso de semConsumidor 
			semConsumidor.acquire(); 
		} 
		catch (InterruptedException e) { 
			System.out.println("InterruptedException capturada"); 
		} 

		// El consumidor consume el ítem obtenido
		System.out.println("El consumidor consumió el ítem : " + item); 

		// Después de que el consumidor consume el ítem,
		// avisa al productor. 
		semProductor.release(); 
	} 

	// Insertar un ítem en el buffer
	void insertar(int item) 
	{ 
		try { 
			// Antes de que el productor pueda producir un ítem,
			// debe pedir permiso a semProductor
			semProductor.acquire(); 
		} 
		catch (InterruptedException e) { 
			System.out.println("InterruptedException capturada"); 
		} 

		// El productor produce el ítem 
		this.item = item; 

		System.out.println("El productor produjo el ítem : " + item); 

		// Después de que el productor produce el ítem, 
		// notifica al consumidor 
		semConsumidor.release(); 
	} 
} 

// Clase Productor
class Productor implements Runnable { 
	Cola cola; 
	Productor(Cola cola) 
	{ 
		this.cola = cola; 
		new Thread(this, "Productor").start(); 
	} 

	public void run() 
	{ 
		for (int i = 0; i < 5; i++) 
			// El productor inserta ítems 
			cola.insertar(i); 
	} 
} 

// Clase Consumidor
class Consumidor implements Runnable { 
	Cola cola; 
	Consumidor(Cola cola) 
	{ 
		this.cola = cola; 
		new Thread(this, "Consumidor").start(); 
	} 

	public void run() 
	{ 
		for (int i = 0; i < 5; i++) 
			// El consumidor obtiene ítems
			cola.obtener(); 
	} 
} 

// Clase principal
class ProdCons { 
	public static void main(String args[]) 
	{ 
		// Crear el buffer
		Cola cola = new Cola(); 

		// Iniciar hilo consumidor
		new Consumidor(cola); 

		// Iniciar hilo productor 
		new Productor(cola); 
	} 
} 
