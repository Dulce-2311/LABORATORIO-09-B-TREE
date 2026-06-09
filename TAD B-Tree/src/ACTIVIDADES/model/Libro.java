package ACTIVIDADES.model; // Define el paquete model para agrupar las clases que representan entidades de datos del negocio

public class Libro implements Comparable<Libro> { // Declara la clase publica Libro e implementa la interfaz Comparable parametrizada con la misma clase Libro

    private String isbn; // Declara el atributo privado isbn de tipo String para almacenar el codigo identificador unico internacional del libro
    private String titulo; // Declara el atributo privado titulo de tipo String para almacenar el nombre representativo del libro
    private String autor; // Declara el atributo privado autor de tipo String para almacenar el creador o escritor de la obra literaria
    private String editorial; // Declara el atributo privado editorial de tipo String para almacenar la empresa encargada de publicar el texto

    public Libro(String isbn) { // Define un constructor sobrecargado que recibe unicamente el ISBN para permitir búsquedas directas en el arbol B
        this.isbn = isbn; // Inicializa el atributo isbn de la instancia con el valor recibido por parametro para busquedas optimas
        this.titulo = ""; // Asigna una cadena vacia por defecto al titulo al ser una instancia orientada a la busqueda de llaves
        this.autor = ""; // Asigna una cadena vacia por defecto al autor del libro en este constructor especifico
        this.editorial = ""; // Asigna una cadena vacia por defecto a la editorial del libro en este constructor especifico
    }

    public Libro(String isbn, String titulo, String autor, String editorial) { // Define el constructor completo para instanciar objetos con todos sus atributos reales
        this.isbn = isbn; // Inicializa el identificador unico isbn de la instancia con la cadena de texto recibida
        this.titulo = titulo; // Inicializa el nombre del libro asignando el parametro titulo al atributo de la clase
        this.autor = autor; // Inicializa el creador literario asignando el parametro autor al atributo de la clase
        this.editorial = editorial; // Inicializa la casa editora asignando el parametro editorial al atributo de la clase
    }

    public String getIsbn() { // Declara el metodo publico accesador para recuperar la cadena del codigo ISBN
        return this.isbn; // Retorna el valor string almacenado en la propiedad privada isbn
    }

    public void setIsbn(String isbn) { // Declara el metodo publico mutador para modificar el codigo ISBN de ser necesario
        this.isbn = isbn; // Sobreescribe el atributo de instancia isbn con el nuevo valor de cadena provisto
    }

    public String getTitulo() { // Declara el metodo publico accesador para obtener la denominacion o titulo del libro
        return this.titulo; // Retorna la referencia del string asociado al atributo privado titulo
    }

    public void setTitulo(String titulo) { // Declara el metodo publico mutador para redefinir el titulo del libro
        this.titulo = titulo; // Actualiza la variable de instancia titulo reemplazándola con el string del parametro
    }

    public String getAutor() { // Declara el metodo publico accesador para conocer el autor de la obra
        return this.autor; // Retorna la cadena de caracteres almacenada en el campo privado autor
    }

    public void setAutor(String autor) { // Declara el metodo publico mutador para actualizar la informacion del autor
        this.autor = autor; // Asigna el valor del parametro autor directamente a la propiedad de la clase
    }

    public String getEditorial() { // Declara el metodo publico accesador para recuperar el nombre de la editorial
        return this.editorial; // Retorna el valor actual contenido en el atributo privado editorial
    }

    public void setEditorial(String editorial) { // Declara el metodo publico mutador para alterar la empresa de edicion registrada
        this.editorial = editorial; // Reemplaza el contenido del atributo editorial con el parametro string ingresado
    }

    @Override // Indica explicitamente al compilador que se procede a redefinir el metodo de comparacion ordenado de la interfaz Comparable
    public int compareTo(Libro otro) { // Implementa el metodo obligatorio compareTo que recibe otra instancia de Libro para medir orden logico
        return this.isbn.compareTo(otro.getIsbn()); // Delega la operacion comparativa al metodo nativo compareTo de la clase String evaluando los codigos ISBN
    }

    @Override // Señala la sobreescritura del metodo base de formateo de texto heredado de la superclase Object
    public String toString() { // Redefine el metodo toString para desplegar los detalles legibles de un libro en una sola linea
        return "[ISBN: " + this.isbn + " | Titulo: " + this.titulo + " | Autor: " + this.autor + " | Editorial: " + this.editorial + "]"; // Retorna la representacion textual compacta concatenando todos los atributos del libro
    }
}