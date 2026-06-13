package EJERCICIO_04_BIBLIOTECA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Biblioteca {

    private BTree<Libro> arbol;

    public Biblioteca() {
        arbol = null;
    }

    // Cargar libros desde archivo
    public void cargarDesdeArchivo(String nombreArchivo) {

        try {

            BufferedReader br =
                    new BufferedReader(
                            new FileReader(nombreArchivo));

            String linea;

            // Primera línea = orden del árbol
            linea = br.readLine();

            int orden =
                    Integer.parseInt(linea);

            arbol = new BTree<>(orden);

            while ((linea = br.readLine()) != null) {

                String[] datos =
                        linea.split(",");

                String isbn = datos[0];
                String titulo = datos[1];
                String autor = datos[2];
                int anio =
                        Integer.parseInt(datos[3]);

                Libro libro =
                        new Libro(
                                isbn,
                                titulo,
                                autor,
                                anio
                        );

                arbol.insert(libro);
            }

            br.close();

            System.out.println(
                    "Archivo cargado correctamente."
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al leer archivo."
            );

            e.printStackTrace();
        }
    }

    // Agregar libro
    public void agregarLibro(Libro libro) {

        arbol.insert(libro);
    }

    // Buscar libro por ISBN
    public boolean buscarLibro(String isbn) {

        Libro temp =
                new Libro(
                        isbn,
                        "",
                        "",
                        0
                );

        return arbol.search(temp);
    }

    // Eliminar libro por ISBN
    public void eliminarLibro(String isbn) {

        Libro temp =
                new Libro(
                        isbn,
                        "",
                        "",
                        0
                );

        arbol.remove(temp);
    }

    // Mostrar árbol
    public void mostrarLibros() {

        System.out.println(arbol);
    }

        // Mostrar libros ordenados
    public void mostrarLibrosOrdenados() {

        arbol.showOrdered();
    }

    // Cantidad total de libros
    public int cantidadLibros() {

        return arbol.size();
    }

    // Altura del árbol
    public int alturaArbol() {

        return arbol.height();
    }

    // Búsqueda mostrando recorrido
    public boolean buscarLibroConRecorrido(String isbn) {

        Libro temp =
                new Libro(
                        isbn,
                        "",
                        "",
                        0
                );

        return arbol.searchWithPath(temp);
    }
}