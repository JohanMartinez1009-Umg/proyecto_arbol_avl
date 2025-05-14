    import java.util.ArrayList;
    import java.util.List;

    public class cArbol {
        private cNodo raiz;

        public cNodo getRaiz() {
            return raiz;
        }

        protected cNodo getNodo(int noEmpleado) {
            return buscar(raiz, noEmpleado);  // Reutiliza tu método privado existente
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


        private int altura(cNodo nodo) {
            return nodo == null ? 0 : nodo.getAltura();
        }

        private int max(int a, int b) {
            return Math.max(a, b);
        }

        private cNodo rotarDerecha(cNodo y) {
            cNodo x = y.getIzquierdo();
            cNodo T2 = x.getDerecho();

            x.setDerecho(y);
            y.setIzquierdo(T2);

            // Actualizar alturas después de la rotación
            y.setAltura(max(altura(y.getIzquierdo()), altura(y.getDerecho())) + 1);
            x.setAltura(max(altura(x.getIzquierdo()), altura(x.getDerecho())) + 1);

            return x;
        }


        private cNodo rotarIzquierda(cNodo x) {
            cNodo y = x.getDerecho();
            cNodo T2 = y.getIzquierdo();

            y.setIzquierdo(x);
            x.setDerecho(T2);

            // Actualizar alturas después de la rotación
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
                return nodo; // No se permiten duplicados

            // Actualizar la altura del nodo actual
            nodo.setAltura(1 + max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())));

            // Obtener el factor de balance de este nodo ancestro para verificar si está desequilibrado
            int balance = obtenerBalance(nodo);

            // Casos de desequilibrio
            // Izquierda Izquierda
            if (balance > 1 && empleado.getNoEmpleado() < nodo.getIzquierdo().getEmpleado().getNoEmpleado())
                return rotarDerecha(nodo);

            // Derecha Derecha
            if (balance < -1 && empleado.getNoEmpleado() > nodo.getDerecho().getEmpleado().getNoEmpleado())
                return rotarIzquierda(nodo);

            // Izquierda Derecha
            if (balance > 1 && empleado.getNoEmpleado() > nodo.getIzquierdo().getEmpleado().getNoEmpleado()) {
                nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
                return rotarDerecha(nodo);
            }

            // Derecha Izquierda
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
                if (nodo.getIzquierdo() == null || nodo.getDerecho() == null) {
                    cNodo temp = (nodo.getIzquierdo() != null) ? nodo.getIzquierdo() : nodo.getDerecho();

                    // Caso sin hijos o un solo hijo
                    if (temp == null) {
                        return null;
                    } else {
                        return temp;
                    }
                } else {
                    // Nodo con dos hijos: obtener el sucesor inorden (el menor en el subárbol derecho)
                    cNodo temp = minValueNode(nodo.getDerecho());

                    // Copiar el valor del sucesor inorden a este nodo
                    nodo.setEmpleado(temp.getEmpleado());

                    // Eliminar el sucesor inorden
                    nodo.setDerecho(eliminar(nodo.getDerecho(), temp.getEmpleado().getNoEmpleado()));
                }
            }

            // Si el árbol tenía solo un nodo, entonces se eliminó y la raíz es nula
            if (nodo == null) return null;

            // Actualizar la altura del nodo actual
            nodo.setAltura(1 + max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho())));

            // Obtener el factor de balance de este nodo ancestro para verificar si está desequilibrado
            int balance = obtenerBalance(nodo);

            // Casos de desequilibrio
            // Izquierda Izquierda
            if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) >= 0)
                return rotarDerecha(nodo);

            // Izquierda Derecha
            if (balance > 1 && obtenerBalance(nodo.getIzquierdo()) < 0) {
                nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
                return rotarDerecha(nodo);
            }

            // Derecha Derecha
            if (balance < -1 && obtenerBalance(nodo.getDerecho()) <= 0)
                return rotarIzquierda(nodo);

            // Derecha Izquierda
            if (balance < -1 && obtenerBalance(nodo.getDerecho()) > 0) {
                nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
                return rotarIzquierda(nodo);
            }

            return nodo;
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


        // Nuevo método para obtener todos los empleados en orden (inorden)
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

        // Nuevo método para vaciar el árbol
        public void vaciarArbol() {
            raiz = null;
        }
    }