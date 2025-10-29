package interfaces;

import excepciones.PagoE;


public interface IProcesadorPago {
    boolean procesarPago(double monto) throws PagoE;
    boolean validarDatos() throws PagoE;
    String obtenerComprobante();
}
//esto lo hice opcional para darle un tono de realidad y simular la compra