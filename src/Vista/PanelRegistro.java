package Vista;

import Controlador.ControladorPrincipal;
import Modelo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Panel de registro de nuevo cliente.
 */
public class PanelRegistro extends JPanel {

    private final VentanaPrincipal     ventana;
    private final ControladorPrincipal ctx;

    private JTextField     campoNombre;
    private JTextField     campoCorreo;
    private JPasswordField campoPassword;
    private JTextField     campoDireccion;
    private JLabel         lblError;

    public PanelRegistro(VentanaPrincipal ventana, ControladorPrincipal ctx) {
        this.ventana = ventana;
        this.ctx     = ctx;
        construirUI();
    }

    private void construirUI() {
        setBackground(Estilos.FONDO_OSCURO);
        setLayout(new GridBagLayout());

        JPanel tarjeta = crearTarjeta();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(40, 50, 40, 50));
        tarjeta.setPreferredSize(new Dimension(460, 560));

        JLabel titulo = Estilos.etiquetaTitulo("Crear cuenta");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = Estilos.etiqueta("Completa tus datos para registrarte");
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoNombre    = Estilos.campoTexto("Tu nombre completo");
        campoCorreo    = Estilos.campoTexto("correo@ejemplo.com");
        campoPassword  = Estilos.campoPassword("Mínimo 1 carácter");
        campoDireccion = Estilos.campoTexto("Cra 10 #20-30, Ciudad");

        for (JTextField c : new JTextField[]{campoNombre, campoCorreo, campoDireccion}) {
            c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        }
        campoPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        lblError = new JLabel(" ");
        lblError.setForeground(Estilos.ACENTO_PELIGRO);
        lblError.setFont(Estilos.FUENTE_PEQUEÑA);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnRegistrar = Estilos.botonPrimario("Crear cuenta");
        JButton btnVolver    = Estilos.botonFantasma("← Volver al login");
        btnRegistrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnVolver.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.CARD_LOGIN));

        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(sub);
        tarjeta.add(Box.createVerticalStrut(26));
        tarjeta.add(lbl("Nombre completo"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoNombre);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lbl("Correo electrónico"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoCorreo);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lbl("Contraseña"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoPassword);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lbl("Dirección de envío"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoDireccion);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lblError);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(btnRegistrar);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(btnVolver);

        add(tarjeta);
    }

    private JLabel lbl(String texto) {
        JLabel l = Estilos.etiqueta(texto);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel crearTarjeta() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Estilos.FONDO_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Estilos.BORDE_SUTIL);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
    }

    private void registrar() {
        String nombre    = campoNombre.getText().trim();
        String correo    = campoCorreo.getText().trim();
        String password  = new String(campoPassword.getPassword());
        String direccion = campoDireccion.getText().trim();
        lblError.setText(" ");

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty() || direccion.isEmpty()) {
            lblError.setText("Todos los campos son obligatorios.");
            return;
        }

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            lblError.setText("El correo no tiene un formato válido.");
            return;
        }

        // Verificar correo duplicado
        for (Cliente c : ctx.getListaClientes()) {
            if (c.getCorreo().equalsIgnoreCase(correo)) {
                lblError.setText("Ya existe una cuenta con ese correo.");
                return;
            }
        }

        // Crear cliente
        Cliente nuevo = new Cliente();
        nuevo.setId("C" + (ctx.getListaClientes().size() + 1));
        nuevo.setNombre(nombre);
        nuevo.setCorreo(correo);
        nuevo.setDireccionEnvio(direccion);
        nuevo.setCarritoDeCompra(new CarritoDeCompra());

        boolean ok = nuevo.registrarse();
        if (!ok) {
            lblError.setText("No se pudo completar el registro. Revisa los datos.");
            return;
        }

        ctx.getListaClientes().add(nuevo);
        JOptionPane.showMessageDialog(this,
            "¡Cuenta creada exitosamente! Ya puedes iniciar sesión.",
            "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
        limpiar();
        ventana.mostrarPanel(VentanaPrincipal.CARD_LOGIN);
    }

    private void limpiar() {
        campoNombre.setText("");
        campoCorreo.setText("");
        campoPassword.setText("");
        campoDireccion.setText("");
        lblError.setText(" ");
    }
}
