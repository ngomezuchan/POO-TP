package vistas;

import modelo.*;
import javax.swing.*;
import java.awt.*;

public class dialogo_admin_producto extends JDialog {

    private JTable tabla;
    private modelo_tabla_productos modeloTabla;
    private JButton btnAgregar, btnEliminar, btnCerrar;

    private final Tienda tienda;

    public dialogo_admin_producto(Window owner, Tienda tienda) {
        super(owner, "Gestión de Productos", ModalityType.APPLICATION_MODAL);
        this.tienda = tienda;
        inicializar();
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    private void inicializar() {
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new modelo_tabla_productos(tienda.getProductos());
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAgregar = new JButton("Agregar");
        btnEliminar = new JButton("Eliminar");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void agregarProducto() {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtStock = new JTextField();

        Object[] fields = {
                "ID:", txtId,
                "Nombre:", txtNombre,
                "Precio:", txtPrecio,
                "Stock:", txtStock
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Nuevo producto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = txtId.getText().trim();
                String nombre = txtNombre.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());

                if (id.isEmpty() || nombre.isEmpty()) throw new IllegalArgumentException("Campos vacíos");

                tienda.agregarProducto(new Producto(id, nombre, precio, stock));
                modeloTabla.setProductos(tienda.getProductos());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
            return;
        }
        Producto p = modeloTabla.getProductoAt(fila);
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar " + p.getNombre() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            tienda.eliminarProducto(p);
            modeloTabla.setProductos(tienda.getProductos());
        }
    }
}
