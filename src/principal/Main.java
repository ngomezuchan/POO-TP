package principal;

import modelo.Tienda;
import vistas.ventana_login;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tienda tienda = new Tienda();
            tienda.cargarDatosDemo();

            ventana_login login = new ventana_login(tienda);
            login.setVisible(true);
        });
    }
}
