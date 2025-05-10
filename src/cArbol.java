public class cArbol {
    private cNodo raiz;

    public cNodo getRaiz() {
        return raiz;
    }

    private int altura(cNodo nodo) {
        return nodo == null ? 0 : nodo.getAltura();
    }

    // Aqui empezo el cambio
    private int max(int a, int b) {
        return Math.max(a, b);
    }
    // Aqui termino el cambio

    private cNodo rotarDerecha(cNodo y) {
        cNodo x = y.getIzquierdo();
        cNodo T2 = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(T2);

        y.setAltura(max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);
        x.setAltura(max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);

        return x;
    }

    private cNodo rotarIzquierda(cNodo x) {
        cNodo y = x.getDerecho();
        cNodo T2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(T2);

        x.setAltura(max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);
        y.setAltura(max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);

        return y;
    }

    private int obtenerBalance(cNodo nodo) {
        return nodo == null ? 0 : altura(nodo.getIzquierdo()) - altura(nodo.getDerecho());
    }

    public void insertar(cEmpleado empleado) {
        raiz = insertar(raiz, empleado);
    }

    private cNodo insertar(cNodo nodo, cEmpleado empleado) {
        if (nodo == null) return new cNodo(empleado);

        if (empleado.getNoEmpleado() < nodo.getEmpleado().getNoEmpleado())
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), empleado));
        else if (empleado.getNoEmpleado() > nodo.getEmpleado().getNoEmpleado())
            nodo.setDerecho(insertar(nodo.getDerecho(), empleado));
        else
            return nodo;

        nodo.setAltura(1 + max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())));

        int balance = obtenerBalance(nodo);

        // Rotaciones para balancear el árbol
        if (balance > 1 && empleado.getNoEmpleado() < nodo.getIzquierdo().getEmpleado().getNoEmpleado())
            return rotarDerecha(nodo);

        if (balance < -1 && empleado.getNoEmpleado() > nodo.getDerecho().getEmpleado().getNoEmpleado())
            return rotarIzquierda(nodo);

        if (balance > 1 && empleado.getNoEmpleado() > nodo.getIzquierdo().getEmpleado().getNoEmpleado()) {
            nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
            return rotarDerecha(nodo);
        }

        if (balance < -1 && empleado.getNoEmpleado() < nodo.getDerecho().getEmpleado().getNoEmpleado()) {
            nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    public cEmpleado buscar(int noEmpleado) {
        cNodo resultado = buscar(raiz, noEmpleado);
        return resultado != null ? resultado.getEmpleado() : null;
    }

    private cNodo buscar(cNodo nodo, int noEmpleado) {
        if (nodo == null || nodo.getEmpleado().getNoEmpleado() == noEmpleado)
            return nodo;

        if (noEmpleado < nodo.getEmpleado().getNoEmpleado())
            return buscar(nodo.getIzquierdo(), noEmpleado);
        else
            return buscar(nodo.getDerecho(), noEmpleado);
    }

    public void eliminar(int noEmpleado) {
        raiz = eliminar(raiz, noEmpleado);
    }

    private cNodo eliminar(cNodo nodo, int noEmpleado) {
        if (nodo == null) return nodo;

        if (noEmpleado < nodo.getEmpleado().getNoEmpleado())
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), noEmpleado));
        else if (noEmpleado > nodo.getEmpleado().getNoEmpleado())
            nodo.setDerecho(eliminar(nodo.getDerecho(), noEmpleado));
        else {
            // Aqui empezo el cambio
            if (nodo.getIzquierdo() == null || nodo.getDerecho() == null) {
                nodo = (nodo.getIzquierdo() != null) ? nodo.getIzquierdo() : nodo.getDerecho();
            } else {
                cNodo temp = minValueNode(nodo.getDerecho());
                nodo.setEmpleado(temp.getEmpleado()); // Copiar todo el empleado
                nodo.setDerecho(eliminar(nodo.getDerecho(), temp.getEmpleado().getNoEmpleado()));
            }
            // Aqui termino el cambio
        }

        if (nodo == null) return null; // Nodo eliminado, no hay necesidad de balancear

        nodo.setAltura(1 + max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())));

        int balance = obtenerBalance(nodo);

        // Rotaciones para balancear después de eliminar
        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) >= 0)
            return rotarDerecha(nodo);

        if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) < 0) {
            nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
            return rotarDerecha(nodo);
        }

        if (balance < -1 && obtenerBalance(nodo.getDerecho()) <= 0)
            return rotarIzquierda(nodo);

        if (balance < -1 && obtenerBalance(nodo.getDerecho()) > 0) {
            nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
            return rotarIzquierda(nodo);
        }

        return nodo; // Importante: devolver el nodo actual (posiblemente rotado)
    }

    private cNodo minValueNode(cNodo nodo) {
        cNodo current = nodo;
        while (current.getIzquierdo() != null)
            current = current.getIzquierdo();
        return current;
    }

    public void inOrden() {
        inOrden(raiz);
    }

    private void inOrden(cNodo nodo) {
        if (nodo != null) {
            inOrden(nodo.getIzquierdo());
            System.out.println(nodo.getEmpleado());
            inOrden(nodo.getDerecho());
        }
    }
}