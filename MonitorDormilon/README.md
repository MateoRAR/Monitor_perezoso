# Monitor Dormilón - Simulación de Concurrencia

Este proyecto implementa la solución al problema del "Monitor Dormilón" usando hilos en Java y semáforos de `java.util.concurrent.Semaphore`.

## Descripción del Problema

El departamento de la Universidad dispone de un monitor que ayuda a los estudiantes en diferentes tareas de sus cursos. La oficina del monitor tiene solamente espacio para un estudiante a la vez, pero hay tres sillas en el corredor para esperarlo.

### Comportamiento:

- Cuando no hay estudiantes esperando, el monitor duerme una siesta
- Si llega un estudiante y encuentra al monitor durmiendo, debe despertarlo
- Si llega un estudiante y encuentra al monitor atendiendo a otro, se sienta en una silla del corredor y espera
- Si no hay sillas libres, el estudiante se va y regresa más tarde
- Los estudiantes alternan entre programar en la sala de cómputo y buscar ayuda del monitor

## Estructura del Proyecto

### Clases Principales:

1. **Monitor.java**: Implementa el hilo del monitor que:

   - Duerme cuando no hay estudiantes
   - Se despierta cuando un estudiante lo necesita
   - Atiende a los estudiantes en orden de llegada
   - Vuelve a dormir cuando no hay más estudiantes

2. **Estudiante.java**: Implementa el hilo de cada estudiante que:

   - Programa en la sala de cómputo por tiempo aleatorio
   - Busca ayuda del monitor
   - Se sienta en una silla si la oficina está ocupada
   - Se va y regresa si no hay sillas disponibles

3. **MonitorDormilon.java**: Clase principal que:
   - Crea y coordina todos los semáforos
   - Inicia los hilos del monitor y estudiantes
   - Maneja la entrada de parámetros

### Semáforos Utilizados:

- **despertarMonitor**: Para despertar al monitor dormido (inicializado en 0)
- **sillasDisponibles**: Controla las 3 sillas del corredor (inicializado en 3)
- **oficinaOcupada**: Controla el acceso a la oficina (inicializado en 1)

## Compilación y Ejecución

### Compilar:

```bash
compilar.bat
```

### Ejecutar:

```bash
ejecutar.bat num_estudiantes semilla
```

### Ejemplo:

```bash
ejecutar.bat 5 12345
```

## Parámetros

- **num_estudiantes**: Número de estudiantes que participarán en la simulación
- **semilla**: Semilla para el generador de números aleatorios (para reproducibilidad)

## Características de la Implementación

- **Sincronización**: Usa semáforos para coordinar el acceso a recursos compartidos
- **Cola de espera**: Los estudiantes se atienden en orden de llegada
- **Tiempo aleatorio**: Simula tiempos realistas de programación y atención
- **Manejo de recursos**: Controla correctamente las 3 sillas del corredor
- **Despertar del monitor**: Implementa correctamente el mecanismo de despertar

## Salida del Programa

El programa muestra en tiempo real:

- Estado del monitor (durmiendo/despierto)
- Actividades de cada estudiante (programando/buscando ayuda)
- Uso de las sillas del corredor
- Proceso de atención de estudiantes
- Tiempos de espera y atención

## Detener la Simulación

Presiona `Ctrl+C` para detener la simulación en cualquier momento.
