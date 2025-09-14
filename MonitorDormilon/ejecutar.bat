@echo off
echo Ejecutando Monitor Dormilon...

if not exist bin (
    echo Error: No se encontraron archivos compilados.
    echo Ejecute primero compilar.bat
    pause
    exit /b 1
)

if "%1"=="" (
    echo Sintaxis: ejecutar.bat num_estudiantes semilla
    echo Ejemplo: ejecutar.bat 5 12345
    pause
    exit /b 1
)

java -cp bin MonitorDormilon %1 %2

pause
