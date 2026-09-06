package Vista;

import Controlador.ControladorPrincipal;
import Modelo.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Panel de inicio de sesión.
 * Permite acceder como Cliente o Administrador.
 */
public class PanelLogin extends JPanel {

    private final VentanaPrincipal     ventana;
    private final ControladorPrincipal ctx;

    private JTextField     campoCorreo;
    private JPasswordField campoPassword;
    private JComboBox<String> comboRol;
    private JLabel         lblError;

    public PanelLogin(VentanaPrincipal ventana, ControladorPrincipal ctx) {
        this.ventana = ventana;
        this.ctx     = ctx;
        construirUI();
    }

    private void construirUI() {
        setBackground(Estilos.FONDO_OSCURO);
        setLayout(new GridBagLayout());

        // ── Tarjeta central ───────────────────────────────────────────────────
        JPanel tarjeta = new JPanel() {
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
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(40, 50, 40, 50));
        tarjeta.setPreferredSize(new Dimension(420, 520));

        // Logo / título
        JLabel icono = new JLabel("🛒", SwingConstants.CENTER);
        icono.setFont(new Font("SansSerif", Font.PLAIN, 48));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = Estilos.etiquetaTitulo("PCE Commerce");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitulo = Estilos.etiqueta("Inicia sesión para continuar");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Selector de rol
        comboRol = new JComboBox<>(new String[]{"Cliente", "Administrador"});
        comboRol.setFont(Estilos.FUENTE_CUERPO);
        comboRol.setBackground(Estilos.FONDO_INPUT);
        comboRol.setForeground(Estilos.TEXTO_PRIMARIO);
        comboRol.setBorder(Estilos.bordeInput());
        comboRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Campos
        campoCorreo   = Estilos.campoTexto("correo@ejemplo.com");
        campoPassword = Estilos.campoPassword("Contraseña");
        campoCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        campoPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        // Error
        lblError = new JLabel(" ");
        lblError.setForeground(Estilos.ACENTO_PELIGRO);
        lblError.setFont(Estilos.FUENTE_PEQUEÑA);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botones
        JButton btnIngresar  = Estilos.botonPrimario("Ingresar");
        JButton btnRegistrarse = Estilos.botonFantasma("¿No tienes cuenta? Regístrate");
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnRegistrarse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        btnIngresar.addActionListener(e -> iniciarSesion());
        btnRegistrarse.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.CARD_REGISTRO));

        // Hint de credenciales de prueba
        JLabel lblHint = new JLabel("<html><center><font color='#64748B'>Admin demo: admin@pce.com / admin1234<br>" +
                "Cliente demo: juan@example.com / cualquier texto</font></center></html>");
        lblHint.setFont(Estilos.FUENTE_PEQUEÑA);
        lblHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ensamblar
        tarjeta.add(icono);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(titulo);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(subtitulo);
        tarjeta.add(Box.createVerticalStrut(28));
        tarjeta.add(etiquetaCampo("Rol"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(comboRol);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(etiquetaCampo("Correo electrónico"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoCorreo);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(etiquetaCampo("Contraseña"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoPassword);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblError);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(btnIngresar);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(btnRegistrarse);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(lblHint);

        add(tarjeta);
    }

    private JLabel etiquetaCampo(String texto) {
        JLabel lbl = Estilos.etiqueta(texto);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void iniciarSesion() {
        String correo   = campoCorreo.getText().trim();
        String password = new String(campoPassword.getPassword());
        String rol      = (String) comboRol.getSelectedItem();
        lblError.setText(" ");

        if (correo.isEmpty() || password.isEmpty()) {
            lblError.setText("Por favor completa todos los campos.");
            return;
        }

        if ("Administrador".equals(rol)) {
            for (Administrador admin : ctx.getListaAdmins()) {
                if (admin.getCorreo().equalsIgnoreCase(correo)) {
                    if (admin.iniciarSesion(password)) {
                        ctx.setAdminActivo(admin);
                        limpiar();
                        ventana.mostrarPanel(VentanaPrincipal.CARD_ADMIN);
                        return;
                    }
                }
            }
            lblError.setText("Credenciales de administrador inválidas.");
        } else {
            for (Cliente cliente : ctx.getListaClientes()) {
                if (cliente.getCorreo().equalsIgnoreCase(correo)) {
                    if (cliente.iniciarSesion(password)) {
                        ctx.setClienteActivo(cliente);
                        // Asegurar que tiene carrito
                        if (cliente.getCarritoDeCompra() == null) {
                            cliente.setCarritoDeCompra(new CarritoDeCompra());
                        }
                        limpiar();
                        ventana.mostrarPanel(VentanaPrincipal.CARD_CATALOGO);
                        return;
                    }
                }
            }
            lblError.setText("Correo o contraseña incorrectos.");
        }
    }

    private void limpiar() {
        campoCorreo.setText("");
        campoPassword.setText("");
        lblError.setText(" ");
    }
}
