package modelo;

import java.io.Serializable;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String nombre;
    private double precio;
    private int stock;
    
    public Producto(String id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }
    
    public boolean hayStock(int cantidad) {
        return this.stock >= cantidad;
    }
    
    public void reducirStock(int cantidad) {
        if (hayStock(cantidad)) {
            this.stock -= cantidad;
        } else {
            throw new IllegalStateException("Stock insuficiente");
        }
    }
    
    @Override
    public String toString() {
        return String.format("Producto [ID: %s, Nombre: %s, Precio: $%.2f, Stock: %d]",id, nombre, precio, stock);
    }
}
