package modelo;

import java.io.Serializable;

public class Producto implements Serializable {
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

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public boolean hayStock(int cantidad) {
        return stock >= cantidad;
    }

    public void reducirStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
        if (cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente para " + nombre);
        }
        stock -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        if (cantidad > 0) stock += cantidad;
    }

    @Override
    public String toString() {
        return nombre + " ($" + precio + ")";
    }
}
