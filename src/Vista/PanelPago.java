package Vista;

import Controlador.*;
import Modelo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Panel de pago. Permite elegir método (Tarjeta de Crédito o Cuenta Digital),
 * ingresar los datos del pago y ejecutar la transacción.
 */
public class PanelPago extends JPanel implements Refrescable {

    private final VentanaPrincipal   ventana;
    private final ControladorPrincipal ctx;
    private final ControladorCarrito ctrlCarrito;
    private final ControladorPedido  ctrlPedido;

    // Resumen del pedido
    private JLabel lblPedidoId;
    private JLabel lblFecha;
    private JLabel lblResSubtotal;
    private JLabel lblResIva;
    private JLabel lblResTotal;

    // Método de pago
    private JRadioButton rbTarjeta;
    private JRadioButton rbCuentaDigital;
    private JPanel       panelDatosPago;
    private CardLayout   cardDatos;

    // Tarjeta
    private JTextField campoNumTarjeta;
    // Cuenta digital
    private JTextField campoCorreoCuenta;

    public PanelPago(VentanaPrincipal ventana, ControladorPrincipal ctx,
                     ControladorCarrito ctrlCarrito, ControladorPedido ctrlPedido) {
        this.ventana     = ventana;
        this.ctx         = ctx;
        this.ctrlCarrito = ctrlCarrito;
        this.ctrlPedido  = ctrlPedido;
        construirUI();
    }

    private void construirUI() {
        setBackground(Estilos.FONDO_OSCURO);
        setLayout(new BorderLayout());

        // ── Barra superior ────────────────────────────────────────────────────
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(Estilos.FONDO_PANEL);
        barraTop.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel titulo = new JLabel("💳  Proceso de Pago");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Estilos.TEXTO_PRIMARIO);

        JButton btnVolver = Estilos.botonFantasma("← Volver al carrito");
        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.CARD_CARRITO));

        barraTop.add(titulo,    BorderLayout.WEST);
        barraTop.add(btnVolver, BorderLayout.EAST);

        // ── Contenido central ─────────────────────────────────────────────────
        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBackground(Estilos.FONDO_OSCURO);
        contenido.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.weightx = 0.45;
        gbc.weighty = 1;
        gbc.gridx = 0;

        // Resumen del pedido (izquierda)
        contenido.add(construirResumenPedido(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.55;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Formulario de pago (derecha)
        contenido.add(construirFormularioPago(), gbc);

        add(barraTop, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
    }

    private JPanel construirResumenPedido() {
        JPanel panel = Estilos.panelTarjeta();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel tit = new JLabel("📋  Resumen del Pedido");
        tit.setFont(Estilos.FUENTE_SUBTITULO);
        tit.setForeground(Estilos.TEXTO_PRIMARIO);
        tit.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPedidoId   = valorLabel("—");
        lblFecha      = valorLabel("—");
        lblResSubtotal = valorLabel("—");
        lblResIva     = valorLabel("—");
        lblResTotal   = new JLabel("—");
        lblResTotal.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblResTotal.setForeground(Estilos.ACENTO_SECUNDARIO);
        lblResTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(tit);
        panel.add(Box.createVerticalStrut(20));
        panel.add(filaResumen("N° Pedido:",   lblPedidoId));
        panel.add(Box.createVerticalStrut(8));
        panel.add(filaResumen("Fecha:",       lblFecha));
        panel.add(Box.createVerticalStrut(20));
        panel.add(separador());
        panel.add(Box.createVerticalStrut(12));
        panel.add(filaResumen("Subtotal:",    lblResSubtotal));
        panel.add(Box.createVerticalStrut(6));
        panel.add(filaResumen("IVA (19%):",   lblResIva));
        panel.add(Box.createVerticalStrut(10));
        panel.add(separador());
        panel.add(Box.createVerticalStrut(10));

        JPanel filaTotal = new JPanel(new BorderLayout());
        filaTotal.setOpaque(false);
        filaTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel lblTotalTxt = new JLabel("TOTAL:");
        lblTotalTxt.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTotalTxt.setForeground(Estilos.TEXTO_PRIMARIO);
        filaTotal.add(lblTotalTxt, BorderLayout.WEST);
        filaTotal.add(lblResTotal, BorderLayout.EAST);
        panel.add(filaTotal);

        return panel;
    }

    private JPanel construirFormularioPago() {
        JPanel panel = Estilos.panelTarjeta();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel tit = new JLabel("💳  Método de Pago");
        tit.setFont(Estilos.FUENTE_SUBTITULO);
        tit.setForeground(Estilos.TEXTO_PRIMARIO);
        tit.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Radio buttons
        rbTarjeta      = crearRadio("Tarjeta de Crédito");
        rbCuentaDigital = crearRadio("Cuenta Digital");
        rbTarjeta.setSelected(true);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbTarjeta);
        grupo.add(rbCuentaDigital);

        // Panel de datos (CardLayout)
        cardDatos     = new CardLayout();
        panelDatosPago = new JPanel(cardDatos);
        panelDatosPago.setOpaque(false);
        panelDatosPago.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Panel tarjeta
        JPanel pTarjeta = new JPanel();
        pTarjeta.setOpaque(false);
        pTarjeta.setLayout(new BoxLayout(pTarjeta, BoxLayout.Y_AXIS));
        pTarjeta.add(lbl("Número de tarjeta"));
        pTarjeta.add(Box.createVerticalStrut(4));
        campoNumTarjeta = Estilos.campoTexto("4111 1111 1111 1111");
        campoNumTarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        pTarjeta.add(campoNumTarjeta);

        // Panel cuenta digital
        JPanel pCuenta = new JPanel();
        pCuenta.setOpaque(false);
        pCuenta.setLayout(new BoxLayout(pCuenta, BoxLayout.Y_AXIS));
        pCuenta.add(lbl("Correo de la cuenta digital"));
        pCuenta.add(Box.createVerticalStrut(4));
        campoCorreoCuenta = Estilos.campoTexto("cuenta@digital.com");
        campoCorreoCuenta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        pCuenta.add(campoCorreoCuenta);

        panelDatosPago.add(pTarjeta, "TARJETA");
        panelDatosPago.add(pCuenta,  "CUENTA");

        rbTarjeta.addActionListener(e       -> cardDatos.show(panelDatosPago, "TARJETA"));
        rbCuentaDigital.addActionListener(e -> cardDatos.show(panelDatosPago, "CUENTA"));

        // Botón pagar
        JButton btnPagar = Estilos.botonSecundario("✓  Confirmar y Pagar");
        btnPagar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnPagar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPagar.addActionListener(e -> procesarPago());

        panel.add(tit);
        panel.add(Box.createVerticalStrut(20));
        panel.add(rbTarjeta);
        panel.add(Box.createVerticalStrut(8));
        panel.add(rbCuentaDigital);
        panel.add(Box.createVerticalStrut(20));
        panel.add(panelDatosPago);
        panel.add(Box.createVerticalStrut(24));
        panel.add(separador());
        panel.add(Box.createVerticalStrut(16));
        panel.add(btnPagar);

        return panel;
    }

    private void procesarPago() {
        Comprobante comprobante;

        if (rbTarjeta.isSelected()) {
            String num = campoNumTarjeta.getText().trim();
            if (num.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingresa el número de tarjeta.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            comprobante = ctrlPedido.procesarConTarjeta(num);
        } else {
            String correo = campoCorreoCuenta.getText().trim();
            if (correo.isEmpty() || !correo.contains("@")) {
                JOptionPane.showMessageDialog(this, "Ingresa un correo válido de cuenta digital.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            comprobante = ctrlPedido.procesarConCuentaDigital(correo);
        }

        limpiarCampos();
        ventana.mostrarPanel(VentanaPrincipal.CARD_COMPROBANTE);
    }

    @Override
    public void refrescar() {
        Pedido pedido = ctrlPedido.getPedidoActual();
        if (pedido == null) return;

        lblPedidoId.setText(pedido.getIdentificador());
        lblFecha.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedido.getFecha()));
        lblResSubtotal.setText(Estilos.formatearPrecio(pedido.getSubtotal()));
        lblResIva.setText(Estilos.formatearPrecio(pedido.getImpuesto19()));
        lblResTotal.setText(Estilos.formatearPrecio(pedido.getValorTotal()));

        // Reset método de pago
        rbTarjeta.setSelected(true);
        cardDatos.show(panelDatosPago, "TARJETA");
    }

    private void limpiarCampos() {
        campoNumTarjeta.setText("");
        campoCorreoCuenta.setText("");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel valorLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Estilos.FUENTE_CUERPO);
        lbl.setForeground(Estilos.TEXTO_PRIMARIO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel filaResumen(String etiqueta, JLabel lblValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(Estilos.FUENTE_CUERPO);
        lbl.setForeground(Estilos.TEXTO_MUTED);
        fila.add(lbl,      BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        return fila;
    }

    private JRadioButton crearRadio(String texto) {
        JRadioButton rb = new JRadioButton(texto);
        rb.setFont(Estilos.FUENTE_CUERPO);
        rb.setForeground(Estilos.TEXTO_PRIMARIO);
        rb.setBackground(Color.TRANSLUCENT == 0 ? new Color(0,0,0,0) : Estilos.FONDO_TARJETA);
        rb.setOpaque(false);
        rb.setAlignmentX(Component.LEFT_ALIGNMENT);
        rb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return rb;
    }

    private JLabel lbl(String texto) {
        JLabel l = Estilos.etiqueta(texto);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private Component separador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(Estilos.BORDE_SUTIL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
}
