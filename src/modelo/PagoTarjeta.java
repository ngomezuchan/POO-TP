package modelo;

import excepciones.PagoException;

public class PagoTarjeta extends Pago {
    private String numeroTarjeta;
    private String nombreTitular;
    private String vencimiento;
    private String cvv;

    public PagoTarjeta(double monto, String numeroTarjeta, String nombreTitular, String vencimiento, String cvv) {
        super(monto);
        this.numeroTarjeta = numeroTarjeta;
        this.nombreTitular = nombreTitular;
        this.vencimiento = vencimiento;
        this.cvv = cvv;
    }

    @Override
    public void validarDatos() throws PagoException {
        if (numeroTarjeta == null || numeroTarjeta.length() < 12)
            throw new PagoException("Número de tarjeta inválido.");
        if (cvv == null || cvv.length() != 3)
            throw new PagoException("CVV inválido.");
        if (nombreTitular == null || nombreTitular.isEmpty())
            throw new PagoException("Debe ingresar el nombre del titular.");
    }

    @Override
    public void procesar() throws PagoException {
        // Simula el procesamiento (en un sistema real habría API bancaria)
        this.procesado = true;
        this.comprobante = generarComprobante();
    }
}
