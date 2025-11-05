package modelo;

import excepciones.PagoException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tienda implements Serializable {

    private List<Cliente> clientes;
    private List<Producto> productos;
    private Cliente clienteActual;

    private static final String CLIENTES_FILE = "data/clientes.dat";
    private static final String PRODUCTOS_FILE = "data/productos.dat";

    public Tienda() {
        clientes = cargarClientes();
        productos = cargarProductos();
        clienteActual = null;

        if (productos.isEmpty()) {
            cargarDatosDemo();
            guardarProductos();
        }
    }

    // ---------- CLIENTES ----------
    public boolean registrarCliente(String nombre, String email, String password) {
        if (buscarClientePorNombre(nombre) != null) return false;
        clientes.add(new Cliente(nombre, email, password));
        guardarClientes();
        return true;
    }

    public boolean iniciarSesion(String nombre, String password) {
        Cliente c = buscarClientePorNombre(nombre);
        if (c != null && c.validarContrasena(password)) {
            clienteActual = c;
            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        clienteActual = null;
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public boolean hayClienteLogueado() {
        return clienteActual != null;
    }

    private Cliente buscarClientePorNombre(String nombre) {
        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(nombre)) return c;
        }
        return null;
    }

    // ---------- PRODUCTOS ----------
    public List<Producto> getProductos() {
        return productos;
    }

    public void agregarProducto(Producto p) {
        productos.add(p);
        guardarProductos();
    }

    public void eliminarProducto(Producto p) {
        productos.remove(p);
        guardarProductos();
    }

    public void cargarDatosDemo() {
        if (productos.isEmpty()) {
            productos.add(new Producto("P001", "Auriculares", 15000, 10));
            productos.add(new Producto("P002", "Mouse Gamer", 12000, 8));
            productos.add(new Producto("P003", "Teclado Mecánico", 18000, 6));
            productos.add(new Producto("P004", "Monitor 27\"", 90000, 4));
            productos.add(new Producto("P005", "Notebook Lenovo", 650000, 3));
        }
    }

    // ---------- PEDIDOS / PAGOS ----------
    public Pedido crearPedido(Carrito carrito) {
        if (clienteActual == null)
            throw new IllegalStateException("No hay cliente activo.");
        if (carrito.estaVacio())
            throw new IllegalStateException("El carrito está vacío.");

        return new Pedido(carrito.getItems());
    }

    public void procesarCompra(Pedido pedido) throws PagoException {
        for (ItemCarrito item : pedido.getItems()) {
            Producto p = item.getProducto();
            p.reducirStock(item.getCantidad());
        }
        if (clienteActual != null) {
            clienteActual.getCarrito().vaciarCarrito();
        }
        guardarProductos();
    }

    // ---------- PERSISTENCIA ----------
    private void guardarClientes() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CLIENTES_FILE));
            out.writeObject(clientes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarProductos() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PRODUCTOS_FILE));
            out.writeObject(productos);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Cliente> cargarClientes() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(CLIENTES_FILE))) {
            return (List<Cliente>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Producto> cargarProductos() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PRODUCTOS_FILE))) {
            return (List<Producto>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
