package vistas;

import modelo.PagoTarjeta;
import excepciones.PagoException;

import javax.swing.*;
import java.awt.*;

public class dialogo_pago_tarjeta extends JDialog {
    private final double monto;
    private PagoTarjeta pago = null;

    private JTextField txt_numero;
    private JTextField txt_titular;
    private JTextField txt_vencimiento;
    private JTextField txt_cvv;
    private JButton btn_confirmar;

    public dialogo_pago_tarjeta(Frame owner, double monto) {
        super(owner, "Pago con Tarjeta", true);
        this.monto = monto;
        inicializar();
        pack();
        setLocationRelativeTo(owner);
    }

    private void inicializar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JLabel lblMonto = new JLabel(String.format("Monto a pagar: $%.2f", monto), SwingConstants.CENTER);
        lblMonto.setFont(lblMonto.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(lblMonto, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Número de tarjeta:"), gbc);
        gbc.gridx = 1;
        txt_numero = new JTextField(20);
        panel.add(txt_numero, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Titular:"), gbc);
        gbc.gridx = 1;
        txt_titular = new JTextField(20);
        panel.add(txt_titular, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Vencimiento (MM/AA):"), gbc);
        gbc.gridx = 1;
        txt_vencimiento = new JTextField(10);
        panel.add(txt_vencimiento, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        txt_cvv = new JTextField(5);
        panel.add(txt_cvv, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        btn_confirmar = new JButton("Confirmar Pago");
        panel.add(btn_confirmar, gbc);

        btn_confirmar.addActionListener(e -> procesarPago());
        setContentPane(panel);
    }

    private void procesarPago() {
        try {
            String numero = txt_numero.getText().trim();
            String titular = txt_titular.getText().trim();
            String venc = txt_vencimiento.getText().trim();
            String cvv = txt_cvv.getText().trim();

            if (numero.isEmpty() || titular.isEmpty() || venc.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }

            pago = new PagoTarjeta(monto, numero, titular, venc, cvv);
            pago.validarDatos();
            dispose();

        } catch (PagoException ex) {
            JOptionPane.showMessageDialog(this, "Error en el pago: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public PagoTarjeta getPago() {
        return pago;
    }
}
