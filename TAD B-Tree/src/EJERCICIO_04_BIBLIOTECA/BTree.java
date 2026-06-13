package EJERCICIO_04_BIBLIOTECA;

import java.util.ArrayList;

public class BTree<E extends Comparable<E>> {

    private BNode<E> root;
    private int orden;

    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    // =========================
    // INSERCIÓN
    // =========================

    public void insert(E cl) {

        up = false;

        E mediana;
        BNode<E> pnew;

        mediana = push(this.root, cl);

        if (up) {

            pnew = new BNode<E>(this.orden);

            pnew.count = 1;
            pnew.keys.set(0, mediana);

            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);

            this.root = pnew;
        }
    }

    private E push(BNode<E> current, E cl) {

        int[] pos = new int[1];
        E mediana;

        if (current == null) {

            up = true;
            nDes = null;

            return cl;
        }

        boolean fl;

        fl = current.searchNode(cl, pos);

        if (fl) {

            System.out.println("Item duplicado");
            up = false;

            return null;
        }

        mediana = push(current.childs.get(pos[0]), cl);

        if (up) {

            if (current.nodeFull(this.orden - 1)) {

                mediana = dividedNode(current,
                                      mediana,
                                      pos[0]);

            } else {

                putNode(current,
                        mediana,
                        nDes,
                        pos[0]);

                up = false;
            }
        }

        return mediana;
    }

    private void putNode(BNode<E> current,
                         E cl,
                         BNode<E> rd,
                         int k) {

        int i;

        for (i = current.count - 1; i >= k; i--) {

            current.keys.set(i + 1,
                             current.keys.get(i));

            current.childs.set(i + 2,
                               current.childs.get(i + 1));
        }

        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);

        current.count++;
    }

    private E dividedNode(BNode<E> current,
                          E cl,
                          int k) {

        BNode<E> rd = nDes;

        int i;
        int posMdna;

        posMdna = (k <= this.orden / 2)
                ? this.orden / 2
                : this.orden / 2 + 1;

        nDes = new BNode<E>(this.orden);

        for (i = posMdna; i < this.orden - 1; i++) {

            nDes.keys.set(i - posMdna,
                          current.keys.get(i));

            nDes.childs.set(i - posMdna + 1,
                            current.childs.get(i + 1));
        }

        nDes.count = (this.orden - 1) - posMdna;

        current.count = posMdna;

        if (k <= this.orden / 2) {

            putNode(current,
                    cl,
                    rd,
                    k);

        } else {

            putNode(nDes,
                    cl,
                    rd,
                    k - posMdna);
        }

        E median = current.keys.get(current.count - 1);

        nDes.childs.set(0,
                        current.childs.get(current.count));

        current.count--;

        return median;
    }

    // =========================
    // EJERCICIO 1
    // SEARCH
    // =========================

    public boolean search(E cl) {

        return search(this.root, cl);
    }

    private boolean search(BNode<E> current,
                           E cl) {

        if (current == null)
            return false;

        int[] pos = new int[1];

        boolean found =
                current.searchNode(cl, pos);

        if (found) {

            System.out.println(
                cl +
                " se encuentra en el nodo "
                + current.idNode +
                " en la posición "
                + pos[0]
            );

            return true;
        }

        return search(current.childs.get(pos[0]),
                      cl);
    }

    // =========================
    // EJERCICIO 2
    // SEARCH RANGE
    // =========================

    public void searchRange(E min,
                            E max) {

        if (min.compareTo(max) > 0) {

            System.out.println(
                "Rango inválido"
            );

            return;
        }

        searchRange(this.root,
                    min,
                    max);

        System.out.println();
    }

    private void searchRange(BNode<E> current,
                             E min,
                             E max) {

        if (current == null)
            return;

        for (int i = 0;
             i < current.count;
             i++) {

            searchRange(current.childs.get(i),
                        min,
                        max);

            E key =
                    current.keys.get(i);

            if (key.compareTo(min) >= 0
                    &&
                key.compareTo(max) <= 0) {

                System.out.print(
                    key + " "
                );
            }
        }

        searchRange(
            current.childs.get(current.count),
            min,
            max
        );
    }

    // =========================
    // EJERCICIO 3
    // REMOVE
    // =========================

    public void remove(E cl) {

        ArrayList<E> elementos =
                new ArrayList<>();

        collectKeys(this.root,
                    elementos);

        if (!elementos.remove(cl)) {

            System.out.println(
                "La clave no existe"
            );

            return;
        }

        this.root = null;

        for (E item : elementos) {

            insert(item);
        }

        System.out.println(
            "Clave eliminada: "
            + cl
        );
    }

    private void collectKeys(
            BNode<E> current,
            ArrayList<E> lista) {

        if (current == null)
            return;

        for (int i = 0;
             i < current.count;
             i++) {

            collectKeys(
                current.childs.get(i),
                lista
            );

            lista.add(
                current.keys.get(i)
            );
        }

        collectKeys(
            current.childs.get(current.count),
            lista
        );
    }

    // =========================
    // MOSTRAR ÁRBOL
    // =========================

    @Override
    public String toString() {

        if (isEmpty()) {

            return "BTree is empty...";
        }

        return writeTree(this.root);
    }

    private String writeTree(
            BNode<E> current) {

        if (current == null) {

            return "";
        }

        StringBuilder sb =
                new StringBuilder();

        sb.append(
            current.toString()
        );

        sb.append("\n");

        for (int i = 0;
             i <= current.count;
             i++) {

            sb.append(
                writeTree(
                    current.childs.get(i)
                )
            );
        }

        return sb.toString();
    }

    // =========================
// CANTIDAD DE ELEMENTOS
// =========================

    public int size() {

        return size(this.root);
    }

    private int size(BNode<E> current) {

        if (current == null)
            return 0;

        int total = current.count;

        for (int i = 0; i <= current.count; i++) {

            total += size(current.childs.get(i));
        }

        return total;
    }

    // =========================
    // ALTURA DEL ÁRBOL
    // =========================

    public int height() {

        return height(this.root);
    }

    private int height(BNode<E> current) {

        if (current == null)
            return 0;

        if (current.childs.get(0) == null)
            return 1;

        return 1 + height(current.childs.get(0));
    }
    
    // =========================
    // RECORRIDO INORDER
    // =========================

    public void showOrdered() {

        showOrdered(this.root);
    }

    private void showOrdered(BNode<E> current) {

        if (current == null)
            return;

        for (int i = 0; i < current.count; i++) {

            showOrdered(current.childs.get(i));

            System.out.println(
                    current.keys.get(i)
            );
        }

        showOrdered(
                current.childs.get(current.count)
        );
    }

    // =========================
    // BÚSQUEDA CON RECORRIDO
    // =========================

    public boolean searchWithPath(E cl) {

        return searchWithPath(this.root, cl);
    }

    private boolean searchWithPath(BNode<E> current,
                                E cl) {

        if (current == null)
            return false;

        System.out.println(
                "Visitando nodo "
                + current.idNode
        );

        int[] pos = new int[1];

        boolean found =
                current.searchNode(cl, pos);

        if (found) {

            System.out.println(
                    "Encontrado en nodo "
                    + current.idNode
            );

            return true;
        }

        return searchWithPath(
                current.childs.get(pos[0]),
                cl
        );
    }
}
