package modelo;

import excepciones.PagoException;
import java.time.YearMonth;


public class PagoTarjeta extends Pago {
    private static final long serialVersionUID = 1L;
    
    private String nombreTarjeta;
    private int vencimientoTarjeta;
    private int codigoTarjeta;
    
    public PagoTarjeta(double monto, String nombreTarjeta, int vencimientoTarjeta, int codigoTarjeta) {
        super(monto);
        this.nombreTarjeta = nombreTarjeta;
        this.vencimientoTarjeta = vencimientoTarjeta;
        this.codigoTarjeta = codigoTarjeta;
    }
    
    @Override
    public boolean validarDatos() throws PagoException {
        if (nombreTarjeta == null || nombreTarjeta.trim().isEmpty()) {
            throw new PagoException("El nombre del titular no puede estar vacío");
        }
        
        if (nombreTarjeta.length() < 3) {
            throw new PagoException("El nombre del titular es muy corto");
        }
        

        if (codigoTarjeta < 100 || codigoTarjeta > 9999) {
            throw new PagoException("Código de seguridad inválido (debe ser de 3 o 4 dígitos)");
        }
        

        validarFechaVencimiento();
        
        return true;
    }
    
    private void validarFechaVencimiento() throws PagoException {
        int mes = vencimientoTarjeta / 100;
        int año = vencimientoTarjeta % 100;
        
        if (mes < 1 || mes > 12) {
            throw new PagoException("Mes de vencimiento inválido");
        }
        

        int añoCompleto = 2000 + año;
        

        YearMonth vencimiento = YearMonth.of(añoCompleto, mes);
        YearMonth actual = YearMonth.now();
        
        if (vencimiento.isBefore(actual)) {
            throw new PagoException("La tarjeta está vencida");
        }
    }
    
    @Override
    protected void mostrarDetallesEspecificos() {
        System.out.println("Tipo de pago: TARJETA");
        System.out.println("Titular: " + nombreTarjeta);
        System.out.println("Tarjeta: **** **** **** " + obtenerUltimos4Digitos());
        System.out.printf("Vencimiento: %02d/%02d\n", 
                         vencimientoTarjeta / 100, vencimientoTarjeta % 100);
    }
    
    private String obtenerUltimos4Digitos() {
        // simulación de últimos 4 dígitos de la tarjeta
        return String.format("%04d", (int)(Math.random() * 10000));
    }


    // getters y setters
    public String getNombreTarjeta() {
        return nombreTarjeta;
    }
    
    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }
    
    public int getVencimientoTarjeta() {
        return vencimientoTarjeta;
    }
    
    public void setVencimientoTarjeta(int vencimientoTarjeta) {
        this.vencimientoTarjeta = vencimientoTarjeta;
    }
    
    public int getCodigoTarjeta() {
        return codigoTarjeta;
    }
    
    public void setCodigoTarjeta(int codigoTarjeta) {
        this.codigoTarjeta = codigoTarjeta;
    }
}
