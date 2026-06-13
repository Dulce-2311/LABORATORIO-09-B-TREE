package EJERCICIO_04_BIBLIOTECA;

public class Libro implements Comparable<Libro> {

    private String isbn;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(String isbn,
                 String titulo,
                 String autor,
                 int anio) {

        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnio() {
        return anio;
    }

    @Override
    public int compareTo(Libro otro) {

        return this.isbn.compareTo(otro.isbn);
    }

    @Override
    public String toString() {

        return isbn + " | "
             + titulo + " | "
             + autor + " | "
             + anio;
    }
}