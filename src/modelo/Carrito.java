package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrito implements Serializable {
    private List<ItemCarrito> items;

    public Carrito() {
        this.items = new ArrayList<>();
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        if (!producto.hayStock(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para el producto seleccionado.");
        }

        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                int nuevaCantidad = item.getCantidad() + cantidad;
                if (!producto.hayStock(nuevaCantidad)) {
                    throw new IllegalStateException("No hay suficiente stock para agregar esa cantidad.");
                }
                items.set(items.indexOf(item), new ItemCarrito(producto, nuevaCantidad));
                return;
            }
        }

        items.add(new ItemCarrito(producto, cantidad));
    }

    public void eliminarProducto(String productoId) {
        items.removeIf(i -> i.getProducto().getId().equals(productoId));
    }

    public void vaciarCarrito() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public int getCantidadItems() {
        int total = 0;
        for (ItemCarrito i : items) {
            total += i.getCantidad();
        }
        return total;
    }

    public double getTotal() {
        double total = 0;
        for (ItemCarrito i : items) {
            total += i.getSubtotal();
        }
        return total;
    }
    public void mostrarCarrito() {
        if (items.isEmpty()) {
            System.out.println("🛒 El carrito está vacío.");
            return;
        }

        System.out.println("\n🛒 Carrito actual:");
        for (ItemCarrito item : items) {
            System.out.printf(" - %s x%d → $%.2f%n",
                    item.getProducto().getNombre(),
                    item.getCantidad(),
                    item.getSubtotal());
        }
        System.out.printf("TOTAL: $%.2f%n", getTotal());
    }

}
