package EJERCICIOS_01_02_03;

public class App {

    public static void main(String[] args) {

        BTree<Integer> tree = new BTree<>(4);

        tree.insert(50);
        tree.insert(20);
        tree.insert(70);
        tree.insert(10);
        tree.insert(30);
        tree.insert(60);
        tree.insert(80);
        tree.insert(25);
        tree.insert(27);
        tree.insert(26);

        System.out.println("=== ARBOL ===");
        System.out.println(tree);

        System.out.println("=== SEARCH ===");
        System.out.println(tree.search(25));
        System.out.println(tree.search(99));

        System.out.println();

        System.out.println("=== SEARCH RANGE ===");
        tree.searchRange(20, 60);

        System.out.println();

        System.out.println("=== REMOVE ===");
        tree.remove(25);

        System.out.println(tree);
    }
}