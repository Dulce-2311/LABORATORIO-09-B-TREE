package EJERCICIOS_01_02_03;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {

    // Claves almacenadas en el nodo
    protected ArrayList<E> keys;

    // Referencias a los hijos
    protected ArrayList<BNode<E>> childs;

    // Cantidad real de claves almacenadas
    protected int count;

    // Identificador único del nodo
    protected int idNode;

    // Contador para generar ids únicos
    private static int nextId = 1;

    public BNode(int n) {

        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<BNode<E>>(n);

        this.count = 0;

        this.idNode = nextId++;

        // Inicializar listas
        for (int i = 0; i < n; i++) {

            this.keys.add(null);
            this.childs.add(null);
        }
    }

    // Verifica si el nodo está lleno
    public boolean nodeFull(int maxKeys) {

        return count == maxKeys;
    }

    // Verifica si el nodo está vacío
    public boolean nodeEmpty() {

        return count == 0;
    }

    /*
     * Busca una clave dentro del nodo.
     *
     * Si la encuentra:
     *      retorna true
     *      pos[0] contiene la posición de la clave
     *
     * Si no la encuentra:
     *      retorna false
     *      pos[0] indica por qué hijo continuar
     */
    public boolean searchNode(E cl, int[] pos) {

        pos[0] = 0;

        while (pos[0] < count
                && cl.compareTo(keys.get(pos[0])) > 0) {

            pos[0]++;
        }

        if (pos[0] < count
                && cl.compareTo(keys.get(pos[0])) == 0) {

            return true;
        }

        return false;
    }

    // Devuelve las claves almacenadas en el nodo
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Nodo ");
        sb.append(idNode);
        sb.append(": (");

        for (int i = 0; i < count; i++) {

            sb.append(keys.get(i));

            if (i < count - 1) {
                sb.append(", ");
            }
        }

        sb.append(")");

        return sb.toString();
    }
}