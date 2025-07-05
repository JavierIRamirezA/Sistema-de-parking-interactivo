
Como requisito para ejecutar la aplicación, antes se debe:
1. tener los paquetes javafx, obtenible desde https://gluonhq.com/products/javafx/ y controlsfx, obtenible desde https://mvnrepository.com/artifact/org.controlsfx/controlsfx.
2. Dentro del directorio donde se encuentra la carpeta src, crear directorio con nombre lib, ubicando la carpeta javafx-sdk-XX.X.X y el archivo controlsfx-11.2.2.jar dentro del directorio lib.
3. Modificar Archivo makefile, modificando el directorio de JAVA_HOME al directorio donde se encuentra instalado Java SE Development Kit de carpeta jdk-XX dependiendo del sistema operativo y versión de jdk. Luego de esto, modificar JAVAFX_LIB y CONTROLSFX_JAR, cambiando los números escritos por la versión descargada y disponible dentro de carpeta lib. Durante la realización del proyecto se utilizó jdk version 24, javafx-sdk-24.0.1 y controlsfx-11.2.2.jar para compilación y ejecución. Considere el uso de estas versiones si encuentra problemas de ejecución.

Ahora, para compilar, ejecutar y borrar archivos compilados de este proyecto se debe, dentro del directorio donde se encuentra makefile en el proyecto:

1.Compilación: Abrir terminal y ejecutar comando "make" . Si no puede ejecutar este comando, considere la instalación de GNU Make.

2.Ejecución: Abrir terminal y ejecutar comando "make run". Este comando ejecuta una linea de compilación que verifica si las versiones de los archivos fuente son las mismas que las compiladas, si los archivos fuente fueron modificados posterior a la compilación, vuelve a compilar los archivos fuente y ejecuta el programa.

3. Limpieza: Para eliminar archivos compilados, ejecutar comando "make clean". Este comando elimina el directorio donde se crearon los archivos compilados.