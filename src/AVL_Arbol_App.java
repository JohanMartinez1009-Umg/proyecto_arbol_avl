import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AVL_Arbol_App extends JFrame {
    private cArbol arbolAVL;
    private PanelArbol panelArbol;
    private JTextField campoBusqueda;
    private JTextArea infoEmpleado;

    public AVL_Arbol_App() {
        arbolAVL = new cArbol();
        cargarDatosDesdeCSV("empleados.csv");

        setTitle("Gestión de Empleados - Árbol AVL");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelArbol = new PanelArbol();
        panelArbol.setRaiz(arbolAVL.getRaiz());
        add(panelArbol, BorderLayout.CENTER);

        infoEmpleado = new JTextArea(10, 30);
        infoEmpleado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoEmpleado);

        JPanel panelControl = new JPanel(new GridLayout(1, 4, 10, 10));
        campoBusqueda = new JTextField(10);
        JButton botonBuscar = new JButton("Buscar");
        JButton botonActualizar = new JButton("Actualizar");
        JButton botonEliminar = new JButton("Eliminar");

        botonBuscar.addActionListener(e -> buscarEmpleado());
        botonActualizar.addActionListener(e -> actualizarEmpleado());
        botonEliminar.addActionListener(e -> eliminarEmpleado());

        panelControl.add(new JLabel("No. Empleado:"));
        panelControl.add(campoBusqueda);
        panelControl.add(botonBuscar);
        panelControl.add(botonActualizar);
        panelControl.add(botonEliminar);

        add(panelControl, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void cargarDatosDesdeCSV(String archivo) {
        List<cEmpleado> empleados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 3) {
                    int noEmpleado = Integer.parseInt(datos[0].trim());
                    empleados.add(new cEmpleado(noEmpleado, datos[1].trim(), datos[2].trim()));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        for (cEmpleado emp : empleados) {
            arbolAVL.insertar(emp);
        }
    }

    private void buscarEmpleado() {
        try {
            long inicio = System.nanoTime();
            int noEmpleado = Integer.parseInt(campoBusqueda.getText());
            cEmpleado empleado = arbolAVL.buscar(noEmpleado);

            if (empleado != null) {
                infoEmpleado.setText(empleado.toString());
                panelArbol.setNodoEncontrado(arbolAVL.getRaiz()); // Esto necesita ajuste para encontrar el nodo correcto
            } else {
                infoEmpleado.setText("Empleado no encontrado");
            }
            panelArbol.repaint();

            long fin = System.nanoTime();
            double tiempo = (fin - inicio) / 1e6;
            JOptionPane.showMessageDialog(this, "Búsqueda completada en " + tiempo + " ms");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de empleado válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEmpleado() {
        // Implementar lógica de actualización
    }

    private void eliminarEmpleado() {
        // Implementar lógica de eliminación
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AVL_Arbol_App app = new AVL_Arbol_App();
            app.setVisible(true);
        });
    }
}
