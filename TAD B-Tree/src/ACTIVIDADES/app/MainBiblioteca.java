package ACTIVIDADES.app; // Define el paquete app para organizar las clases que controlan el flujo de ejecucion de la aplicacion

import java.io.BufferedReader; // Importa BufferedReader para efectuar lecturas de texto eficientes desde flujos de archivos
import java.io.FileReader; // Importa FileReader para abrir canales de lectura vinculados a archivos fisicos en el disco
import java.io.IOException; // Importa IOException para manejar fallos imprevistos de entrada o salida de datos
import java.util.Scanner; // Importa Scanner para capturar los datos ingresados por el usuario mediante el teclado

import ACTIVIDADES.btree.BTree;
import ACTIVIDADES.exception.ItemDuplicatedException;
import ACTIVIDADES.exception.ItemNotFoundException;
import ACTIVIDADES.model.Libro;

public class MainBiblioteca { // Declara la clase publica MainBiblioteca que contiene el punto de entrada de la aplicacion

    public static void main(String[] args) { // Define el metodo estatico main que inicia formalmente la ejecucion del programa en Java
        BTree<Libro> biblioteca = new BTree<>(4); // Instancia un Arbol B de orden 4 parametrizado con la entidad Libro cumpliendo la guia
        cargarLibrosDesdeArchivo(biblioteca, "libros.txt"); // Invoca al metodo de carga masiva pasandole el arbol y la ruta del archivo de texto
        Scanner scanner = new Scanner(System.in); // Instancia un objeto Scanner conectado al flujo de entrada estandar para leer datos del usuario
        int opcion = 0; // Declara e inicializa la variable entera opcion para almacenar la seleccion del menu interactivo
        do { // Inicia un bucle de control do-while para mantener el menu activo hasta que el usuario decida salir
            System.out.println("\n=== SISTEMA DE GESTION DE BIBLIOTECA (TAD B-TREE) ==="); // Imprime el titulo principal del menu por consola
            System.out.println("1. Buscar Libro por ISBN"); // Imprime la opcion para efectuar busquedas exactas por codigo identificador
            System.out.println("2. Buscar Libros en un Rango de ISBNs"); // Imprime la opcion para consultas por intervalo ordenado
            System.out.println("3. Dar de Baja un Libro (Eliminar)"); // Imprime la opcion para remover registros usando el balanceo del arbol
            System.out.println("4. Mostrar Estructura Completa del Arbol B"); // Imprime la opcion para visualizar el volcado textual de los nodos
            System.out.println("5. Salir"); // Imprime la alternativa para concluir la ejecucion del programa secuencial
            System.out.print("Seleccione una opcion: "); // Imprime una peticion invitando al usuario a digitar su eleccion
            if (scanner.hasNextInt()) { // Evalua si el dato proximo en el flujo del teclado corresponde a un valor entero valido
                opcion = scanner.nextInt(); // Lee y asigna el entero capturado a la variable de control opcion
                scanner.nextLine(); // Consume el caracter de salto de linea remanente en el buffer del escaner
                switch (opcion) { // Estructura un selector switch para bifurcar el flujo de acuerdo al numero de opcion elegido
                    case 1: // Bloque operativo para la busqueda exacta de un libro
                        System.out.print("Ingrese el ISBN a buscar: "); // Solicita al usuario el codigo de barras o identificador string
                        String isbnBuscar = scanner.nextLine(); // Captura la linea de texto completa ingresada por el teclado
                        Libro libroBuscar = new Libro(isbnBuscar); // Crea un objeto Libro auxiliar inicializado unicamente con la clave buscada
                        biblioteca.search(libroBuscar); // Llama al metodo search del arbol el cual imprimira internamente el ID del nodo y posicion
                        break; // Interrumpe el switch impidiendo la ejecucion del caso consecutivo
                    case 2: // Bloque operativo para la busqueda por intervalos
                        System.out.print("Ingrese el ISBN minimo del rango: "); // Solicita el limite inferior de la consulta
                        String min = scanner.nextLine(); // Almacena el limite alfanumerico inferior
                        System.out.print("Ingrese el ISBN maximo del rango: "); // Solicita el limite superior de la consulta
                        String max = scanner.nextLine(); // Almacena el limite alfanumerico superior
                        biblioteca.searchRange(new Libro(min), new Libro(max)); // Invoca al metodo searchRange pasandole los dos libros comodines limites
                        break; // Termina la ejecucion de la seccion de rango del selector
                    case 3: // Bloque operativo para procesar la baja de un libro
                        System.out.print("Ingrese el ISBN del libro a eliminar: "); // Solicita la clave del documento a retirar de las estanterias
                        String isbnEliminar = scanner.nextLine(); // Captura el string del ISBN desde el teclado
                        try { // Abre un bloque try para aislar potenciales fallos por la ausencia del elemento a borrar
                            biblioteca.remove(new Libro(isbnEliminar)); // Llama al metodo remove del arbol B enviando el libro con el ISBN indicado
                            System.out.println("El libro con ISBN " + isbnEliminar + " fue eliminado exitosamente."); // Muestra un mensaje confirmando el exito de la baja
                        } catch (ItemNotFoundException e) { // Captura especificamente la excepcion personalizada si el ISBN no existia en el arbol
                            System.out.println("Error: " + e.getMessage()); // Imprime en la consola el texto descriptivo del error almacenado en la excepcion
                        }
                        break; // Finaliza el bloque del caso de eliminacion
                    case 4: // Bloque operativo para auditar la topologia del arbol B
                        System.out.println("\n--- VISTA DE NODOS DEL ARBOL B ---"); // Imprime una etiqueta de encabezado de depuracion
                        System.out.print(biblioteca.toString()); // Invoca de manera explicita al toString del arbol desplegando el estado de cada nodo
                        break; // Concluye la ejecucion del caso de impresion de estructura
                    case 5: // Bloque operativo para la salida del programa
                        System.out.println("Cerrando el sistema de la biblioteca. ¡Adios!"); // Muestra una notificacion de despedida en consola
                        break; // Rompe el flujo del switch para este caso final
                    default: // Clausula por defecto ejecutada si el entero ingresado no coincide con ninguna opcion listada
                        System.out.println("Opcion no valida. Intente nuevamente."); // Advierte al usuario sobre la seleccion de un codigo erroneo
                }
            } else { // Se activa si el usuario ingresa datos no numericos como letras o caracteres especiales
                System.out.println("Error: Debe ingresar un numero entero."); // Imprime una advertencia de tipo de dato incompatible
                scanner.nextLine(); // Limpia completamente el residuo de caracteres del escaner para evitar bucles infinitos
                opcion = 0; // Resetea la variable opcion a cero para forzar una nueva iteracion del menu
            }
        } while (opcion != 5); // Evalua la condicion del bucle continuando con la ejecucion si la opcion es diferente de cinco
        scanner.close(); // Clausura el objeto Scanner liberando de forma segura los recursos del sistema operativo
    }

    private static void cargarLibrosDesdeArchivo(BTree<Libro> arbol, String rutaArchivo) { // Metodo auxiliar estatico para automatizar la lectura y parseo del archivo de texto
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) { // Abre un flujo BufferedReader bajo el esquema try-with-resources para el auto-cierre del archivo
            String linea; // Declara una variable String para retener transitoriamente cada linea leida del archivo plano
            int cargados = 0; // Inicializa un contador entero en cero para contabilizar las inserciones exitosas
            while ((linea = br.readLine()) != null) { // Itera secuencialmente leyendo el archivo linea por linea hasta alcanzar el final del documento plano
                if (linea.trim().isEmpty()) { // Evalua mediante filtrado si la linea en proceso carece de caracteres o esta vacia
                    continue; // Omite el procesamiento de la linea en blanco saltando inmediatamente a la proxima iteracion
                }
                String[] partes = linea.split(","); // Fracciona la cadena de texto usando la coma como caracter delimitador oficial
                if (partes.length == 4) { // Verifica estructuralmente que la linea contenga exactamente los cuatro atributos demandados por el modelo
                    String isbn = partes[0].trim(); // Extrae el ISBN removiendo espacios en blanco superfluos en los extremos
                    String titulo = partes[1].trim(); // Extrae el titulo limpiando espacios en blanco residuales
                    String autor = partes[2].trim(); // Extrae el nombre del autor removiendo espaciados innecesarios
                    String editorial = partes[3].trim(); // Extrae la casa editora aplicando la funcion trim
                    Libro nuevoLibro = new Libro(isbn, titulo, autor, editorial); // Instancia un nuevo objeto Libro pasandole los cuatro valores limpios al constructor
                    try { // Abre un bloque try interno para gestionar excepciones individuales por datos repetidos sin tumbar la carga masiva
                        arbol.insert(nuevoLibro); // Intenta añadir el objeto Libro dentro de los nodos correspondientes del Arbol B
                        cargados++; // Incrementa en una unidad el registro de libros guardados satisfactoriamente
                    } catch (ItemDuplicatedException e) { // Captura el error en caso de que el ISBN ya hubiese sido cargado previamente
                        System.out.println("Aviso al cargar archivo: El libro con ISBN " + isbn + " ya existe. Se omitio."); // Imprime una alerta detallando la colision de claves encontrada
                    }
                }
            }
            System.out.println("Carga inicial finalizada con exito. Se registraron " + cargados + " libros en el Arbol B."); // Informa el total consolidado de libros cargados al inicializar la aplicacion
        } catch (IOException e) { // Captura fallos criticos del sistema de archivos como la ausencia del archivo libros.txt
            System.out.println("Error critico al leer el archivo de base de datos: " + e.getMessage()); // Imprime una alerta notificando la imposibilidad de abrir la fuente de datos
            System.out.println("El Arbol B iniciara vacio."); // Advierte que el programa operara con una estructura vacia de forma provisional
        }
    }
}