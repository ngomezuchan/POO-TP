package modelo;

import excepciones.PagoException;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contadorPedidos = 0;

    // Se declaran como 'final' ya que solo se asignan en el constructor
    private final int idPedido;
    private final Cliente cliente;
    private final Carrito carrito;
    private final LocalDateTime fechaPedido;

    private double impuesto;
    private Pago pago;
    private EstadoPedido estado;
    private double montoTotal;

    public enum EstadoPedido {
        PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    }

    public Pedido(Cliente cliente, Carrito carrito) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser null");
        }
        if (carrito == null || carrito.estaVacio()) {
            throw new IllegalArgumentException("El carrito no puede estar vacío");
        }

        this.idPedido = ++contadorPedidos;
        this.cliente = cliente;
        this.carrito = carrito;
        this.fechaPedido = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.impuesto = 0;
        asignarImpuesto();
    }

    /**
     Impuesto 21%
     */
    public void asignarImpuesto() {
        double tasaImpuesto = 0.21;
        this.impuesto = carrito.getTotal() * tasaImpuesto;
        this.montoTotal = carrito.getTotal() + this.impuesto;
    }

    /**
     * Lógica crítica: Reduce el stock de los productos comprados.
     */
    private void reducirStock() {
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            int cantidad = item.getCantidad();
            producto.reducirStock(cantidad);
        }
    }


    public boolean procesarPago(Pago pago) throws PagoException {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser null");
        }

        if (this.estado != EstadoPedido.PENDIENTE) {
            throw new PagoException("El pedido ya fue procesado");
        }

        if (Math.abs(pago.getMonto() - this.montoTotal) > 0.01) {
            throw new PagoException(String.format(
                    "El monto del pago ($%.2f) no coincide con el total del pedido ($%.2f)",
                    pago.getMonto(), this.montoTotal
            ));
        }

        boolean pagoExitoso = pago.procesarPago(montoTotal);

        if (pagoExitoso) {
            this.pago = pago;
            this.estado = EstadoPedido.PAGADO;
            reducirStock(); // <--- CRÍTICO: Se reduce el stock aquí
            System.out.println("Pedido #" + idPedido + " pagado exitosamente");
            return true;
        }

        return false;
    }


    public void mostrarResumen() {
        System.out.println("\nRESUMEN DE PEDIDO #" + idPedido);
        System.out.println("Cliente: " + cliente.getNombre() + " (ID: " + cliente.getId() + ")");
        System.out.println("Fecha: " + fechaPedido);
        System.out.println("Estado: " + estado);

        carrito.mostrarCarrito();

        System.out.printf("\nSubtotal: $%.2f\n", carrito.getTotal());
        System.out.printf("Impuesto (IVA): $%.2f\n", impuesto);
        System.out.printf("TOTAL A PAGAR: $%.2f\n", montoTotal);

        if (pago != null && pago.isPagoProcesado()) {
            System.out.println("\n--- Información del Pago ---");
            pago.mostrar();
        }
        System.out.println("-------------------------------------------\n");
    }


    public boolean cancelarPedido() {
        // En una aplicación real, si el estado es PAGADO, se debería gestionar un reembolso.
        if (estado == EstadoPedido.PENDIENTE) {
            estado = EstadoPedido.CANCELADO;
            System.out.println("Pedido #" + idPedido + " cancelado exitosamente");
            return true;
        } else if (estado == EstadoPedido.PAGADO) {
            // Se permite cancelar, pero con una advertencia
            System.out.println("Advertencia: El pedido #" + idPedido + " está PAGADO. Se requiere gestión de reembolso.");
            estado = EstadoPedido.CANCELADO;
            return true;
        }

        System.out.println("No se puede cancelar el pedido en estado: " + estado);
        return false;
    }

    public double getMontoTotal() {
        return montoTotal;
    }
}