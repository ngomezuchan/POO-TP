package modelo;

import interfaces.IAutenticacion;
import excepciones.AutentiE;
import servicios.GestorPersistencia;
import java.util.ArrayList;
import java.util.List;


public class Tienda implements IAutenticacion {
    
    private Cliente clienteActual;
    private List<Cliente> clientes;
    private List<Producto> productos;
    private List<Pedido> pedidos;
    private GestorPersistencia<Cliente> gestorClientes;
    private GestorPersistencia<Producto> gestorProductos;
    private GestorPersistencia<Pedido> gestorPedidos;
    
    public Tienda() {
        this.clientes = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.clienteActual = null;
        
        // Inicializar gestores de persistencia
        this.gestorClientes = new GestorPersistencia<>("clientes.dat");
        this.gestorProductos = new GestorPersistencia<>("productos.dat");
        this.gestorPedidos = new GestorPersistencia<>("pedidos.dat");
        
        // cargar datos desde archivos
        cargarDatos();
        
        // si no hay productos, cargar algunos de ejemplo
        if (productos.isEmpty()) {
            inicializarProductosEjemplo();
        }
    }

    @Override
    public Cliente iniciarSesion(String nombre, String contrasena) throws AutentiE {
        if (clienteActual != null) {
            throw new AutentiE("ya hay una sesion activa");
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new AutentiE("el nombre de usuario no puede estar vacío");
        }
        
        if (contrasena == null || contrasena.isEmpty()) {
            throw new AutentiE("la contraseña no puede estar vacía");
        }
        
        for (Cliente cliente : clientes) {
            if (cliente.getNombre().equalsIgnoreCase(nombre)) {
                if (cliente.validarContrasena(contrasena)) {
                    clienteActual = cliente;
                    System.out.println("sesioon iniciada exitosamente. Bienvenido, " + cliente.getNombre());
                    return cliente;
                } else {
                    throw new AutentiE("contraseña incorrecta");
                }
            }
        }
        
        throw new AutentiE("usuario no encontrado: " + nombre);
    }
    
    /**
     # registra nuevo cliente
     */
    @Override
    public boolean registrar(Cliente cliente) throws AutentiE {
        if (cliente == null) {
            throw new AutentiE("Los datos del cliente no pueden ser null");
        }
        
        // se fija si ya existia
        for (Cliente c : clientes) {
            if (c.getNombre().equalsIgnoreCase(cliente.getNombre())) {
                throw new AutentiE("Ya existe un usuario con ese nombre");
            }
        }
        
        clientes.add(cliente);
        guardarClientes(); // guarda cliente
        System.out.println("Cliente registrado exitosamente: " + cliente.getNombre());
        return true;
    }

    public boolean registrar(String nombre, String contrasena) throws AutentiE {
        Cliente nuevoCliente = new Cliente(nombre, contrasena);
        return registrar(nuevoCliente);
    }
    
    /**
     # cierra sesion
     */
    @Override
    public void cerrarSesion() {
        if (clienteActual != null) {
            System.out.println("Cerrando sesión de " + clienteActual.getNombre());
            clienteActual = null;
        } else {
            System.out.println("No hay ninguna sesión activa");
        }
    }

    public Pedido crearPedido(Carrito carrito) throws IllegalStateException {
        if (clienteActual == null) {
            throw new IllegalStateException("Debe iniciar sesión para crear un pedido");
        }
        
        if (carrito == null || carrito.estaVacio()) {
            throw new IllegalStateException("El carrito no puede estar vacío");
        }
        
        Pedido nuevoPedido = new Pedido(clienteActual, carrito);
        pedidos.add(nuevoPedido);
        guardarPedidos(); // guarda pedido
        
        return nuevoPedido;
    }
    
    /**
     busca un producto por ID
     */
    public Producto buscarProducto(String id) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
    
    /**
     # muestra todos los productos disponibles
     */
    public void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos disponibles");
            return;
        }
        
        System.out.println("\n=== CATÁLOGO DE PRODUCTOS ===");
        for (Producto p : productos) {
            System.out.println(p);
        }
        System.out.println("==============================\n");
    }
    
    /**
     # inicializa productos de ejemplo
     */
    private void inicializarProductosEjemplo() {
        productos.add(new Producto("P001", "Laptop Dell", 45000.00, 10));
        productos.add(new Producto("P002", "Mouse Logitech", 1500.00, 50));
        productos.add(new Producto("P003", "Teclado Mecánico", 5500.00, 25));
        productos.add(new Producto("P004", "Monitor 24\"", 18000.00, 15));
        productos.add(new Producto("P005", "Auriculares Gamer", 3500.00, 30));
        productos.add(new Producto("P006", "Webcam HD", 4500.00, 20));
        productos.add(new Producto("P007", "Mousepad XXL", 800.00, 40));
        productos.add(new Producto("P008", "Cable HDMI", 450.00, 100));
        
        guardarProductos();
    }
    
    /**
     # carga los datos desde archivos
     */
    private void cargarDatos() {
        try {
            clientes = gestorClientes.cargarTodos();
            System.out.println("Clientes cargados: " + clientes.size());
        } catch (Exception e) {
            System.out.println("No se pudieron cargar los clientes: " + e.getMessage());
        }
        
        try {
            productos = gestorProductos.cargarTodos();
            System.out.println("Productos cargados: " + productos.size());
        } catch (Exception e) {
            System.out.println("No se pudieron cargar los productos: " + e.getMessage());
        }
        
        try {
            pedidos = gestorPedidos.cargarTodos();
            System.out.println("pedidos cargados: " + pedidos.size());
        } catch (Exception e) {
            System.out.println("no se pudieron cargar los pedidos: " + e.getMessage());
        }
    }
    
    /**
     # guarda los clientes en archivo
     */
    private void guardarClientes() {
        try {
            gestorClientes.guardarLista(clientes);
        } catch (Exception e) {
            System.err.println("error al guardar clientes: " + e.getMessage());
        }
    }
    
    /**
     # guarda los productos en archivo
     */
    private void guardarProductos() {
        try {
            gestorProductos.guardarLista(productos);
        } catch (Exception e) {
            System.err.println("eerror al guardar productos: " + e.getMessage());
        }
    }
    
    /**
     # Guarda los pedidos en archivo
     */
    private void guardarPedidos() {
        try {
            gestorPedidos.guardarLista(pedidos);
        } catch (Exception e) {
            System.err.println("error al guardar pedidos: " + e.getMessage());
        }
    }
    
    // getters
    public Cliente getClienteActual() {
        return clienteActual;
    }
    
    public List<Producto> getProductos() {
        return new ArrayList<>(productos); // devuelve copia para la encapsulación
    }
    
    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }
    
    public List<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }
    
    public boolean hayClienteLogueado() {
        return clienteActual != null;
    }
}
