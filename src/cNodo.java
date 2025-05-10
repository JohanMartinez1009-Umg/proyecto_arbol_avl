public class cNodo {
    private cEmpleado empleado;
    private cNodo izquierdo;
    private cNodo derecho;
    private int altura;

    public cNodo(cEmpleado empleado) {
        this.empleado = empleado;
        this.altura = 1;
    }

    // Getters y Setters
    public cEmpleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(cEmpleado empleado) {
        this.empleado = empleado;
    }
    public cNodo getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(cNodo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public cNodo getDerecho() {
        return derecho;
    }

    public void setDerecho(cNodo derecho) {
        this.derecho = derecho;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
