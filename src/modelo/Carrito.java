package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.io.Serial;

public class Carrito implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private double total;
    private final ArrayList<ItemCarrito> carrito;

    public Carrito() {
        this.carrito = new ArrayList<>();
        this.total = 0.0;
    }

    /**
     * agrega un producto al carrito
     */
    public void agregarProducto(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }

        for (ItemCarrito item : carrito) {
            if (item.getProducto().getId().equals(producto.getId())) {

                if (!item.getProducto().hayStock(item.getCantidad() + cantidad)) {
                    throw new IllegalStateException("Stock insuficiente. No se pueden agregar " + cantidad + " unidades adicionales.");
                }

                item.incrementarCantidad(cantidad);
                actualizarTotal();
                return;
            }
        }

        if (!producto.hayStock(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        carrito.add(new ItemCarrito(producto, cantidad));
        actualizarTotal();
    }

    /**
     * elimina un producto del carrito
     */
    public void eliminarProducto(String productoId) {
        if (productoId == null || productoId.isEmpty()) {
            throw new IllegalArgumentException("El ID del producto no puede ser vacío");
        }

        boolean eliminado = carrito.removeIf(item ->
                item.getProducto().getId().equals(productoId)
        );

        if (!eliminado) {
            throw new IllegalArgumentException("producto no encontrado en el carrito");
        }

        actualizarTotal();
    }

    /**
     * muestra el contenido del carrito
     */
    public void mostrarCarrito() {
        if (carrito.isEmpty()) {
            System.out.println("El carrito esta vacio");
            return;
        }

        System.out.println("\n--- CONTENIDO DEL CARRITO --");
        for (int i = 0; i < carrito.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, carrito.get(i));
        }
        System.out.println("------------------------------------");
        mostrarTotal();
    }

    /**
     * muestra el total del carrito
     */
    public void mostrarTotal() {
        System.out.printf("TOTAL: $%.2f\n", total);
    }

    /**
     * actualiza total del carrito
     */
    private void actualizarTotal() {
        this.total = 0;
        for (ItemCarrito item : carrito) {
            this.total += item.getSubtotal();
        }
    }

    /**
     * vacia el carrito
     */
    public void vaciarCarrito() {
        carrito.clear();
        total = 0;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<ItemCarrito> getItems() {
        return new ArrayList<>(carrito);
    }

    public boolean estaVacio() {
        return carrito.isEmpty();
    }

    public int getCantidadItems() {
        return carrito.size();
    }
}