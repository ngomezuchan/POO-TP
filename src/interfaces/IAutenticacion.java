package interfaces;

import excepciones.AutenticacionException;
import modelo.Cliente;


public interface IAutenticacion {
    Cliente iniciarSesion(String nombre, String contrasena) throws AutenticacionException;
    void cerrarSesion();
    boolean registrar(Cliente cliente) throws AutenticacionException;
}
// esto para en caso de que ya haya un cliente registrado y se este registrando con el mismo nombre yy contrasena
