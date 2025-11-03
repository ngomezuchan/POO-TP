package interfaces;

import excepciones.PagoException;


public interface IProcesadorPago {
    boolean procesarPago(double monto) throws PagoException;
    boolean validarDatos() throws PagoException;
    String obtenerComprobante();
}
//esto lo hice opcional para darle un tono de realidad y simular la compra