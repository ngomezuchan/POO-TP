package modelo;

import excepciones.PagoException;

public class PagoTransferencia extends Pago {
    private String cbuOrigen;
    private String aliasOrigen;

    public PagoTransferencia(double monto, String cbuOrigen, String aliasOrigen) {
        super(monto);
        this.cbuOrigen = cbuOrigen;
        this.aliasOrigen = aliasOrigen;
    }

    @Override
    public void validarDatos() throws PagoException {
        if ((cbuOrigen == null || cbuOrigen.length() < 10) && (aliasOrigen == null || aliasOrigen.isEmpty())) {
            throw new PagoException("Debe ingresar CBU o alias válidos.");
        }
    }

    @Override
    public void procesar() throws PagoException {
        this.procesado = true;
        this.comprobante = generarComprobante();
    }
}
