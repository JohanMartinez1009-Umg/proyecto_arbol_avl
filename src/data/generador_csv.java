package data;
import java.io.FileWriter;
import java.io.IOException;

public class generador_csv {
    public static void main(String[] args) {
        String[][] empleados = {
                {"101", "Juan Pérez", "Gerente"},
                {"205", "María López", "Desarrollador"},
                {"302", "Carlos Sánchez", "Analista"},
                {"104", "Ana Ramírez", "Diseñador"},
                {"503", "Pedro Gómez", "Contador"},
                {"610", "Laura Díaz", "Recursos Humanos"},
                {"207", "Miguel Torres", "Marketing"},
                {"309", "Sofía Castro", "Desarrollador"},
                {"401", "Ricardo Flores", "Analista"},
                {"112", "Elena Ruiz", "Asistente"},
                {"415", "Roberto Jiménez", "Soporte Técnico"},
                {"518", "Carolina Méndez", "Administrador"},
                {"223", "Daniel Vázquez", "Desarrollador"},
                {"704", "Patricia Ortega", "Diseñador"},
                {"126", "Jorge Silva", "Analista"}
        };

        try (FileWriter writer = new FileWriter("empleados.csv")) {
            for (String[] empleado : empleados) {
                writer.write(String.join(",", empleado) + "\n");
            }
            System.out.println("Archivo CSV creado exitosamente!");
        } catch (IOException e) {
            System.err.println("Error al crear el archivo: " + e.getMessage());
        }
    }
}
