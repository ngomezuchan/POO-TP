package interfaces;

import modelo.Cliente;
import excepciones.AutentiE;


public interface IAutenticacion {
    Cliente iniciarSesion(String nombre, String contrasena) throws AutentiE;
    void cerrarSesion();
    boolean registrar(Cliente cliente) throws AutentiE;
}
// esto para en caso de que ya haya un cliente registrado y se este registrando con el mismo nombre yy contrasena
