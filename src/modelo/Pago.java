package modelo;

import interfaces.IProcesadorPago;
import excepciones.PagoException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public abstract class Pago implements IProcesadorPago, Serializable {
    private static final long serialVersionUID = 1L;
    
    protected LocalDateTime fecha;
    protected double monto;
    protected boolean pagoProcesado;
    protected String numeroComprobante;
    
    public Pago(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        this.fecha = LocalDateTime.now();
        this.monto = monto;
        this.pagoProcesado = false;
    }
    
    /**
     * valida y procesa pago
     */
    public boolean validarPago() throws PagoException {
        if (monto <= 0) {
            throw new PagoException("Monto inválido para el pago");
        }
        
        if (pagoProcesado) {
            throw new PagoException("Este pago ya fue procesado");
        }
        
        // Validación específica de cada tipo de pago
        return validarDatos();
    }
    
    /**
     # muestra la información del pago
     */
    public void mostrar() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("\n=== INFORMACIÓN DEL PAGO ===");
        System.out.println("Fecha: " + fecha.format(formatter));
        System.out.printf("Monto: $%.2f\n", monto);
        System.out.println("Estado: " + (pagoProcesado ? "PROCESADO" : "PENDIENTE"));
        if (pagoProcesado && numeroComprobante != null) {
            System.out.println("Número de comprobante: " + numeroComprobante);
        }
        mostrarDetallesEspecificos();
        System.out.println("===========================");
    }

    protected abstract void mostrarDetallesEspecificos();
    
    @Override
    public boolean procesarPago(double monto) throws PagoException {
        if (validarPago()) {
            this.pagoProcesado = true;
            this.numeroComprobante = generarNumeroComprobante();
            return true;
        }
        return false;
    }
    
    protected String generarNumeroComprobante() {
        return "COMP-" + System.currentTimeMillis();
    }
    
    @Override
    public String obtenerComprobante() {
        return numeroComprobante;
    }
    
    // Getters y Setters
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public double getMonto() {
        return monto;
    }
    
    public boolean isPagoProcesado() {
        return pagoProcesado;
    }
    
    public String getNumeroComprobante() {
        return numeroComprobante;
    }
}
