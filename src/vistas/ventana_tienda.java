package vistas;

import modelo.Tienda;
import modelo.Producto;
import modelo.Carrito;
import principal.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class ventana_tienda extends JFrame {

    private JPanel panel_principal;
    private JTable tbl_productos;
    private JButton btn_agregar_carrito;
    private JLabel lbl_total_carrito;
    private JButton btn_ver_carrito;
    private JButton btn_cerrar_sesion;
    private JLabel lbl_bienvenido;

    private final Tienda tienda;
    private modelo_tabla_productos tableModel;

    public ventana_tienda(Tienda tienda) {
        this.tienda = tienda;

        inicializarComponentes();

        setTitle("Catálogo de Productos - " + tienda.getClienteActual().getNombre());
        setContentPane(panel_principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        lbl_bienvenido.setText("Bienvenido, " + tienda.getClienteActual().getNombre());

        cargarProductos();
        actualizarTotalCarrito();

        //listen
        btn_agregar_carrito.addActionListener(e -> agregarProductoSeleccionado());
        btn_ver_carrito.addActionListener(e -> abrirVentanaCarrito());
        btn_cerrar_sesion.addActionListener(e -> cerrarSesion());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (tienda.hayClienteLogueado()) {
                    tienda.cerrarSesion();
                }
                if (ventana_tienda.getFrames().length == 0) {
                    new Main().setVisible(true);
                }
            }
        });
    }

    private void inicializarComponentes() {
        panel_principal = new JPanel(new BorderLayout(10, 10));
        JPanel panel_top = new JPanel(new BorderLayout());
        JPanel panel_bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // componentes
        lbl_bienvenido = new JLabel("Bienvenido", SwingConstants.LEFT);
        lbl_total_carrito = new JLabel("Total Carrito: $0.00", SwingConstants.RIGHT);
        tbl_productos = new JTable();
        btn_agregar_carrito = new JButton("Agregar al Carrito");
        btn_ver_carrito = new JButton("Ver Carrito / Pagar");
        btn_cerrar_sesion = new JButton("Cerrar Sesión");

        panel_top.add(lbl_bienvenido, BorderLayout.WEST);
        panel_top.add(btn_cerrar_sesion, BorderLayout.EAST);

        panel_bottom.add(lbl_total_carrito);
        panel_bottom.add(btn_agregar_carrito);
        panel_bottom.add(btn_ver_carrito);
        panel_principal.add(panel_top, BorderLayout.NORTH);
        panel_principal.add(new JScrollPane(tbl_productos), BorderLayout.CENTER);
        panel_principal.add(panel_bottom, BorderLayout.SOUTH);
        panel_principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void cargarProductos() {
        List<Producto> productos = tienda.getProductos();
        if (tableModel == null) {
            tableModel = new modelo_tabla_productos(productos);
            tbl_productos.setModel(tableModel);
        } else {
            tableModel.setProductos(productos);
        }
    }

    private void agregarProductoSeleccionado() {
        int filaSeleccionada = tbl_productos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para agregar.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto producto = tableModel.getProductoAt(filaSeleccionada);

        String cantidadStr = JOptionPane.showInputDialog(this,
                "Ingrese la cantidad a agregar (Max: " + producto.getStock() + "):",
                "Cantidad", JOptionPane.QUESTION_MESSAGE);

        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr.trim());
            tienda.getClienteActual().getCarrito().agregarProducto(producto, cantidad);

            JOptionPane.showMessageDialog(this, producto.getNombre() + " agregado al carrito.");
            cargarProductos();
            actualizarTotalCarrito();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido para la cantidad.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarTotalCarrito() {
        Carrito carrito = tienda.getClienteActual().getCarrito();
        lbl_total_carrito.setText(String.format("Total Carrito: $%.2f (%d items)",
                carrito.getTotal(), carrito.getCantidadItems()));
    }

    private void abrirVentanaCarrito() {
        ventana_checkout checkout = new ventana_checkout(tienda, this);
        checkout.setVisible(true);
    }

    private void cerrarSesion() {
        tienda.cerrarSesion();
        this.dispose();
    }
}