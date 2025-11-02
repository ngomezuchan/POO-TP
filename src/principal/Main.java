package principal;
import excepciones.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import modelo.*;


public class Main {

    private static Tienda tienda;
    private static Scanner scanner;
    private static Carrito carritoActual;

    public static void main(String[] args) {
        tienda = new Tienda();
        scanner = new Scanner(System.in);
        carritoActual = new Carrito();
        
        menuPrincipal();
        
        scanner.close();
        System.out.println("\n¡Gracias por su tiempo!");
    }
    
    /**
     menu sistema
     */
    private static void menuPrincipal() {
        int opcion;
        
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    iniciarSesion();
                    break;
                case 2:
                    registrarCliente();
                    break;
                case 3:
                    if (verificarSesion()) {
                        menuTienda();
                    }
                    break;
                case 4:
                    cerrarSesion();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
    
    /**
     muestra el menú principal
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\nMENU");
        
        if (tienda.hayClienteLogueado()) {
            System.out.println("Usuario: " + tienda.getClienteActual().getNombre());
        }
        
        System.out.println("1. Iniciar Sesion");
        System.out.println("2. Registrar Nuevo Cliente");
        System.out.println("3. Ingresar a la Tienda");
        System.out.println("4. Cerrar Sesion");

        System.out.println("0. Salir");

        System.out.print("Seleccione una opcion: ");
    }
    

    private static void menuTienda() {
        int opcion;
        
        do {
            mostrarMenuTienda();
            opcion = leerOpcion();
            //preguntar si le jode que use break, es lo mas facil de usar
            switch (opcion) {
                case 1:
                    tienda.mostrarProductos();
                    break;
                case 2:
                    agregarProductoAlCarrito();
                    break;
                case 3:
                    carritoActual.mostrarCarrito();
                    break;
                case 4:
                    eliminarProductoDelCarrito();
                    break;
                case 5:
                    realizarPedido();
                    break;
                case 6:
                    carritoActual.vaciarCarrito();
                    System.out.println("Carrito vaciado exitosamente");
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }
    
    /**
     muestra el menú de la tienda
     */
    private static void mostrarMenuTienda() {
        System.out.println("\nTIENDA");
        System.out.println(" Cliente: " + tienda.getClienteActual().getNombre());
        System.out.println(" Items en carrito: " + carritoActual.getCantidadItems());

        System.out.println(" 1. Ver catalogo de productos");
        System.out.println(" 2. Agregar producto al carrito");
        System.out.println(" 3. Ver carrito");
        System.out.println(" 4. Eliminar producto del carrito");
        System.out.println(" 5. Realizar pedido");
        System.out.println(" 6. Vaciar carrito");
        System.out.println(" 0. Volver al menu principal");

        System.out.print("Seleccione una opción: ");
    }
    
    /**
      inicia sesión de un cliente
     */
    private static void iniciarSesion() {
        try {
            System.out.print("Ingrese nombre de usuario: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Ingrese contraseña: ");
            String contrasena = scanner.nextLine();
            
            tienda.iniciarSesion(nombre, contrasena);
            carritoActual = new Carrito(); // Nuevo carrito para la sesión
            
        } catch (AutentiE e) {
            System.err.println("Error de autenticación: " + e.getMessage());
        }
    }
    
    /**
     para registrarr un nuevo cliente
     */
    private static void registrarCliente() {
        try {
            System.out.println("\n--- REGISTRO DE NUEVO CLIENTE ---");
            
            System.out.print("Ingrese nombre de usuario: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Ingrese contraseña (minimo 4 caracteres): ");
            String contrasena = scanner.nextLine();
            
            System.out.print("Confirme contraseña: ");
            String confirmacion = scanner.nextLine();
            
            if (!contrasena.equals(confirmacion)) {
                System.err.println("Las contraseñass no coinciden");
                return;
            }
            
            tienda.registrar(nombre, contrasena);
            
        } catch (AutentiE e) {
            System.err.println("Error en el registro: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Datos invalidos: " + e.getMessage());
        }
    }
    
    /**
      cierra la sesion actual
     */
    private static void cerrarSesion() {
        tienda.cerrarSesion();
        carritoActual = new Carrito(); // carrito a 0
    }
    
    /**
     agrega un producto al carrito
     */
    private static void agregarProductoAlCarrito() {
        try {
            tienda.mostrarProductos();
            
            System.out.print("Ingrese el ID del producto: ");
            String idProducto = scanner.nextLine();
            
            Producto producto = tienda.buscarProducto(idProducto);
            if (producto == null) {
                System.err.println("Producto no encontrado");
                return;
            }
            
            System.out.println("Producto seleccionado: " + producto.getNombre());
            System.out.println("Precio: $" + producto.getPrecio());
            System.out.println("Stock disponible: " + producto.getStock());
            
            System.out.print("Ingrese la cantidad: ");
            int cantidad = leerEntero();
            
            carritoActual.agregarProducto(producto, cantidad);
            System.out.println("Producto agregado al carrito exitosamente");
            
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Datos inválidos: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un producto del carrito
     */
    private static void eliminarProductoDelCarrito() {
        if (carritoActual.estaVacio()) {
            System.out.println("El carrito está vacío");
            return;
        }
        
        carritoActual.mostrarCarrito();
        
        System.out.print("Ingrese el ID del producto a eliminar: ");
        String idProducto = scanner.nextLine();
        
        try {
            carritoActual.eliminarProducto(idProducto);
            System.out.println("Producto eliminado del carrito");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Realiza un pedido con el carrito actual
     */
    private static void realizarPedido() {
        if (carritoActual.estaVacio()) {
            System.out.println("El carrito está vacío. Agregue productos antes de realizar un pedido.");
            return;
        }
        
        try {
            // crea pedido
            Pedido pedido = tienda.crearPedido(carritoActual);
            pedido.mostrarResumen();
            

            System.out.println("\n--- MÉTODO DE PAGO ---");
            System.out.println("1. Tarjeta de Crédito/Débito");
            System.out.println("2. Transferencia Bancaria");
            System.out.print("Seleccione método de pago: ");
            
            int metodoPago = leerEntero();
            Pago pago = null;
            
            switch (metodoPago) {
                case 1:
                    pago = crearPagoTarjeta(pedido.getMontoTotal());
                    break;
                case 2:
                    pago = crearPagoTransferencia(pedido.getMontoTotal());
                    break;
                default:
                    System.err.println("Método de pago inválido");
                    return;
            }
            
            if (pago != null) {
                pedido.procesarPago(pago);
                carritoActual = new Carrito(); // borra el carrito dsp de pagar
                
                System.out.println("\n¡PEDIDO COMPLETADO EXITOSAMENTE!");
                pedido.mostrarResumen();
            }
            
        } catch (IllegalStateException e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
        } catch (PagoE e) {
            System.err.println("Error en el pago: " + e.getMessage());
        }
    }
    
    /**
      crea pago con tarjeta
     */
    private static PagoTarjeta crearPagoTarjeta(double monto) {
        try {
            System.out.println("\n--- DATOS DE LA TARJETA ---");
            
            System.out.print("nombre del titular: ");
            String nombre = scanner.nextLine();
            
            System.out.print("fecha de vencimiento (MMYY): ");
            int vencimiento = leerEntero();
            
            System.out.print("codigo de seguridad (CVV): ");
            int cvv = leerEntero();
            
            return new PagoTarjeta(monto, nombre, vencimiento, cvv);
            
        } catch (Exception e) {
            System.err.println("eerror al crear pago con tarjeta: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * crea pago por transferencia
     */
    private static PagoTransferencia crearPagoTransferencia(double monto) {
        try {
            System.out.println("\n--- DATOS DE LA TRANSFERENCIA ---");
            System.out.println("Bancos disponibles:");
            System.out.println("- Banco Provincial");
            System.out.println("- Banco Santander");
            System.out.println("- Banco BBVA");

            
            System.out.print("Ingrese el nombre del banco: ");
            String banco = scanner.nextLine();
            
            return new PagoTransferencia(monto, banco);
            
        } catch (Exception e) {
            System.err.println("error al crear transferencia: " + e.getMessage());
            return null;
        }
    }
    
    /**
     se fija si hay una sesión activa
     */
    private static boolean verificarSesion() {
        if (!tienda.hayClienteLogueado()) {
            System.err.println("Deberiaa iniciar sesión para acceder a la tienda");
            return false;
        }
        return true;
    }
    

    
    /**
     * lee una opción entera del usuario
     */
    private static int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            return opcion;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }
    
    /**
     * lee un entero del usuario
     */
    private static int leerEntero() {
        try {
            int valor = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            return valor;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpiar buffer
            throw new IllegalArgumentException("Debe ingresar un número entero");
        }
    }
}
