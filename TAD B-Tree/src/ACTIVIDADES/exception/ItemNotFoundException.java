package ACTIVIDADES.exception; // Define el paquete al que pertenece esta clase, permitiendo organizar el codigo de manera modular

public class ItemNotFoundException extends Exception { // Declara una clase publica llamada ItemNotFoundException que hereda de la clase base Exception para convertirse en una excepcion comprobada

    public ItemNotFoundException() { // Declara el constructor por defecto (sin parametros) de la clase para permitir instanciar la excepcion sin un mensaje especifico
        super(); // Invoca de manera explicita al constructor vacio de la clase padre Exception para inicializar la estructura base de la excepcion
    }

    public ItemNotFoundException(String message) { // Declara un constructor sobrecargado que recibe un parametro de tipo String llamado message para asignar un texto descriptivo al error
        super(message); // Invoca al constructor de la clase padre Exception pasando el parametro message para almacenar el mensaje detallado del error que se mostrara en la consola o traza
    }
}