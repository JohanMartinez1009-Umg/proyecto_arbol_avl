public class cArbol {
    private cNodo raiz;

    public cNodo getRaiz() {
        return raiz;
    }

    private int altura(cNodo nodo) {
        return nodo == null ? 0 : nodo.getAltura();
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

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

    private cNodo buscar(cNodo raiz, int noEmpleado) {
        if (raiz == null || raiz.getEmpleado().getNoEmpleado() == noEmpleado)
            return raiz;

        if (raiz.getEmpleado().getNoEmpleado() < noEmpleado)
            return buscar(raiz.getDerecho(), noEmpleado);

        return buscar(raiz.getIzquierdo(), noEmpleado);
    }

    public void eliminar(int noEmpleado) {
        raiz = eliminar(raiz, noEmpleado);
    }

    private cNodo eliminar(cNodo raiz, int noEmpleado) {
        if (raiz == null) return raiz;

        if (noEmpleado < raiz.getEmpleado().getNoEmpleado())
            raiz.setIzquierdo(eliminar(raiz.getIzquierdo(), noEmpleado));
        else if (noEmpleado > raiz.getEmpleado().getNoEmpleado())
            raiz.setDerecho(eliminar(raiz.getDerecho(), noEmpleado));
        else {
            if (raiz.getIzquierdo() == null || raiz.getDerecho() == null) {
                cNodo temp = raiz.getIzquierdo() != null ? raiz.getIzquierdo() : raiz.getDerecho();
                if (temp == null) {
                    temp = raiz;
                    raiz = null;
                } else {
                    raiz = temp;
                }
            } else {
                cNodo temp = minValueNode(raiz.getDerecho());
                raiz.getEmpleado().setNombreCompleto(temp.getEmpleado().getNombreCompleto());
                raiz.getEmpleado().setPuesto(temp.getEmpleado().getPuesto());
                raiz.setDerecho(eliminar(raiz.getDerecho(), temp.getEmpleado().getNoEmpleado()));
            }
        }

        if (raiz == null) return raiz;

        raiz.setAltura(1 + max(altura(raiz.getIzquierdo()), altura(raiz.getDerecho())));

        int balance = obtenerBalance(raiz);

        // Rotaciones para balancear después de eliminar
        if (balance > 1 && obtenerBalance(raiz.getIzquierdo()) >= 0)
            return rotarDerecha(raiz);

        if (balance > 1 && obtenerBalance(raiz.getIzquierdo()) < 0) {
            raiz.setIzquierdo(rotarIzquierda(raiz.getIzquierdo()));
            return rotarDerecha(raiz);
        }

        if (balance < -1 && obtenerBalance(raiz.getDerecho()) <= 0)
            return rotarIzquierda(raiz);

        if (balance < -1 && obtenerBalance(raiz.getDerecho()) > 0) {
            raiz.setDerecho(rotarDerecha(raiz.getDerecho()));
            return rotarIzquierda(raiz);
        }

        return raiz;
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
