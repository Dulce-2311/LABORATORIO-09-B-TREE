package ACTIVIDADES.exception; // Especifica la ruta del paquete exception para agrupar y reutilizar las clases de error en todo el proyecto

public class ItemDuplicatedException extends Exception { // Declara una clase publica llamada ItemDuplicatedException que extiende de Exception para implementar una estructura de error personalizada ante datos repetidos

    public ItemDuplicatedException() { // Define el constructor publico sin argumentos de la clase para crear instancias de la excepcion con los valores iniciales predeterminados
        super(); // Llama al constructor por defecto de la superclase Exception mediante la palabra clave super para configurar el objeto de excepcion basico
    }

    public ItemDuplicatedException(String message) { // Define el constructor publico que acepta una cadena de texto message para personalizar la descripcion del problema detectado durante la ejecucion
        super(message); // Transfiere la cadena message al constructor de la clase padre Exception usando super para que quede registrada como el mensaje oficial del error
    }
}
