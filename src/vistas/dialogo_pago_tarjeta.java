package vistas;

import modelo.PagoTarjeta;
import excepciones.PagoException;
import javax.swing.*;
import java.awt.*;

public class dialogo_pago_tarjeta extends JDialog {

    private final double monto;
    private PagoTarjeta pago = null;

    // Componentes
    private JTextField txt_nombre;
    private JTextField txt_vencimiento;
    private JTextField txt_cvv;
    private JButton btn_pagar;
    private JPanel panel_principal;

    public dialogo_pago_tarjeta(Frame owner, double monto) {
        super(owner, "Pagar con Tarjeta", true);
        this.monto = monto;

        inicializarComponentes();

        setContentPane(panel_principal);
        pack();
        setLocationRelativeTo(owner);
    }

    private void inicializarComponentes() {
        panel_principal = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel lbl_monto = new JLabel("Monto a Pagar:");
        JLabel lbl_monto_valor = new JLabel(String.format("$%.2f", monto));

        txt_nombre = new JTextField(20);
        txt_vencimiento = new JTextField(10);
        txt_cvv = new JTextField(5);
        btn_pagar = new JButton("Confirmar Pago");

        //
        // componentes al panel
        panel_principal.add(lbl_monto);
        panel_principal.add(lbl_monto_valor);
        panel_principal.add(new JLabel("Titular:"));
        panel_principal.add(txt_nombre);
        panel_principal.add(new JLabel("Vencimiento (MMYY):"));
        panel_principal.add(txt_vencimiento);
        panel_principal.add(new JLabel("CVV:"));
        panel_principal.add(txt_cvv);
        panel_principal.add(new JLabel());
        panel_principal.add(btn_pagar);

        btn_pagar.addActionListener(e -> procesarDatos());

        panel_principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void procesarDatos() {
        try {
            String nombre = txt_nombre.getText();
            int vencimiento = Integer.parseInt(txt_vencimiento.getText());
            int cvv = Integer.parseInt(txt_cvv.getText());

            PagoTarjeta nuevoPago = new PagoTarjeta(monto, nombre, vencimiento, cvv);

            nuevoPago.validarDatos();

            this.pago = nuevoPago;
            this.dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vencimiento y CVV deben ser números válidos.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (PagoException e) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + e.getMessage(),
                    "Error de Tarjeta", JOptionPane.ERROR_MESSAGE);
        }
    }

    public PagoTarjeta getPago() {
        return pago;
    }
}