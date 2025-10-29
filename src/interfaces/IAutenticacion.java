package interfaces;

import modelo.Cliente;
import excepciones.AutentiE;


public interface IAutenticacion {
    Cliente iniciarSesion(String nombre, String contrasena) throws AutentiE;
    void cerrarSesion();
    boolean registrar(Cliente cliente) throws AutentiE;
}
