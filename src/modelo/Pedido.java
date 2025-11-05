package modelo;

import excepciones.PagoException;
import java.util.List;

public class Pedido {
    private List<ItemCarrito> items;
    private boolean pagado;
    private String numeroComprobante;

    public Pedido(List<ItemCarrito> items) {
        this.items = items;
        this.pagado = false;
        this.numeroComprobante = null;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public boolean isPagado() {
        return pagado;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public double getMontoTotal() {
        double total = 0;
        if (items != null) {
            for (ItemCarrito item : items) {
                total += item.getSubtotal();
            }
        }
        return total;
    }

    public void procesarPago(Pago pago) throws PagoException {
        if (pago == null)
            throw new PagoException("Pago no válido.");

        pago.validarDatos();
        pago.procesar();

        numeroComprobante = pago.obtenerComprobante();
        if (numeroComprobante == null || numeroComprobante.trim().isEmpty()) {
            numeroComprobante = "COMP-" + System.currentTimeMillis();
        }

        pagado = true;
    }
}
