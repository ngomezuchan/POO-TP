package excepciones;

/**
  la excepción para errores relacionados con la tienda
 */
public class TiendaException extends Exception {
    
    public TiendaException(String mensaje) {
        super(mensaje);
    }
    
    public TiendaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
