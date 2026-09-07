package Vista;

import Controlador.*;
import java.awt.*;
import javax.swing.*;

/**
 * Ventana principal del sistema. Actúa como contenedor de todos los paneles
 * usando CardLayout para la navegación entre vistas.
 */
public class VentanaPrincipal extends JFrame {

    // ── Constantes de navegación (nombre de cada "card") ──────────────────────
    public static final String CARD_LOGIN      = "LOGIN";
    public static final String CARD_REGISTRO   = "REGISTRO";
    public static final String CARD_CATALOGO   = "CATALOGO";
    public static final String CARD_CARRITO    = "CARRITO";
    public static final String CARD_PAGO       = "PAGO";
    public static final String CARD_COMPROBANTE= "COMPROBANTE";
    public static final String CARD_ADMIN      = "ADMIN";

    // ── Controladores ─────────────────────────────────────────────────────────
    private final ControladorPrincipal ctrlPrincipal;
    private final ControladorProductos ctrlProductos;
    private final ControladorCarrito   ctrlCarrito;
    private final ControladorPedido    ctrlPedido;

    // ── Layout y panel contenedor ─────────────────────────────────────────────
    private final CardLayout cardLayout   = new CardLayout();
    private final JPanel     panelPrincipal = new JPanel(cardLayout);

    // ── Paneles de la aplicación ──────────────────────────────────────────────
    private PanelLogin           panelLogin;
    private PanelRegistro        panelRegistro;
    private PanelCatalogo        panelCatalogo;
    private PanelCarrito         panelCarrito;
    private PanelPago            panelPago;
    private PanelComprobante     panelComprobante;
    private PanelAdminProductos  panelAdmin;

    // ─────────────────────────────────────────────────────────────────────────
    public VentanaPrincipal(ControladorPrincipal ctrlPrincipal,
                            ControladorProductos ctrlProductos,
                            ControladorCarrito   ctrlCarrito,
                            ControladorPedido    ctrlPedido) {
        this.ctrlPrincipal = ctrlPrincipal;
        this.ctrlProductos = ctrlProductos;
        this.ctrlCarrito   = ctrlCarrito;
        this.ctrlPedido    = ctrlPedido;

        configurarVentana();
        inicializarPaneles();
        mostrarPanel(CARD_LOGIN);
    }

    // ── Configuración general del JFrame ──────────────────────────────────────
    private void configurarVentana() {
        setTitle("PCE — Sistema de Comercio Electrónico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setPreferredSize(new Dimension(1280, 800));
        setLocationRelativeTo(null);

        // Paleta de colores oscura global
        UIManager.put("Panel.background",          new Color(18, 18, 30));
        UIManager.put("OptionPane.background",      new Color(18, 18, 30));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);

        getContentPane().setBackground(new Color(18, 18, 30));
        getContentPane().add(panelPrincipal, BorderLayout.CENTER);
    }

    // ── Inicializa y registra todos los paneles ───────────────────────────────
    private void inicializarPaneles() {
        panelLogin       = new PanelLogin(this, ctrlPrincipal);
        panelRegistro    = new PanelRegistro(this, ctrlPrincipal);
        panelCatalogo    = new PanelCatalogo(this, ctrlPrincipal, ctrlProductos, ctrlCarrito);
        panelCarrito     = new PanelCarrito(this, ctrlPrincipal, ctrlCarrito, ctrlPedido);
        panelPago        = new PanelPago(this, ctrlPrincipal, ctrlCarrito, ctrlPedido);
        panelComprobante = new PanelComprobante(this, ctrlPrincipal, ctrlPedido);
        panelAdmin       = new PanelAdminProductos(this, ctrlPrincipal, ctrlProductos);

        panelPrincipal.add(panelLogin,       CARD_LOGIN);
        panelPrincipal.add(panelRegistro,    CARD_REGISTRO);
        panelPrincipal.add(panelCatalogo,    CARD_CATALOGO);
        panelPrincipal.add(panelCarrito,     CARD_CARRITO);
        panelPrincipal.add(panelPago,        CARD_PAGO);
        panelPrincipal.add(panelComprobante, CARD_COMPROBANTE);
        panelPrincipal.add(panelAdmin,       CARD_ADMIN);

        pack();
    }

    /**
     * Navega al panel indicado y lo refresca si implementa Refrescable.
     */
    public void mostrarPanel(String nombreCard) {
        // Notificar al panel destino que va a ser mostrado
        Component[] componentes = panelPrincipal.getComponents();
        for (Component c : componentes) {
            if (c instanceof Refrescable) {
                String name = panelPrincipal.getLayout() instanceof CardLayout
                        ? ((CardLayout) panelPrincipal.getLayout()).toString()
                        : "";
                // Identificamos el panel por nombre de card usando el nombre del componente
            }
        }

        // Refrescar el panel específico según destino
        switch (nombreCard) {
            case CARD_CATALOGO:    panelCatalogo.refrescar();    break;
            case CARD_CARRITO:     panelCarrito.refrescar();     break;
            case CARD_PAGO:        panelPago.refrescar();        break;
            case CARD_COMPROBANTE: panelComprobante.refrescar(); break;
            case CARD_ADMIN:       panelAdmin.refrescar();       break;
            default: break;
        }

        cardLayout.show(panelPrincipal, nombreCard);
    }

    // ── Getters de controladores (para uso interno de los paneles) ────────────
    public ControladorPrincipal getCtrlPrincipal() { return ctrlPrincipal; }
    public ControladorProductos getCtrlProductos() { return ctrlProductos; }
    public ControladorCarrito   getCtrlCarrito()   { return ctrlCarrito; }
    public ControladorPedido    getCtrlPedido()    { return ctrlPedido; }
}
