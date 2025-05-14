import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

public class AVL_Arbol_App extends JFrame {
    private cArbol arbolAVL;
    private PanelArbol panelArbol;
    private JTextField campoBusqueda;
    private JTextArea infoEmpleado;

    public AVL_Arbol_App() {
        arbolAVL = new cArbol();
        // cargarDatosDesdeCSV("empleados.csv"); // Comenta o elimina esta línea

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

        JPanel panelControl = new JPanel(new GridLayout(1, 6, 10, 10)); // Ajustamos las columnas
        campoBusqueda = new JTextField(10);
        JButton botonBuscar = new JButton("Buscar");
        JButton botonActualizar = new JButton("Actualizar");
        JButton botonEliminar = new JButton("Eliminar");
        JButton botonInsertar = new JButton("Insertar"); // Nuevo botón
        JButton botonImportarCSV = new JButton("Importar CSV"); // Nuevo botón

        botonActualizar.addActionListener(e -> {
            try {
                int noEmpleado = Integer.parseInt(campoBusqueda.getText());
                cNodo nodoAActualizar = arbolAVL.getNodo(noEmpleado);
                if (nodoAActualizar != null) {
                    panelArbol.setNodoEncontrado(nodoAActualizar);
                    panelArbol.repaint();
                    actualizarEmpleado(); // Llama a actualizarEmpleado DESPUÉS de la selección visual
                } else {
                    JOptionPane.showMessageDialog(this, "Empleado no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    panelArbol.setNodoEncontrado(null);
                    panelArbol.repaint();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número de empleado válido", "Error", JOptionPane.ERROR_MESSAGE);
                panelArbol.setNodoEncontrado(null);
                panelArbol.repaint();
            }
        });

        // El ActionListener del botón Eliminar se mantiene similar para la selección visual
        botonEliminar.addActionListener(e -> {
            try {
                int noEmpleado = Integer.parseInt(campoBusqueda.getText());
                cNodo nodoAEliminar = arbolAVL.getNodo(noEmpleado);
                if (nodoAEliminar != null) {
                    panelArbol.setNodoEncontrado(nodoAEliminar);
                    panelArbol.repaint();
                    eliminarEmpleado(); // Llama a eliminarEmpleado DESPUÉS de la selección visual
                } else {
                    JOptionPane.showMessageDialog(this, "Empleado no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    panelArbol.setNodoEncontrado(null);
                    panelArbol.repaint();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número de empleado válido", "Error", JOptionPane.ERROR_MESSAGE);
                panelArbol.setNodoEncontrado(null);
                panelArbol.repaint();
            }
        });

        botonBuscar.addActionListener(e -> buscarEmpleado());
        botonInsertar.addActionListener(e -> insertarEmpleado()); // ActionListener para el nuevo botón
        botonImportarCSV.addActionListener(e -> importarDesdeCSV()); // ActionListener para el nuevo botón

        panelControl.add(new JLabel("No. Empleado:"));
        panelControl.add(campoBusqueda);
        panelControl.add(botonBuscar);
        panelControl.add(botonActualizar);
        panelControl.add(botonEliminar);
        panelControl.add(botonInsertar);
        panelControl.add(botonImportarCSV); // Agregamos el botón Importar CSV

        add(panelControl, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void importarDesdeCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File archivoSeleccionado = fileChooser.getSelectedFile();
            cargarDatosDesdeCSV(archivoSeleccionado.getAbsolutePath());
        }
    }

    private void insertarEmpleado() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField campoNoEmpleado = new JTextField();
        JTextField campoNombre = new JTextField();
        JTextField campoPuesto = new JTextField();

        panel.add(new JLabel("Número Empleado:"));
        panel.add(campoNoEmpleado);
        panel.add(new JLabel("Nombre Completo:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Puesto:"));
        panel.add(campoPuesto);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Insertar Nuevo Empleado",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String noEmpleadoStr = campoNoEmpleado.getText().trim();
                if (noEmpleadoStr.length() >= 3 && noEmpleadoStr.length() <= 4 && noEmpleadoStr.matches("\\d+")) {
                    int noEmpleado = Integer.parseInt(noEmpleadoStr);
                    String nombre = campoNombre.getText().trim();
                    String puesto = campoPuesto.getText().trim();

                    if (nombre.isEmpty() || puesto.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Por favor, complete todos los campos.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    cEmpleado nuevoEmpleado = new cEmpleado(noEmpleado, nombre, puesto);
                    long inicio = System.nanoTime();
                    arbolAVL.insertar(nuevoEmpleado);
                    long fin = System.nanoTime();
                    double tiempo = (fin - inicio) / 1e6;

                    // Asegúrate de que el PanelArbol esté utilizando la raíz actualizada del árbol
                    panelArbol.setRaiz(arbolAVL.getRaiz());
                    panelArbol.setNodoEncontrado(null); // Limpiar cualquier nodo resaltado
                    panelArbol.repaint(); // Solicitar que el panel se vuelva a dibujar
                    guardarDatosEnCSV("empleados.csv"); // Guardar en CSV después de insertar

                    JOptionPane.showMessageDialog(this,
                            "Empleado insertado en " + tiempo + " ms",
                            "Inserción Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(this,
                            "El número de empleado debe tener 3 o 4 dígitos.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese un número de empleado válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al insertar el empleado: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarDatosEnCSV(String archivo) {
        try (FileWriter fw = new FileWriter(archivo)) {
            List<cEmpleado> empleados = arbolAVL.obtenerTodosLosEmpleadosEnOrden(); // Asumiendo este método en cArbol
            for (cEmpleado empleado : empleados) {
                fw.write(empleado.getNoEmpleado() + "," + empleado.getNombreCompleto() + "," + empleado.getPuesto() + "\n");
            }
            JOptionPane.showMessageDialog(this, "Datos guardados en " + archivo, "Guardado Exitoso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los datos en el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosDesdeCSV(String archivo) {
        List<cEmpleado> empleados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 3) {
                    try {
                        String noEmpleadoStr = datos[0].trim();
                        if (noEmpleadoStr.length() >= 3 && noEmpleadoStr.length() <= 4 && noEmpleadoStr.matches("\\d+")) {
                            int noEmpleado = Integer.parseInt(noEmpleadoStr);
                            empleados.add(new cEmpleado(noEmpleado, datos[1].trim(), datos[2].trim()));
                        } else {
                            JOptionPane.showMessageDialog(this, "Número de empleado con formato incorrecto en la línea: " + linea + " (debe tener 3 o 4 dígitos)", "Error de formato", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Error al leer el número de empleado en la línea: " + linea, "Error de formato", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Formato incorrecto en la línea: " + linea + " (debe ser noEmpleado,nombre,puesto)", "Error de formato", JOptionPane.ERROR_MESSAGE);
                }
            }
            arbolAVL.vaciarArbol(); // Limpiar el árbol existente
            for (cEmpleado emp : empleados) {
                arbolAVL.insertar(emp); // <--- El balanceo ocurre aquí durante la inserción
            }
            panelArbol.setRaiz(arbolAVL.getRaiz());
            panelArbol.setNodoEncontrado(null); // Limpiar cualquier nodo resaltado
            panelArbol.repaint();
            JOptionPane.showMessageDialog(this, "Datos importados exitosamente desde " + archivo, "Importación Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo CSV: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEmpleado() {
        try {
            long inicio = System.nanoTime();
            int noEmpleado = Integer.parseInt(campoBusqueda.getText());
            cEmpleado empleado = arbolAVL.buscar(noEmpleado);

            if (empleado != null) {
                cNodo nodo = arbolAVL.getNodo(noEmpleado);

                String info = empleado.toString();
                int saltos = arbolAVL.obtenerSaltos(noEmpleado);
                info += "\nSaltos desde la raíz: " + saltos + "\n";

                infoEmpleado.setText(info);
                panelArbol.setNodoEncontrado(nodo);
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
        try {
            int noEmpleado = Integer.parseInt(campoBusqueda.getText());
            long inicio = System.nanoTime();

            cEmpleado empleado = arbolAVL.buscar(noEmpleado);

            if (empleado != null) {
                // Crear diálogo de actualización
                JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JTextField campoNombre = new JTextField(empleado.getNombreCompleto());
                JTextField campoPuesto = new JTextField(empleado.getPuesto());
                int saltos = arbolAVL.obtenerSaltos(noEmpleado);

                panel.add(new JLabel("Número Empleado:"));
                panel.add(new JLabel(String.valueOf(noEmpleado)));
                panel.add(new JLabel("Nombre Completo:"));
                panel.add(campoNombre);
                panel.add(new JLabel("Puesto:"));
                panel.add(campoPuesto);
                panel.add(new JLabel("Saltos desde la raíz:"));
                panel.add(new JLabel(String.valueOf(saltos)));

                int resultado = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Actualizar Empleado",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (resultado == JOptionPane.OK_OPTION) {
                    // Actualizar datos
                    empleado.setNombreCompleto(campoNombre.getText());
                    empleado.setPuesto(campoPuesto.getText());

                    // Actualizar visualización
                    infoEmpleado.setText(empleado.toString());
                    String info = empleado.toString();
                    int saltosActualizado = arbolAVL.obtenerSaltos(noEmpleado);
                    info += "\nSaltos desde la raíz: " + saltosActualizado + "\n";
                    infoEmpleado.setText(info);
                    panelArbol.repaint();
                    guardarDatosEnCSV("empleados.csv"); // Guardar en CSV después de actualizar

                    long fin = System.nanoTime();
                    double tiempo = (fin - inicio) / 1e6;
                    JOptionPane.showMessageDialog(this,
                            "Empleado actualizado en " + tiempo + " ms",
                            "Actualización Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Empleado no encontrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número de empleado válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleado() {
        try {
            int noEmpleado = Integer.parseInt(campoBusqueda.getText());
            long inicio = System.nanoTime();

            cEmpleado empleado = arbolAVL.buscar(noEmpleado);

            if (empleado != null) {
                // Confirmar eliminación
                int confirmacion = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar al empleado?\n" + empleado.toString(),
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Obtener el nodo antes de eliminarlo para resaltarlo
                    cNodo nodoEliminado = arbolAVL.getNodo(noEmpleado);

                    // Eliminar empleado
                    arbolAVL.eliminar(noEmpleado);

                    // Asegúrate de que el PanelArbol esté utilizando la raíz actualizada del árbol
                    panelArbol.setRaiz(arbolAVL.getRaiz());
                    panelArbol.setNodoEncontrado(nodoEliminado); // Establecer el nodo eliminado
                    panelArbol.repaint(); // Solicitar que el panel se vuelva a dibujar
                    guardarDatosEnCSV("empleados.csv"); // Guardar en CSV después de eliminar

                    long fin = System.nanoTime();
                    double tiempo = (fin - inicio) / 1e6;
                    JOptionPane.showMessageDialog(this,
                            "Empleado eliminado en " + tiempo + " ms",
                            "Eliminación Exitosa",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    panelArbol.setNodoEncontrado(null); // Limpiar selección si no se eliminó
                    panelArbol.repaint();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Empleado no encontrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número de empleado válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AVL_Arbol_App app = new AVL_Arbol_App();
            app.setVisible(true);
        });
    }
}