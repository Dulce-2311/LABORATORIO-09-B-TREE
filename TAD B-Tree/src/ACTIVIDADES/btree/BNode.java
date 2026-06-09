package ACTIVIDADES.btree; // Define el paquete btree para agrupar las clases que conforman la estructura interna del Arbol B

import java.util.ArrayList; // Importa la clase ArrayList para manejar colecciones dinamicas de elementos
import java.util.List; // Importa la interfaz List para definir variables de tipo lista abstracta

public class BNode<E extends Comparable<E>> { // Declara la clase publica BNode utilizando tipos genericos acotados a objetos que implementen la interfaz Comparable

    private static int idCounter = 0; // Declara una variable estatica entera que funciona como contador global para asignar identificadores unicos a cada nodo creado
    private int idNode; // Declara una variable de instancia entera para almacenar el identificador unico de este nodo en particular
    private List<E> keys; // Declara una lista dinamica llamada keys que almacenara las claves de datos de tipo generico E guardadas en el nodo
    private List<BNode<E>> childs; // Declara una lista dinamica llamada childs que contendra las referencias a los nodos hijos de este nodo
    private BNode<E> parent; // Declara una referencia de tipo BNode llamada parent para apuntar directamente al nodo padre de este nodo

    public BNode() { // Define el constructor publico por defecto de la clase BNode para inicializar las propiedades de un nodo vacio
        this.idNode = ++idCounter; // Incrementa el contador global idCounter en uno y asigna el nuevo valor como el identificador unico de este nodo
        this.keys = new ArrayList<>(); // Instancia un nuevo ArrayList vacio para inicializar la lista de claves asociadas a este nodo
        this.childs = new ArrayList<>(); // Instancia un nuevo ArrayList vacio para inicializar la lista de subarboles o nodos hijos de este nodo
        this.parent = null; // Inicializa la referencia al nodo padre como nula indicando que inicialmente no tiene un nivel superior asociado
    }

    public int getIdNode() { // Declara un metodo publico que retorna un entero para obtener el identificador de este nodo
        return this.idNode; // Devuelve el valor almacenado en el atributo privado idNode
    }

    public List<E> getKeys() { // Declara un metodo publico que retorna una lista de elementos genericos para acceder a las claves del nodo
        return this.keys; // Devuelve la referencia de la lista dinamica keys
    }

    public void setKeys(List<E> keys) { // Declara un metodo publico que recibe una lista para modificar completamente el conjunto de claves del nodo
        this.keys = keys; // Asigna la lista recibida por parametro al atributo privado keys de la instancia
    }

    public List<BNode<E>> getChilds() { // Declara un metodo publico que retorna una lista de nodos para acceder a los subarboles hijos
        return this.childs; // Devuelve la referencia de la lista dinamica childs
    }

    public void setChilds(List<BNode<E>> childs) { // Declara un metodo publico que recibe una lista de nodos para redefinir los hijos de este nodo
        this.childs = childs; // Asigna la lista de nodos hijos recibida por parametro al atributo privado childs de la instancia
    }

    public BNode<E> getParent() { // Declara un metodo publico que retorna un nodo para conocer el elemento padre del nodo actual
        return this.parent; // Devuelve la referencia almacenada en el atributo privado parent
    }

    public void setParent(BNode<E> parent) { // Declara un metodo publico que recibe un nodo para actualizar el enlace hacia el nodo padre superior
        this.parent = parent; // Asigna el nodo recibido por parametro al atributo privado parent de la instancia
    }

    @Override // Indica al compilador que se esta sobrescribiendo un metodo heredado de la clase base Object
    public String toString() { // Declara el metodo publico toString que devuelve una cadena de texto representando el estado del objeto
        StringBuilder sb = new StringBuilder(); // Instancia un objeto StringBuilder para construir de forma eficiente la cadena formateada de salida
        sb.append("Id.Nodo: ").append(this.idNode).append(" | "); // Adiciona la etiqueta Id.Nodo seguida del numero identificador y un separador visual
        sb.append("Claves: ").append(this.keys.toString()).append(" | "); // Convierte e integra la representacion en texto de la lista de claves encerradas entre corchetes
        sb.append("Id.Padre: ").append(this.parent != null ? this.parent.getIdNode() : "null").append(" | "); // Evalua de forma ternaria si existe padre para anexar su ID o el texto null en su defecto
        sb.append("Id.Hijos: ["); // Añade la etiqueta inicial para listar los identificadores de los nodos de nivel inferior
        for (int i = 0; i < this.childs.size(); i++) { // Inicia un bucle for estructurado para recorrer secuencialmente cada uno de los elementos dentro de la lista de hijos
            sb.append(this.childs.get(i).getIdNode()); // Extrae el nodo hijo en la posicion i y concatena su identificador unico al string en construccion
            if (i < this.childs.size() - 1) { // Verifica mediante una condicion logica si el elemento actual no es el ultimo de la coleccion de hijos
                sb.append(", "); // Añade una coma y un espacio como separador intermedio entre los identificadores de los hijos vecinos
            }
        }
        sb.append("]"); // Agrega el corchete de cierre que delimita la coleccion de identificadores de nodos hijos
        return sb.toString(); // Transforma el contenido acumulado en el StringBuilder en un objeto de tipo String convencional y lo retorna
    }
}