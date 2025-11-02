package vistas;

import modelo.PagoTransferencia;
import excepciones.PagoException;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class dialogo_pago_transferencia extends JDialog {

    private final double monto;
    private PagoTransferencia pago = null;

    // Componentes
    private JComboBox<String> cmb_bancos;
    private JButton btn_pagar;
    private JPanel panel_principal;

    // ⚠️ LISTA DE BANCOS CORREGIDA Y UNIFICADA CON LA VALIDACIÓN DEL MODELO ⚠️
    private static final List<String> BANCOS_DISPONIBLES = Arrays.asList(
            "Seleccione un Banco", // Opción por defecto
            "Banco Nacional",
            "Banco Provincial",
            "Banco Santander",
            "Banco BBVA",
            "Banco Galicia",
            "Banco Macro"
    );

    public dialogo_pago_transferencia(Frame owner, double monto) {
        super(owner, "Pagar con Transferencia", true); // Modal
        this.monto = monto;

        inicializarComponentes();

        setContentPane(panel_principal);
        pack();
        setLocationRelativeTo(owner);
    }

    private void inicializarComponentes() {
        panel_principal = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel lbl_monto = new JLabel("Monto a Pagar:");
        JLabel lbl_monto_valor = new JLabel(String.format("$%.2f", monto));

        // Crea el JComboBox con la lista unificada
        cmb_bancos = new JComboBox<>(BANCOS_DISPONIBLES.toArray(new String[0]));
        btn_pagar = new JButton("Confirmar Transferencia");

        // Añadir componentes al panel
        panel_principal.add(lbl_monto);
        panel_principal.add(lbl_monto_valor);
        panel_principal.add(new JLabel("Seleccione Banco:"));
        panel_principal.add(cmb_bancos);
        panel_principal.add(new JLabel());
        panel_principal.add(btn_pagar);

        btn_pagar.addActionListener(e -> procesarDatos());

        panel_principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void procesarDatos() {
        String banco = (String) cmb_bancos.getSelectedItem();

        // ⚠️ VALIDACIÓN MEJORADA: si no selecciona nada o es la opción por defecto
        if (banco == null || banco.equals("Seleccione un Banco") || banco.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un banco válido.",
                    "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Se crea el objeto de pago
            PagoTransferencia nuevoPago = new PagoTransferencia(monto, banco);

            // Se valida que el banco exista en la lista del modelo
            nuevoPago.validarDatos();

            // Si la validación no lanza excepción, el pago es válido para ser procesado
            this.pago = nuevoPago;
            this.dispose();

        } catch (PagoException e) {
            // Captura el error si el banco no es válido según el modelo (PagoTransferencia.java)
            JOptionPane.showMessageDialog(this, "Error de validación: " + e.getMessage(),
                    "Error de Pago", JOptionPane.ERROR_MESSAGE);
        }
    }

    public PagoTransferencia getPago() {
        return pago;
    }
}