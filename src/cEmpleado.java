public class cEmpleado {
    private int noEmpleado;
    private String nombreCompleto;
    private String puesto;

    public cEmpleado(int noEmpleado, String nombreCompleto, String puesto) {
        this.noEmpleado = noEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.puesto = puesto;
    }

    // Getters y Setters
    public int getNoEmpleado() {
        return noEmpleado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    @Override
    public String toString() {
        return "No. Empleado: " + noEmpleado +
                "\nNombre: " + nombreCompleto +
                "\nPuesto: " + puesto;
    }

    // Método equals para comparar objetos cEmpleado
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        cEmpleado otro = (cEmpleado) obj;
        return noEmpleado == otro.noEmpleado;
    }
}