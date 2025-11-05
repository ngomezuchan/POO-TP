package modelo;

import excepciones.PagoException;
import java.util.Random;

public abstract class Pago {
    protected double monto;
    protected boolean procesado;
    protected String comprobante;

    public Pago(double monto) {
        this.monto = monto;
        this.procesado = false;
        this.comprobante = null;
    }

    public double getMonto() {
        return monto;
    }

    public boolean isProcesado() {
        return procesado;
    }

    public String obtenerComprobante() {
        return comprobante;
    }

    /** Cada tipo de pago valida sus datos (por ejemplo, tarjeta o CBU) */
    public abstract void validarDatos() throws PagoException;

    /** Cada tipo de pago procesa su lógica específica */
    public abstract void procesar() throws PagoException;

    /** 🔹 Genera un número aleatorio tipo comprobante */
    protected String generarComprobante() {
        int numero = new Random().nextInt(900000) + 100000; // 6 dígitos
        return "COMP-" + numero;
    }
}
