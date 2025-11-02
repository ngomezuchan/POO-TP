package modelo;

import java.io.Serializable;

public class ItemCarrito implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Producto producto;
    private int cantidad;
    
    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.cantidad = cantidad;
    }
    
    public void incrementarCantidad(int cantidad) {
        this.cantidad += cantidad;
    }
    
    @Override
    public String toString() {
        return String.format("%s x %d = $%.2f", 
                           producto.getNombre(), cantidad, getSubtotal());
    }
}
