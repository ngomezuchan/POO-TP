package vistas;

import modelo.*;
import excepciones.PagoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;

public class ventana_checkout extends JFrame {

    // Componentes declarados
    private JPanel panel_principal;
    private JTable tbl_carrito;
    private JButton btn_eliminar_item;
    private JButton btn_vaciar_carrito;
    private JLabel lbl_total;
    private JButton btn_pagar_tarjeta;
    private JButton btn_pagar_transferencia;

    private final Tienda tienda;
    private final ventana_tienda ventana_padre;
    private modelo_tabla_carrito tableModel;

    public ventana_checkout(Tienda tienda, ventana_tienda ventana_padre) {
        this.tienda = tienda;
        this.ventana_padre = ventana_padre;

        // Llama al método de construcción para inicializar todos los componentes
        inicializarComponentes();

        setTitle("Finalizar Compra - Carrito");
        setContentPane(panel_principal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 500);
        setLocationRelativeTo(ventana_padre);

        cargarCarrito();

        // Listeners
        btn_eliminar_item.addActionListener(e -> eliminarItemSeleccionado());
        btn_vaciar_carrito.addActionListener(e -> vaciarCarrito());
        btn_pagar_tarjeta.addActionListener(e -> intentarPago(1));
        btn_pagar_transferencia.addActionListener(e -> intentarPago(2));

        // Al cerrar, notifica a la tienda principal para actualizar su total y stock
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                ventana_padre.actualizarTotalCarrito();
                ventana_padre.cargarProductos(); // Refresca el stock en la ventana principal
            }
        });
    }

    /**
     * Inicializa los componentes de la GUI manualmente, utilizando layouts anidados
     * para asegurar la visibilidad del botón de transferencia.
     */
    private void inicializarComponentes() {
        // Estructura principal
        panel_principal = new JPanel(new BorderLayout(10, 10));
        JPanel panel_botones = new JPanel(new GridLayout(1, 2, 10, 10)); // Contiene Opciones y Pago

        // Paneles anidados
        JPanel panel_opciones_carrito = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // ⚠️ PANEL DE PAGO CON BORDERLAYOUT PARA SEPARAR EL TOTAL DE LOS BOTONES ⚠️
        JPanel panel_pago = new JPanel(new BorderLayout());

        // Panel interno para los botones de pago, usando FlowLayout (derecha)
        JPanel panel_botones_pago = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // Componentes (Inicializaciones)
        tbl_carrito = new JTable();
        btn_eliminar_item = new JButton("Eliminar Ítem Seleccionado");
        btn_vaciar_carrito = new JButton("Vaciar Carrito");
        lbl_total = new JLabel("Total a Pagar: $0.00", SwingConstants.RIGHT);
        btn_pagar_tarjeta = new JButton("Pagar con Tarjeta");
        btn_pagar_transferencia = new JButton("Pagar con Transferencia"); // Inicialización OK

        // 1. Panel de opciones de carrito (Eliminar/Vaciar)
        panel_opciones_carrito.add(btn_eliminar_item);
        panel_opciones_carrito.add(btn_vaciar_carrito);

        // 2. Sub-Panel de botones de pago (Derecha)
        panel_botones_pago.add(btn_pagar_tarjeta);
        panel_botones_pago.add(btn_pagar_transferencia);

        // 3. Panel de Pago Final (Contiene el Total (Izquierda) y los Botones (Derecha))
        panel_pago.add(lbl_total, BorderLayout.WEST);
        panel_pago.add(panel_botones_pago, BorderLayout.EAST);


        // 4. Panel inferior (opciones y pago)
        panel_botones.add(panel_opciones_carrito);
        panel_botones.add(panel_pago);

        // 5. Panel principal
        panel_principal.add(new JScrollPane(tbl_carrito), BorderLayout.CENTER);
        panel_principal.add(panel_botones, BorderLayout.SOUTH);
        panel_principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    // --- Lógica del Carrito ---
    private void cargarCarrito() {
        Carrito carrito = tienda.getClienteActual().getCarrito();
        tableModel = new modelo_tabla_carrito(carrito.getItems());
        tbl_carrito.setModel(tableModel);
        lbl_total.setText(String.format("Total a Pagar: $%.2f", carrito.getTotal()));
    }

    private void eliminarItemSeleccionado() {
        int filaSeleccionada = tbl_carrito.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem del carrito para eliminar.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productoId = tableModel.getProductoIdAt(filaSeleccionada);

        try {
            tienda.getClienteActual().getCarrito().eliminarProducto(productoId);
            cargarCarrito();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void vaciarCarrito() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea vaciar el carrito?", "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            tienda.getClienteActual().getCarrito().vaciarCarrito();
            cargarCarrito();
            JOptionPane.showMessageDialog(this, "Carrito vaciado.");
        }
    }

    // pago
    private void intentarPago(int tipoPago) {
        Carrito carrito = tienda.getClienteActual().getCarrito();
        if (carrito.estaVacio()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío.",
                    "Error de Pago", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Pedido pedido = tienda.crearPedido(carrito);
            double monto = pedido.getMontoTotal();

            Pago pago = null;

            if (tipoPago == 1) { // Tarjeta
                dialogo_pago_tarjeta dialogo = new dialogo_pago_tarjeta(this, monto);
                dialogo.setVisible(true);
                pago = dialogo.getPago();

            } else if (tipoPago == 2) { // Transferencia
                dialogo_pago_transferencia dialogo = new dialogo_pago_transferencia(this, monto);
                dialogo.setVisible(true);
                pago = dialogo.getPago();
            }

            if (pago != null) {
                pedido.procesarPago(pago);

                tienda.getClienteActual().getCarrito().vaciarCarrito();
                JOptionPane.showMessageDialog(this,
                        "¡PEDIDO COMPLETADO EXITOSAMENTE!\nComprobante: " + pago.obtenerComprobante(),
                        "Pago Aprobado", JOptionPane.INFORMATION_MESSAGE);

                this.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "El pago fue cancelado o no se completó.",
                        "Pago Cancelado", JOptionPane.WARNING_MESSAGE);
            }

        } catch (IllegalStateException | PagoException e) {
            JOptionPane.showMessageDialog(this, "Error al procesar el pedido/pago: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}