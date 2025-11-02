package interfaces;

import excepciones.PagoException;


public interface IProcesadorPago {
    boolean procesarPago(double monto) throws PagoException;
    boolean validarDatos() throws PagoException;
    String obtenerComprobante();
}
