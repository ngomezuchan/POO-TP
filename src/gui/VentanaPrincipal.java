package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaPrincipal extends JFrame {
    private JButton btnRegistrar, btnVerProductos, btnCarrito, btnSalir;

    public VentanaPrincipal() {
        setTitle("Tienda de Indumentaria");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        btnRegistrar = new JButton("Registrar Cliente");
        btnVerProductos = new JButton("Ver Productos");
        btnCarrito = new JButton("Carrito de Compras");
        btnSalir = new JButton("Salir");

        add(btnRegistrar);
        add(btnVerProductos);
        add(btnCarrito);
        add(btnSalir);

        // Ejemplo de manejo de evento
        btnSalir.addActionListener(e -> System.exit(0));
    }
}
