package vistas;

import modelo.Tienda;
import javax.swing.*;
import java.awt.*;

public class ventana_login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPass;
    private JButton btnLogin, btnRegistrar;
    private final Tienda tienda;

    public ventana_login(Tienda tienda) {
        this.tienda = tienda;
        setTitle("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 220);
        setLocationRelativeTo(null);
        inicializar();
    }

    private void inicializar() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblPass = new JLabel("Contraseña:");
        txtUsuario = new JTextField();
        txtPass = new JPasswordField();
        btnLogin = new JButton("Iniciar sesión");
        btnRegistrar = new JButton("Registrarse");

        panel.add(lblUsuario);
        panel.add(txtUsuario);
        panel.add(lblPass);
        panel.add(txtPass);
        panel.add(btnLogin);
        panel.add(btnRegistrar);

        add(panel);

        btnLogin.addActionListener(e -> login());
        btnRegistrar.addActionListener(e -> registrar());
    }

    private void login() {
        String nombre = txtUsuario.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (tienda.iniciarSesion(nombre, pass)) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + nombre);
            new ventana_tienda(tienda).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }
    }

    private void registrar() {
        String nombre = txtUsuario.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (nombre.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        if (!tienda.registrarCliente(nombre, nombre + "@mail.com", pass)) {
            JOptionPane.showMessageDialog(this, "El usuario ya existe");
        } else {
            JOptionPane.showMessageDialog(this, "Registro exitoso, ahora puede iniciar sesión");
        }
    }
}
