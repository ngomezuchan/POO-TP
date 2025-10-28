package interfaces;

import modelo.Cliente;
import excepciones.AutenticacionException;


public interface IAutenticacion {
    Cliente iniciarSesion(String nombre, String contrasena) throws AutenticacionException;
    void cerrarSesion();
    boolean registrar(Cliente cliente) throws AutenticacionException;
}
