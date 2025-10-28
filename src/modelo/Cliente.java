package modelo;

import java.io.Serializable;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private int id;
    private String contrasena;
    
    public Cliente(String nombre, int id, String contrasena) {
        this.nombre = nombre;
        this.id = id;
        this.contrasena = contrasena;
    }

    public Cliente(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.id = generarId();
    }
    
    private int generarId() {
        // genera un ID basado en el tiempo actual
        return (int) (System.currentTimeMillis() % 1000000);
    }
    
    // getters y setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }
        this.id = id;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        if (contrasena == null || contrasena.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres");
        }
        this.contrasena = contrasena;
    }
    
    public boolean validarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }
    
    @Override
    public String toString() {
        return String.format("Cliente [ID: %d, Nombre: %s]", id, nombre);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente cliente = (Cliente) obj;
        return id == cliente.id;
    }
}
