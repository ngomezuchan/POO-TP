package vistas;

import modelo.PagoTransferencia;
import excepciones.PagoException;

import javax.swing.*;
import java.awt.*;


public class dialogo_pago_transferencia extends JDialog {

    private final double monto;
    private PagoTransferencia pago = null;

    private JTextField txt_cbu;
    private JTextField txt_alias;
    private JButton btn_confirmar;
    private JPanel panel_principal;

    public dialogo_pago_transferencia(Frame owner, double monto) {
        super(owner, "Pagar por Transferencia", true);
        this.monto = monto;
        inicializarComponentes();
        setContentPane(panel_principal);
        pack();
        setLocationRelativeTo(owner);
    }

    private void inicializarComponentes() {
        panel_principal = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel lbl_monto = new JLabel("Monto a transferir:");
        JLabel lbl_monto_valor = new JLabel(String.format("$%.2f", monto));

        txt_cbu = new JTextField(22);
        txt_alias = new JTextField(22);
        btn_confirmar = new JButton("Confirmar Pago");

        panel_principal.add(lbl_monto);
        panel_principal.add(lbl_monto_valor);

        panel_principal.add(new JLabel("CBU (22 dígitos):"));
        panel_principal.add(txt_cbu);

        panel_principal.add(new JLabel("Alias:"));
        panel_principal.add(txt_alias);

        panel_principal.add(new JLabel());
        panel_principal.add(btn_confirmar);

        btn_confirmar.addActionListener(e -> procesarPago());

        panel_principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void procesarPago() {
        try {
            String cbu = txt_cbu.getText().trim();
            String alias = txt_alias.getText().trim();

            if (cbu.isEmpty() || alias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!cbu.matches("\\d{22}")) {
                JOptionPane.showMessageDialog(this, "El CBU debe tener exactamente 22 dígitos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear el pago usando el constructor correcto
            PagoTransferencia nuevoPago = new PagoTransferencia(monto, cbu, alias);
            nuevoPago.validarDatos();

            this.pago = nuevoPago;
            this.dispose();

        } catch (PagoException ex) {
            JOptionPane.showMessageDialog(this, "Error en la transferencia: " + ex.getMessage(), "Error de pago", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public PagoTransferencia getPago() {
        return pago;
    }
}
