package excepciones;

/**
 excepcion para los problemas relacionados con pagos
 */
public class PagoException extends Exception {

    public PagoException(String mensaje) {
        super(mensaje);
    }
}
