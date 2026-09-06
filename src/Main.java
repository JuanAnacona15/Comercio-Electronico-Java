import Controlador.*;
import Vista.VentanaPrincipal;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        // Usar FlatLaf o el Look & Feel del sistema si está disponible;
        // de lo contrario usar el predeterminado de Swing.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            // Instanciar controladores
            ControladorPrincipal ctrlPrincipal = new ControladorPrincipal();
            ControladorProductos ctrlProductos = new ControladorProductos(ctrlPrincipal);
            ControladorCarrito   ctrlCarrito   = new ControladorCarrito(ctrlPrincipal);
            ControladorPedido    ctrlPedido    = new ControladorPedido(ctrlPrincipal, ctrlCarrito);

            // Crear y mostrar la ventana principal
            VentanaPrincipal ventana = new VentanaPrincipal(
                ctrlPrincipal, ctrlProductos, ctrlCarrito, ctrlPedido
            );
            ctrlPrincipal.setVentana(ventana);
            ventana.setVisible(true);
        });
    }
}
