@echo off
echo Compilando Monitor Dormilon...

if not exist bin mkdir bin

javac -d bin src\*.java

if %errorlevel% equ 0 (
    echo Compilacion exitosa!
    echo.
    echo Para ejecutar el programa:
    echo java -cp bin MonitorDormilon num_estudiantes semilla
    echo.
    echo Ejemplo:
    echo java -cp bin MonitorDormilon 5 12345
) else (
    echo Error en la compilacion!
)

pause
