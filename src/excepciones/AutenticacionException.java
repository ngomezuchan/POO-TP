package excepciones;

/**
  la excepción por problemas de autenticación
 */
public class AutenticacionException extends TiendaException {
    
    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}
