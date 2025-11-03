package vistas;

import excepciones.AutenticacionException;
import modelo.Tienda;
import modelo.Cliente;

import javax.swing.*;
import java.awt.*;

public class ventana_login extends JFrame {

    // ⚠️ Ahora estas variables serán inicializadas AQUÍ, no por el .form
    private JPanel panel_principal;
    private JTextField txt_usuario;
    private JPasswordField txt_contrasena;
    private JButton btn_ingresar;
    private JButton btn_registrar;

    private final Tienda tienda;

    public ventana_login() {
        // 1. Inicializa el modelo
        this.tienda = new Tienda();

        // 2. CONSTRUCCIÓN MANUAL DE LA GUI
        // Inicializa los componentes
        panel_principal = new JPanel(new GridLayout(4, 2, 10, 10)); // Usamos GridLayout para organizar
        txt_usuario = new JTextField(15);
        txt_contrasena = new JPasswordField(15);
        btn_ingresar = new JButton("Ingresar");
        btn_registrar = new JButton("Registrar");

        // Agrega los componentes al panel principal
        panel_principal.add(new JLabel("Usuario:"));
        panel_principal.add(txt_usuario);
        panel_principal.add(new JLabel("Contraseña:"));
        panel_principal.add(txt_contrasena);
        panel_principal.add(btn_registrar);
        panel_principal.add(btn_ingresar);

        // Añadir un borde para estética
        panel_principal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        // 3. Configuración de la ventana (el resto del código es igual)
        setTitle("Tienda Online - Iniciar Sesión / Registrar");
        setContentPane(panel_principal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        // Listeners
        btn_ingresar.addActionListener(e -> intentarLogin());
        btn_registrar.addActionListener(e -> intentarRegistro());
    }

    private void intentarLogin() {
        String nombre = txt_usuario.getText();
        String contrasena = new String(txt_contrasena.getPassword());

        try {
            // logica de autenticación
            Cliente cliente = tienda.iniciarSesion(nombre, contrasena);
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + cliente.getNombre() + "!");

            this.dispose();
            new ventana_tienda(tienda).setVisible(true);

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
        SwingUtilities.invokeLater(() -> {
            new ventana_login().setVisible(true);
        });
    }
}