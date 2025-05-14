import java.util.ArrayList;
import java.util.List;

public class cArbol {
    private cNodo raiz;

    public cNodo getRaiz() {
        return raiz;
    }

    protected cNodo getNodo(int noEmpleado) {
        return buscarNodo(raiz, noEmpleado);
    }

    public int obtenerSaltos(int noEmpleado) {
        return obtenerSaltosRecursivo(raiz, noEmpleado, 0);
    }

    private int obtenerSaltosRecursivo(cNodo nodo, int noEmpleado, int nivel) {
        if (nodo == null) return -1;
        if (nodo.getEmpleado().getNoEmpleado() == noEmpleado) return nivel;

        if (noEmpleado < nodo.getEmpleado().getNoEmpleado()) {
            return obtenerSaltosRecursivo(nodo.getIzquierdo(), noEmpleado, nivel + 1);
        } else {
            return obtenerSaltosRecursivo(nodo.getDerecho(), noEmpleado, nivel + 1);
        }
    }

    public void insertar(cEmpleado empleado) {
        raiz = insertarNodo(raiz, empleado);
    }

    private cNodo insertarNodo(cNodo nodo, cEmpleado empleado) {
        if (nodo == null) {
            return new cNodo(empleado);
        }

        if (empleado.getNoEmpleado() < nodo.getEmpleado().getNoEmpleado()) {
            nodo.setIzquierdo(insertarNodo(nodo.getIzquierdo(), empleado));
        } else if (empleado.getNoEmpleado() > nodo.getEmpleado().getNoEmpleado()) {
            nodo.setDerecho(insertarNodo(nodo.getDerecho(), empleado));
        } else {
            // Si el número de empleado ya existe, podrías optar por no insertar
            // o manejar la duplicidad de alguna otra manera (por ejemplo, lanzar una excepción).
            // Por ahora, simplemente no hacemos nada.
        }
        return nodo;
    }

    public cEmpleado buscar(int noEmpleado) {
        cNodo resultado = buscarNodo(raiz, noEmpleado);
        return resultado != null ? resultado.getEmpleado() : null;
    }

    private cNodo buscarNodo(cNodo nodo, int noEmpleado) {
        if (nodo == null || nodo.getEmpleado().getNoEmpleado() == noEmpleado) {
            return nodo;
        }

        if (noEmpleado < nodo.getEmpleado().getNoEmpleado()) {
            return buscarNodo(nodo.getIzquierdo(), noEmpleado);
        } else {
            return buscarNodo(nodo.getDerecho(), noEmpleado);
        }
    }

    public void eliminar(int noEmpleado) {
        raiz = eliminarNodo(raiz, noEmpleado);
    }

    private cNodo eliminarNodo(cNodo nodo, int noEmpleado) {
        if (nodo == null) {
            return null;
        }

        if (noEmpleado < nodo.getEmpleado().getNoEmpleado()) {
            nodo.setIzquierdo(eliminarNodo(nodo.getIzquierdo(), noEmpleado));
        } else if (noEmpleado > nodo.getEmpleado().getNoEmpleado()) {
            nodo.setDerecho(eliminarNodo(nodo.getDerecho(), noEmpleado));
        } else {
            // Nodo encontrado
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            } else if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }

            // Nodo con dos hijos: encontrar el sucesor inorden (el menor en el subárbol derecho)
            cNodo sucesor = encontrarMinimo(nodo.getDerecho());
            nodo.setEmpleado(sucesor.getEmpleado());
            nodo.setDerecho(eliminarNodo(nodo.getDerecho(), sucesor.getEmpleado().getNoEmpleado()));
        }
        return nodo;
    }

    private cNodo encontrarMinimo(cNodo nodo) {
        while (nodo.getIzquierdo() != null) {
            nodo = nodo.getIzquierdo();
        }
        return nodo;
    }

    public void inOrden() {
        inOrdenRecursivo(raiz);
    }

    private void inOrdenRecursivo(cNodo nodo) {
        if (nodo != null) {
            inOrdenRecursivo(nodo.getIzquierdo());
            System.out.println(nodo.getEmpleado());
            inOrdenRecursivo(nodo.getDerecho());
        }
    }

    public List<cEmpleado> obtenerTodosLosEmpleadosEnOrden() {
        List<cEmpleado> listaEmpleados = new ArrayList<>();
        obtenerTodosLosEmpleadosEnOrdenRecursivo(raiz, listaEmpleados);
        return listaEmpleados;
    }

    private void obtenerTodosLosEmpleadosEnOrdenRecursivo(cNodo nodo, List<cEmpleado> lista) {
        if (nodo != null) {
            obtenerTodosLosEmpleadosEnOrdenRecursivo(nodo.getIzquierdo(), lista);
            lista.add(nodo.getEmpleado());
            obtenerTodosLosEmpleadosEnOrdenRecursivo(nodo.getDerecho(), lista);
        }
    }

    public void vaciarArbol() {
        raiz = null;
    }

    // Método de eliminación utilizando la sustitución con el predecesor inorder
    public void eliminarConSustitucionPredecesor(int noEmpleado) {
        raiz = eliminarNodoConPredecesor(raiz, noEmpleado);
    }

    private cNodo eliminarNodoConPredecesor(cNodo nodoActual, int noEmpleado) {
        if (nodoActual == null) {
            return null;
        }

        if (noEmpleado < nodoActual.getEmpleado().getNoEmpleado()) {
            nodoActual.setIzquierdo(eliminarNodoConPredecesor(nodoActual.getIzquierdo(), noEmpleado));
            return nodoActual;
        } else if (noEmpleado > nodoActual.getEmpleado().getNoEmpleado()) {
            nodoActual.setDerecho(eliminarNodoConPredecesor(nodoActual.getDerecho(), noEmpleado));
            return nodoActual;
        } else {
            // Nodo encontrado
            if (nodoActual.getIzquierdo() == null) {
                return nodoActual.getDerecho();
            } else if (nodoActual.getDerecho() == null) {
                return nodoActual.getIzquierdo();
            } else {
                // Nodo con dos hijos - Sustituir con el predecesor inorder
                cNodo predecesor = encontrarMaximo(nodoActual.getIzquierdo());
                nodoActual.setEmpleado(predecesor.getEmpleado());
                nodoActual.setIzquierdo(eliminarNodoConPredecesor(nodoActual.getIzquierdo(), predecesor.getEmpleado().getNoEmpleado()));
                return nodoActual;
            }
        }
    }

    private cNodo encontrarMaximo(cNodo nodo) {
        while (nodo.getDerecho() != null) {
            nodo = nodo.getDerecho();
        }
        return nodo;
    }
}