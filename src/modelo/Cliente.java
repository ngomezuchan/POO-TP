package modelo;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String email;
    private String contrasena;
    private Carrito carrito;

    public Cliente(String nombre, String email, String contrasena) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.carrito = new Carrito();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean validarContrasena(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }

    public Carrito getCarrito() {
        return carrito;
    }
}
