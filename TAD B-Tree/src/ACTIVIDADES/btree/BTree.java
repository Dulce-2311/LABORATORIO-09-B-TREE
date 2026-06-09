package ACTIVIDADES.btree; // Define el paquete btree para agrupar las clases que conforman la estructura interna del Arbol B

import java.util.ArrayList; // Importa la clase ArrayList para gestionar arreglos dinamicos de objetos
import java.util.List; // Importa la interfaz List para utilizar colecciones de elementos indexados

import ACTIVIDADES.exception.ItemDuplicatedException;
import ACTIVIDADES.exception.ItemNotFoundException;

public class BTree<E extends Comparable<E>> { // Declara la clase publica BTree que maneja tipos genericos acotados por la interfaz Comparable

    private BNode<E> root; // Declara el atributo privado root de tipo BNode para almacenar la referencia al nodo raiz del arbol B
    private int order; // Declara el atributo privado entero order para almacenar el orden maximo de ramificacion permitido para los nodos

    public BTree(int order) { // Define el constructor de la clase que recibe el orden maximo del arbol por parametro
        this.order = order; // Inicializa el atributo order de la instancia con el valor entero recibido por parametro
        this.root = null; // Inicializa la raiz en nulo indicando que el arbol B se encuentra inicialmente vacio
    }

    public boolean isEmpty() { // Define un metodo publico para verificar si la estructura carece de nodos
        return this.root == null; // Devuelve verdadero si la referencia root es igual a nulo, o falso en caso contrario
    }

    public void insert(E cl) throws ItemDuplicatedException { // Define el metodo publico de insercion que recibe una clave y propaga la excepcion por duplicidad
        boolean[] empujarArriba = new boolean[1]; // Instancia un arreglo booleano de un elemento para emular el paso por referencia del estado de division
        E[] clvSubida = (E[]) new Comparable[1]; // Instancia un arreglo de tipo Comparable para capturar por referencia la clave mediana promovida
        BNode<E>[] nodoDerecho = new BNode[1]; // Instancia un arreglo de nodos para capturar por referencia el nuevo nodo hermano derecho generado
        boolean resultado = push(this.root, cl, empujarArriba, clvSubida, nodoDerecho); // Invoca al metodo recursivo push para intentar insertar la clave en el arbol
        if (resultado) { // Evalua si la operacion fue exitosa y requiere procesar el estado de desbordamiento en la parte superior
            if (empujarArriba[0]) { // Condiciona si el indicador de desbordamiento de la raiz quedo activo tras la recursion
                BNode<E> nuevaRaiz = new BNode<>(); // Instancia un nuevo nodo que se convertira en la nueva raiz del arbol B
                nuevaRaiz.getKeys().add(clvSubida[0]); // Añade la clave mediana promovida a la lista de claves de la nueva raiz
                nuevaRaiz.getChilds().add(this.root); // Añade la raiz antigua como el primer hijo izquierdo del nuevo nodo raiz
                nuevaRaiz.getChilds().add(nodoDerecho[0]); // Añade el nodo hermano derecho generado como el segundo hijo de la nueva raiz
                if (this.root != null) { // Verifica si existia una raiz previa antes de reconfigurar los enlaces superiores
                    this.root.setParent(nuevaRaiz); // Actualiza la referencia padre de la antigua raiz apuntando al nuevo nodo superior
                }
                if (nodoDerecho[0] != null) { // Verifica si el nuevo nodo hermano derecho es una instancia valida y no nula
                    nodoDerecho[0].setParent(nuevaRaiz); // Establece la relacion padre del nuevo nodo hermano apuntando hacia la nueva raiz
                }
                this.root = nuevaRaiz; // Asigna el nuevo nodo creado al atributo root oficial de la clase BTree
            }
        } else { // Bloque que se ejecuta si el metodo push retorna falso debido al hallazgo de un elemento repetido
            throw new ItemDuplicatedException("El item ya se encuentra en el Arbol B."); // Lanza de manera formal la excepcion personalizada por duplicidad de datos
        }
    }

    private boolean push(BNode<E> actual, E cl, boolean[] empujarArriba, E[] clvSubida, BNode<E>[] nodoDerecho) { // Metodo privado recursivo para buscar la posicion e insertar
        int[] pos = new int[1]; // Instancia un arreglo entero de un elemento para recuperar por referencia la posicion de la clave o su ruta
        if (actual == null) { // Evalua si el estado de la recursion ha alcanzado el limite inferior de las hojas
            empujarArriba[0] = true; // Activa el flag para indicarle al nivel superior que debe realizar una insercion base
            clvSubida[0] = cl; // Coloca la clave a insertar dentro del contenedor de promocion ascendente
            nodoDerecho[0] = null; // Establece el hijo derecho de esta operacion base como nulo
            return true; // Retorna verdadero para validar la viabilidad de la insercion en el nodo hoja
        }
        boolean enc = searchNode(actual, cl, pos); // Invoca a searchNode para determinar si la clave existe en el nodo actual y obtener su posicion indexada
        if (enc) { // Condiciona si la busqueda dentro del nodo arrojo un resultado positivo
            return false; // Retorna falso interrumpiendo el flujo ascendente al haber encontrado una colision por duplicidad
        }
        BNode<E> hijoSiguiente = (actual.getChilds().isEmpty()) ? null : actual.getChilds().get(pos[0]); // Selecciona el nodo hijo correcto basandose en la posicion hallada o asigna nulo si es hoja
        boolean insertado = push(hijoSiguiente, cl, empujarArriba, clvSubida, nodoDerecho); // Invoca recursivamente a push descendiendo un nivel en la jerarquia del arbol
        if (!insertado) { // Evalua si los niveles inferiores reportaron un fallo por duplicidad
            return false; // Propaga el estado falso hacia arriba para detener la cadena de ejecucion
        }
        if (empujarArriba[0]) { // Condiciona si el nivel inferior sufrio una division y requiere una insercion en el nodo actual
            if (actual.getKeys().size() < this.order - 1) { // Verifica si el nodo actual cuenta con espacio suficiente para albergar la clave promovida
                empujarArriba[0] = false; // Desactiva el indicador de empuje al poder resolver la operacion de forma local
                putNode(actual, clvSubida[0], nodoDerecho[0], pos[0]); // Invoca a putNode para insertar la clave y su respectivo hijo derecho de forma ordenada
            } else { // Se ejecuta si el nodo actual ya se encuentra lleno y demanda una operacion de division tecnica
                dividedNode(actual, clvSubida[0], nodoDerecho[0], pos[0], clvSubida, nodoDerecho); // Invoca a dividedNode para partir el nodo y promover la nueva mediana
            }
        }
        return true; // Retorna verdadero ratificando el exito de la secuencia de operaciones en este nivel
    }

    private boolean searchNode(BNode<E> actual, E cl, int[] pos) { // Metodo para localizar el indice idoneo de una clave dentro de las listas de un nodo
        pos[0] = 0; // Inicializa el indice de busqueda en la primera posicion de la coleccion de claves
        while (pos[0] < actual.getKeys().size() && actual.getKeys().get(pos[0]).compareTo(cl) < 0) { // Itera mientras el indice sea valido y las claves internas sean estrictamente menores al valor buscado
            pos[0]++; // Incrementa el indice de busqueda para inspeccionar el elemento subsecuente de la lista
        }
        if (pos[0] < actual.getKeys().size() && actual.getKeys().get(pos[0]).compareTo(cl) == 0) { // Evalua si el indice se detuvo sobre una coincidencia exacta de valores
            return true; // Retorna verdadero informando el exito del hallazgo dentro de la lista de claves del nodo actual
        }
        return false; // Retorna falso indicando que la clave no reside en este nodo y provee la posicion para la ramificacion
    }

    private void putNode(BNode<E> actual, E cl, BNode<E> rd, int pos) { // Inserta una clave y su hijo derecho en una posicion especifica manteniendo el orden
        actual.getKeys().add(pos, cl); // Añade de manera indexada la clave en la posicion indicada desplazando el resto de elementos
        if (rd != null) { // Verifica si el subarbol derecho provisto corresponde a una referencia valida
            actual.getChilds().add(pos + 1, rd); // Inserta el nodo hijo derecho en la posicion inmediata posterior a la clave agregada
            rd.setParent(actual); // Vincula al subarbol derecho actualizando su referencia de nodo superior con el nodo actual
        }
    }

    private void dividedNode(BNode<E> actual, E cl, BNode<E> rd, int pos, E[] clvSubida, BNode<E>[] nuevoNodo) { // Divide un nodo saturado en dos partes equilibradas
        int mi = (this.order) / 2; // Calcula de forma matematica el indice medio que determinara la clave que debe ser promovida
        BNode<E> hermano = new BNode<>(); // Instancia un nuevo nodo que funcionara como el hermano de la derecha tras la biparticion
        int i = mi; // Inicializa un puntero de copia en la posicion mediana calculada para iniciar la transferencia de datos
        while (i < this.order - 1) { // Recorre los elementos sobrantes situados en el flanco derecho del nodo saturado
            hermano.getKeys().add(actual.getKeys().remove(mi)); // Remueve secuencialmente la clave del nodo original y la traslada al hermano derecho
            if (!actual.getChilds().isEmpty()) { // Evalua si el nodo en proceso cuenta con una coleccion de subarboles hijos asociados
                BNode<E> hijoMovido = actual.getChilds().remove(mi + 1); // Remueve la referencia del hijo colindante del nodo original
                hermano.getChilds().add(hijoMovido); // Traspasa la referencia del hijo al arreglo del nuevo nodo hermano derecho
                hijoMovido.setParent(hermano); // Modifica el puntero superior del hijo reasignandolo al nodo hermano recien creado
            }
        }
        if (pos <= mi) { // Evalua mediante una condicion si la nueva clave en transito pertenece a la fraccion izquierda
            putNode(actual, cl, rd, pos); // Integra localmente la clave y su hijo derecho en el nodo original a traves del metodo putNode
        } else { // Determina que la posicion de insercion corresponde a la seccion fraccionaria de la derecha
            putNode(hermano, cl, rd, pos - mi - 1); // Inserta de forma ordenada la clave y su hijo derecho dentro de las colecciones del nodo hermano
        }
        clvSubida[0] = actual.getKeys().remove(actual.getKeys().size() - 1); // Extrae la ultima clave remanente del nodo original y la designa como la clave mediana promovida
        if (!actual.getChilds().isEmpty() && actual.getChilds().size() > actual.getKeys().size() + 1) { // Sincroniza la consistencia en el numero de hijos con respecto a las claves en el nodo de la izquierda
            BNode<E> hijoHuérfano = actual.getChilds().remove(actual.getChilds().size() - 1); // Desvincula el ultimo hijo sobrante del nodo original
            hermano.getChilds().add(0, hijoHuérfano); // Lo inserta en la primera posicion de la lista del nodo hermano derecho
            hijoHuérfano.setParent(hermano); // Actualiza la correspondencia del padre del hijo movil vinculandolo con el hermano
        }
        nuevoNodo[0] = hermano; // Almacena el nodo hermano derecho en el contenedor por referencia para su retorno ascendente
    }

    public boolean search(E cl) { // Implementacion oficial de la busqueda exacta requerida por la actividad 3
        int[] pos = new int[1]; // Instancia un contenedor entero para recuperar la posicion interna de la coincidencia
        BNode<E> nodoHallado = searchRecursivo(this.root, cl, pos); // Invoca a la funcion de busqueda recursiva partiendo de la raiz
        if (nodoHallado != null) { // Valida si el retorno corresponde a una instancia de nodo real y efectiva
            System.out.println("Clave encontrada en el Nodo ID: " + nodoHallado.getIdNode() + " | Posicion de lista: " + pos[0]); // Imprime el mensaje solicitado en los lineamientos informando el ID y la posicion
            return true; // Retorna verdadero certificando el hallazgo de la clave en la estructura del arbol
        }
        System.out.println("La clave " + cl + " no existe en el Arbol B."); // Muestra por consola una alerta indicando la ausencia de la clave analizada
        return false; // Retorna falso confirmando que la clave no se localiza en ningun componente del arbol
    }

    private BNode<E> searchRecursivo(BNode<E> actual, E cl, int[] pos) { // Funcion auxiliar interna para navegar recursivamente el arbol en busqueda de una clave
        if (actual == null) { // Evalua si el flujo de navegacion rebaso el nivel de las hojas sin exito
            return null; // Retorna nulo indicando que no hay mas nodos disponibles para evaluar
        }
        boolean enc = searchNode(actual, cl, pos); // Invoca a searchNode para buscar coincidencias locales y obtener el indice operativo
        if (enc) { // Si el elemento se encuentra registrado en el nodo bajo inspeccion actual
            return actual; // Retorna la instancia del nodo actual deteniendo la recursion con exito
        }
        BNode<E> hijoSiguiente = (actual.getChilds().isEmpty()) ? null : actual.getChilds().get(pos[0]); // Determina la ruta hacia el subarbol inferior correcto basandose en la posicion indexada
        return searchRecursivo(hijoSiguiente, cl, pos); // Efectua el llamado recursivo delegando la operacion al subarbol hijo escogido
    }

    public List<E> searchRange(E min, E max) { // Implementacion oficial del recorrido por rango solicitado por la actividad 3
        List<E> resultado = new ArrayList<>(); // Instancia la coleccion donde se acumularan los elementos validados dentro del rango
        System.out.println("Iniciando busqueda en rango [" + min + " , " + max + "]:"); // Imprime una cabecera en consola detallando los limites establecidos para la consulta
        searchRangeRecursivo(this.root, min, max, resultado); // Invoca al metodo recursivo de busqueda en rango inyectando la coleccion receptora
        System.out.println(); // Realiza un salto de linea estetico en consola tras concluir la visualizacion de elementos
        return resultado; // Retorna la lista con todas las claves ordenadas que cumplieron los criterios de seleccion
    }

    private void searchRangeRecursivo(BNode<E> actual, E min, E max, List<E> resultado) { // Recorrido en-orden modificado para extraer claves en un rango acotado
        if (actual == null) { // Condicion de parada que detiene el procesamiento al alcanzar un enlace nulo inferior
            return; // Interrumpe la ejecucion de este subproceso recursivo retornando al llamador anterior
        }
        int i = 0; // Inicializa un indice en cero para evaluar secuencialmente las claves e hijos del nodo
        while (i < actual.getKeys().size()) { // Bucle de control que recorre todas las claves almacenadas en el nodo actual
            E claveActual = actual.getKeys().get(i); // Recupera el elemento generico situado en la posicion i de la lista de claves
            if (!actual.getChilds().isEmpty() && claveActual.compareTo(min) >= 0) { // Evalua si es viable descender al hijo izquierdo de la clave basandose en el limite inferior
                searchRangeRecursivo(actual.getChilds().get(i), min, max, resultado); // Desciende recursivamente por la rama izquierda del indice actual
            }
            if (claveActual.compareTo(min) >= 0 && claveActual.compareTo(max) <= 0) { // Evalua mediante comparacion logica si la clave actual se encuentra dentro del rango inclusivo
                System.out.print(claveActual + " "); // Imprime la clave por consola de forma horizontal cumpliendo con el requerimiento de visualizacion
                resultado.add(claveActual); // Incorpora la clave validada en la lista dinamica de resultados finales
            }
            if (claveActual.compareTo(max) > 0) { // Optimizacion algoritmica que detecta si la clave rebaso el limite maximo permitido
                if (!actual.getChilds().isEmpty()) { // Verifica la existencia de un subarbol hijo izquierdo colindante
                    searchRangeRecursivo(actual.getChilds().get(i), min, max, resultado); // Explora dicho subarbol antes de abortar el bucle por exceso
                }
                return; // Aborta anticipadamente la exploracion del nodo actual dado que las claves siguientes seran aun mayores
            }
            i++; // Incrementa el indice de control para evaluar la clave subsiguiente en el proximo ciclo del bucle
        }
        if (!actual.getChilds().isEmpty()) { // Evalua la existencia de un ultimo subarbol hijo derecho tras haber procesado todas las claves del nodo
            searchRangeRecursivo(actual.getChilds().get(actual.getChilds().size() - 1), min, max, resultado); // Recorre de forma recursiva el ultimo subarbol hijo del nodo actual
        }
    }

    public void remove(E cl) throws ItemNotFoundException { // Implementacion oficial de la eliminacion requerida en la actividad 3
        if (isEmpty()) { // Evalua si el arbol carece de informacion antes de proceder con el algoritmo de baja
            throw new ItemNotFoundException("No se puede eliminar, el Arbol B se encuentra vacio."); // Lanza la excepcion de elemento no hallado ante un arbol sin nodos
        }
        int[] pos = new int[1]; // Crea un contenedor por referencia para capturar la ubicacion del elemento a borrar
        BNode<E> nodoObjetivo = searchRecursivo(this.root, cl, pos); // Realiza la busqueda de la clave para comprobar su existencia y obtener su nodo contenedor
        if (nodoObjetivo == null) { // Condiciona el flujo si la clave no fue localizada en ninguna zona de la estructura
            throw new ItemNotFoundException("La clave " + cl + " no se encuentra en el Arbol B por lo que no puede ser removida."); // Lanza formalmente la excepcion reportando la ausencia de la clave buscada
        }
        eliminarClave(nodoObjetivo, pos[0]); // Invoca a la rutina interna encargada de procesar los casos de eliminacion, redistribucion y fusion
        if (this.root.getKeys().isEmpty() && !this.root.getChilds().isEmpty()) { // Valida si tras el borrado la raiz quedo vacia pero posee un hijo remanente
            this.root = this.root.getChilds().get(0); // Promueve al unico hijo de la raiz como la nueva raiz oficial del arbol B
            this.root.setParent(null); // Corta la relacion ascendente de la nueva raiz configurandola en nulo
        }
        if (this.root.getKeys().isEmpty()) { // Condiciona si tras todas las contracciones el arbol se quedo completamente desprovisto de claves
            this.root = null; // Setea la raiz del arbol en nulo declarando la estructura vacia
        }
    }

    private void eliminarClave(BNode<E> nodo, int pos) { // Procesa la eliminacion distinguiendo si el nodo es hoja o requiere busqueda de sucesor
        if (nodo.getChilds().isEmpty()) { // Evalua si el nodo en cuestion es un nodo hoja (carece de coleccion de hijos)
            nodo.getKeys().remove(pos); // Borra directamente la clave de la lista de claves del nodo hoja de forma indexada
            balancear(nodo); // Invoca a la rutina balancear para corregir potenciales subdesbordamientos en el nodo hoja
        } else { // Determina que la clave a retirar reside en un nodo interno del arbol B
            BNode<E> sucesor = obtenerSucesorInorden(nodo.getChilds().get(pos + 1)); // Obtiene el sucesor en-orden navegando por la rama derecha inmediata
            E claveSucesor = sucesor.getKeys().get(0); // Toma la clave mas pequeña del nodo sucesor hallado
            nodo.getKeys().set(pos, claveSucesor); // Reemplaza la clave a eliminar por el valor de la clave del sucesor en-orden
            eliminarClave(sucesor, 0); // Invoca recursivamente el borrado de la clave copiada desde su posicion original en la hoja sucesora
        }
    }

    private BNode<E> obtenerSucesorInorden(BNode<E> actual) { // Localiza recursivamente el nodo mas a la izquierda de un subarbol dado
        while (!actual.getChilds().isEmpty()) { // Desciende continuamente por la primera posicion de hijos mientras el nodo no sea hoja
            actual = actual.getChilds().get(0); // Actualiza la referencia moviéndose hacia el primer hijo izquierdo de la coleccion
        }
        return actual; // Retorna el nodo hoja localizado que contiene al sucesor inmediato en-orden
    }

    private void balancear(BNode<E> nodo) { // Corrige la violacion de la propiedad de ocupacion minima en un nodo
        int minClaves = (this.order - 1) / 2; // Determina de manera matematica el numero minimo de claves que debe retener todo nodo (excepto raiz)
        if (nodo == this.root || nodo.getKeys().size() >= minClaves) { // Condicion de parada si el nodo es la raiz o retiene la cantidad minima requerida de claves
            return; // Finaliza el proceso de balance para este nodo al no presentar anomalias estructurales
        }
        BNode<E> padre = nodo.getParent(); // Obtiene la referencia del nodo padre para coordinar las operaciones con hermanos
        int idxHijo = padre.getChilds().indexOf(nodo); // Encuentra la posicion o indice que ocupa el nodo actual dentro del listado de hijos del padre
        if (idxHijo > 0 && padre.getChilds().get(idxHijo - 1).getKeys().size() > minClaves) { // Verifica si el hermano izquierdo existe y dispone de claves sobrantes para prestar
            prestarDelHermanoIzquierdo(nodo, padre, idxHijo); // Ejecuta el algoritmo de redistribucion tomando prestado una clave del hermano izquierdo
        } else if (idxHijo < padre.getChilds().size() - 1 && padre.getChilds().get(idxHijo + 1).getKeys().size() > minClaves) { // Verifica si el hermano derecho existe y cuenta con claves suficientes para prestar
            prestarDelHermanoDerecho(nodo, padre, idxHijo); // Ejecuta el algoritmo de redistribucion solicitando prestado una clave al hermano derecho
        } else { // Si ningun hermano dispone de elementos suficientes se procede obligatoriamente con una operacion de fusion
            if (idxHijo > 0) { // Condiciona si el nodo actual cuenta con un hermano en el flanco izquierdo valido para unirse
                fusionarNodos(padre.getChilds().get(idxHijo - 1), nodo, padre, idxHijo - 1); // Fusiona al hermano izquierdo con el nodo actual utilizando la clave intermedia del padre
            } else { // Determina que el nodo actual debe acoplarse obligatoriamente con su hermano del lado derecho
                fusionarNodos(nodo, padre.getChilds().get(idxHijo + 1), padre, idxHijo); // Fusiona al nodo actual con su hermano derecho absorbiendo la clave divisoria del padre
            }
            balancear(padre); // Invoca de forma recursiva y ascendente el balance sobre el nodo padre por si sufrio un subdesbordamiento
        }
    }

    private void prestarDelHermanoIzquierdo(BNode<E> nodo, BNode<E> padre, int idxHijo) { // Realiza una rotacion a la derecha desde el hermano izquierdo
        BNode<E> hermanoIzq = padre.getChilds().get(idxHijo - 1); // Obtiene la instancia del hermano situado a la izquierda inmediata
        E clavePadre = padre.getKeys().set(idxHijo - 1, hermanoIzq.getKeys().remove(hermanoIzq.getKeys().size() - 1)); // Remueve la ultima clave del hermano izquierdo y la intercambia con la clave divisoria del padre
        nodo.getKeys().add(0, clavePadre); // Inserta la antigua clave extraida del padre en la primera posicion de la lista del nodo actual
        if (!hermanoIzq.getChilds().isEmpty()) { // Evalua si el hermano izquierdo poseia subarboles bajo su estructura
            BNode<E> hijoMovido = hermanoIzq.getChilds().remove(hermanoIzq.getChilds().size() - 1); // Remueve el ultimo hijo del hermano de la izquierda
            nodo.getChilds().add(0, hijoMovido); // Lo inserta como el primer hijo de la coleccion del nodo actual
            hijoMovido.setParent(nodo); // Reconfigura la relacion superior del hijo movil apuntándola hacia el nodo actual
        }
    }

    private void prestarDelHermanoDerecho(BNode<E> nodo, BNode<E> padre, int idxHijo) { // Realiza una rotacion a la izquierda desde el hermano derecho
        BNode<E> hermanoDer = padre.getChilds().get(idxHijo + 1); // Obtiene la instancia del hermano situado a la derecha inmediata
        E clavePadre = padre.getKeys().set(idxHijo, hermanoDer.getKeys().remove(0)); // Remueve la primera clave del hermano derecho y la permuta con la clave intermedia del padre
        nodo.getKeys().add(clavePadre); // Añade la clave recuperada del padre al final de la lista de claves del nodo actual
        if (!hermanoDer.getChilds().isEmpty()) { // Evalua si el hermano derecho posee ramas inferiores asociadas
            BNode<E> hijoMovido = hermanoDer.getChilds().remove(0); // Remueve el primer hijo de la lista de la coleccion del hermano derecho
            nodo.getChilds().add(hijoMovido); // Lo añade al final de la lista de subarboles hijos del nodo actual
            hijoMovido.setParent(nodo); // Actualiza la propiedad padre del hijo transferido vinculandolo al nodo actual
        }
    }

    private void fusionarNodos(BNode<E> izq, BNode<E> der, BNode<E> padre, int idxClavePadre) { // Une dos nodos hermanos en una sola entidad integrando un elemento del padre
        E clavePadre = padre.getKeys().remove(idxClavePadre); // Remueve la clave divisoria intermedia de la coleccion del nodo padre
        padre.getChilds().remove(idxClavePadre + 1); // Borra la referencia redundante del hijo derecho de la coleccion del padre
        izq.getKeys().add(clavePadre); // Inserta la clave removida del padre al final de las claves del nodo izquierdo
        izq.getKeys().addAll(der.getKeys()); // Copia e integra todas las claves del nodo derecho en la lista de claves del nodo izquierdo
        if (!der.getChilds().isEmpty()) { // Evalua si el nodo de la derecha contenia referencias a subarboles inferiores
            for (BNode<E> h : der.getChilds()) { // Recorre exhaustivamente cada uno de los nodos hijos pertenecientes al nodo derecho
                izq.getChilds().add(h); // Anexa de forma secuencial cada hijo dentro de la lista de subarboles del nodo izquierdo
                h.setParent(izq); // Reasigna el puntero padre de los hijos acoplados vinculandolos al nodo izquierdo unificado
            }
        }
    }

    @Override // Indica que se esta reemplazando la implementacion por defecto del metodo toString de la superclase Object
    public String toString() { // Metodo publico para obtener el volcado completo en texto de la estructura del arbol B
        StringBuilder sb = new StringBuilder(); // Instancia un objeto StringBuilder para consolidar el texto de manera optima
        if (isEmpty()) { // Evalua si el arbol carece de datos para generar un reporte acorde
            return "El Arbol B se encuentra vacio.\n"; // Retorna una cadena directa informando que la estructura no tiene elementos
        }
        toStringRecursivo(this.root, sb); // Llama al metodo de recorrido interno inyectando la raiz y el constructor de cadenas
        return sb.toString(); // Convierte el contenido consolidado en String y lo devuelve formalmente
    }

    private void toStringRecursivo(BNode<E> actual, StringBuilder sb) { // Recorre el arbol por niveles de forma pre-orden para formatear cada nodo existente
        if (actual == null) { // Condicion de salida que detiene el subproceso al topar con una referencia nula
            return; // Finaliza la ejecucion de la rama de llamadas recursivas actual
        }
        sb.append(actual.toString()).append("\n"); // Invoca al metodo toString propio del nodo agregando un salto de linea para cumplir el formato oficial
        for (BNode<E> hijo : actual.getChilds()) { // Recorre en un bucle for-each cada uno de los subarboles registrados en el nodo bajo analisis
            toStringRecursivo(hijo, sb); // Llama recursivamente a la funcion delegando la impresion para los nodos del siguiente nivel
        }
    }
}