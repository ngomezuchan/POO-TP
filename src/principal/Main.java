package principal;

import excepciones.AutenticacionException;
import modelo.Tienda;
import modelo.Cliente;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private final Tienda tienda;
    private final JTextField txt_usuario;
    private final JPasswordField txt_contrasena;

    public Main() {
        this.tienda = new Tienda();

        JPanel panel_principal = new JPanel(new GridLayout(4, 2, 10, 10));
        this.txt_usuario = new JTextField(15);
        this.txt_contrasena = new JPasswordField(15);
        JButton btn_ingresar = new JButton("Ingresar");
        JButton btn_registrar = new JButton("Registrar");

        panel_principal.add(new JLabel("Usuario:"));
        panel_principal.add(txt_usuario);
        panel_principal.add(new JLabel("Contraseña:"));
        panel_principal.add(txt_contrasena);
        panel_principal.add(btn_registrar);
        panel_principal.add(btn_ingresar);

        panel_principal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        setTitle("Tienda Online - Iniciar Sesión / Registrar");
        setContentPane(panel_principal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        btn_ingresar.addActionListener(e -> intentarLogin());
        btn_registrar.addActionListener(e -> intentarRegistro());
    }

    private void intentarLogin() {
        String nombre = txt_usuario.getText();
        String contrasena = new String(txt_contrasena.getPassword());

        try {
            Cliente cliente = tienda.iniciarSesion(nombre, contrasena);
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + cliente.getNombre() + "!");

            this.dispose();
            new vistas.ventana_tienda(tienda).setVisible(true);

        } catch (AutenticacionException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void intentarRegistro() {
        try {
            String nombre = txt_usuario.getText();
            String contrasena = new String(txt_contrasena.getPassword());

            if (nombre.trim().isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar nombre y contraseña para registrarse.",
                        "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            tienda.registrar(nombre, contrasena);

            JOptionPane.showMessageDialog(this, "¡Registro exitoso! Ya puede iniciar sesión.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (AutenticacionException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error en el registro: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Inicializa la aplicación Swing en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}