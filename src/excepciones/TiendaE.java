package excepciones;

/**
  la excepción para errores relacionados con la tienda
 */
public class TiendaE extends Exception {
    
    public TiendaE(String mensaje) {
        super(mensaje);
    }
    
    public TiendaE(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
