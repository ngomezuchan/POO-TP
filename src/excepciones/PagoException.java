package excepciones;

/**
  excepcion para los problemas relacionados con pagos
 */
public class PagoException extends TiendaException {
    
    public PagoException(String mensaje) {
        super(mensaje);
    }
}
