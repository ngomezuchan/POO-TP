package modelo;

import excepciones.PagoException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Clase Pedido
 * GRASP: Controller - Coordina la creación del pedido
 * GRASP: Information Expert - Conoce y calcula el impuesto
 */
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static int contadorPedidos = 0;
    private int idPedido;
    private Cliente cliente;
    private Carrito carrito;
    private double impuesto;
    private Pago pago;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;
    private double montoTotal;
    
    // Enum para el estado del pedido
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
        asignarImpuesto(); // Calcula automáticamente el impuesto
    }
    
    /**
     Impuesto21%
     */
    public void asignarImpuesto() {
        double tasaImpuesto = 0.21;
        this.impuesto = carrito.getTotal() * tasaImpuesto;
        this.montoTotal = carrito.getTotal() + this.impuesto;
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
            System.out.println("Pedido #" + idPedido + " pagado exitosamente");
            return true;
        }
        
        return false;
    }
    

    public void mostrarResumen() {
        System.out.println("\n========== RESUMEN DEL PEDIDO #" + idPedido + " ==========");
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
        System.out.println("================================================\n");
    }
    

    public boolean cancelarPedido() {
        if (estado == EstadoPedido.PENDIENTE || estado == EstadoPedido.PAGADO) {
            estado = EstadoPedido.CANCELADO;
            System.out.println("Pedido #" + idPedido + " cancelado exitosamente");
            return true;
        }
        System.out.println("No se puede cancelar el pedido en estado: " + estado);
        return false;
    }
    
    // getters setters
    public int getIdPedido() {
        return idPedido;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public double getImpuesto() {
        return impuesto;
    }
    
    public Pago getPago() {
        return pago;
    }
    
    public EstadoPedido getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
    
    public double getMontoTotal() {
        return montoTotal;
    }
    
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }
    
    public Carrito getCarrito() {
        return carrito;
    }
}
