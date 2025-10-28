package modelo;

import excepciones.PagoException;

public class PagoTransferencia extends Pago {
    private static final long serialVersionUID = 1L;
    
    private int numeroComprobante;
    private String banco;
    
    public PagoTransferencia(double monto, String banco) {
        super(monto);
        this.banco = banco;
        this.numeroComprobante = generarNumeroComprobanteTransferencia();
    }
    
    private int generarNumeroComprobanteTransferencia() {
        return (int) (Math.random() * 900000) + 100000;
    }
    
    @Override
    public boolean validarDatos() throws PagoException {
        if (banco == null || banco.trim().isEmpty()) {
            throw new PagoException("El banco no puede estar vacío");
        }
        
        // Simulación de validación bancaria
        String[] bancosValidos = {"Banco Nacional", "Banco Provincial", "Banco Santander", 
                                 "Banco BBVA", "Banco Galicia", "Banco Macro"};
        
        boolean bancoValido = false;
        for (String b : bancosValidos) {
            if (b.equalsIgnoreCase(banco)) {
                bancoValido = true;
                break;
            }
        }
        
        if (!bancoValido) {
            throw new PagoException("Banco no válido: " + banco);
        }
        
        return true;
    }
    
    @Override
    protected void mostrarDetallesEspecificos() {
        System.out.println("Tipo de pago: TRANSFERENCIA");
        System.out.println("Banco: " + banco);
        System.out.println("Número de comprobante bancario: " + numeroComprobante);
    }
    
    @Override
    public boolean procesarPago(double monto) throws PagoException {
        System.out.println("Procesando transferencia bancaria...");
        System.out.println("Conectando con " + banco + "...");
        
        // Simulación de procesamiento
        try {
            Thread.sleep(1000); // Simula tiempo de procesamiento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        boolean resultado = super.procesarPago(monto);
        
        if (resultado) {
            System.out.println("Transferencia aprobada - Comprobante: " + numeroComprobante);
        } else {
            System.out.println("Transferencia rechazada");
        }
        
        return resultado;
    }
    
    // getters y setters
    public int getNumeroComprobanteTransferencia() {
        return numeroComprobante;
    }
    
    public String getBanco() {
        return banco;
    }
    
    public void setBanco(String banco) {
        this.banco = banco;
    }
}
