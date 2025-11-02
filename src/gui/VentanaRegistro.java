package gui;

import modelo.Cliente;
import servicios.GestorPersistencia;
import javax.swing.*;
import java.awt.event.*;

public class VentanaRegistro extends JFrame {
    private JTextField txtNombre, txtId;
    private JButton btnGuardar;

    public VentanaRegistro() {
        setTitle("Registrar Cliente");
        setSize(300, 200);
        setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 80, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(100, 20, 150, 25);
        add(txtNombre);

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 60, 80, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 60, 150, 25);
        add(txtId);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(80, 110, 120, 30);
        add(btnGuardar);

        btnGuardar.addActionListener(e -> registrarCliente());
    }

    private void registrarCliente() {
        String nombre = txtNombre.getText();
        int id = Integer.parseInt(txtId.getText());
        Cliente c = new Cliente(nombre, id, "1234"); // Ejemplo

        GestorPersistencia.guardarCliente(c);
        JOptionPane.showMessageDialog(this, "Cliente guardado con éxito");
    }
}
