package EJERCICIO_04_BIBLIOTECA;

public class AppBiblioteca {

    public static void main(String[] args) {

        Biblioteca bib =
                new Biblioteca();

        bib.cargarDesdeArchivo(
                "TAD B-Tree/biblioteca.txt"
        );

        System.out.println();

        System.out.println(
                "=== LIBROS CARGADOS ==="
        );

        bib.mostrarLibros();

        System.out.println();

        System.out.println(
                "=== LIBROS ORDENADOS ==="
        );

        bib.mostrarLibrosOrdenados();

        System.out.println();

        System.out.println(
                "Cantidad de libros: "
                + bib.cantidadLibros()
        );

        System.out.println(
                "Altura del árbol: "
                + bib.alturaArbol()
        );

        System.out.println();

        System.out.println(
                "=== BUSQUEDA CON RECORRIDO ==="
        );

        bib.buscarLibroConRecorrido(
                "9780134685991"
        );
    }
}