package modelo;

import excepciones.PagoE;

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
        return (int) (Math.random() * 900000) + 100000;} //para el numero ese gigante del comprobante
    
    @Override
    public boolean validarDatos() throws PagoE {
        if (banco == null || banco.trim().isEmpty()) {
            throw new PagoE("El banco no puede estar vacío");
        }
        
        // seleccionar bancos
        String[] bancosValidos = {"Banco Nacional", "Banco Provincial", "Banco Santander", 
                                 "Banco BBVA", "Banco Galicia", "Banco Macro"};
        
        boolean bancoValido = false;
        for (String b : bancosValidos) {
            if (b.equalsIgnoreCase(banco)) {
                bancoValido = true;
                break;
                 //los bancos los podes poner en mayuscula y en miniscula
            }
        }
        
        if (!bancoValido) {
            throw new PagoE("Banco no válido: " + banco);
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
    public boolean procesarPago(double monto) throws PagoE {
        System.out.println("Procesando transferencia bancaria...");
        System.out.println("Conectando con " + banco + "...");
        return true;
    }

    // getters y setters
    /**public int getNumeroComprobanteTransferencia() {
        return numeroComprobante;
    }*/ 
    //revisar
    
    public String getBanco() {
        return banco;
    }
    
    public void setBanco(String banco) {
        this.banco = banco;
    }
}
