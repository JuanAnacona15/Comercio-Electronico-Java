package Vista;

import Controlador.*;
import Modelo.Producto;
import Vista.componentes.TarjetaProducto;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Panel de catálogo de productos.
 * Muestra tarjetas de cada producto disponible en un grid scrollable.
 */
public class PanelCatalogo extends JPanel implements Refrescable {

    private final VentanaPrincipal     ventana;
    private final ControladorPrincipal ctx;
    private final ControladorProductos ctrlProductos;
    private final ControladorCarrito   ctrlCarrito;

    private JPanel panelGrid;
    private JLabel lblBienvenida;
    private JLabel lblContadorCarrito;

    public PanelCatalogo(VentanaPrincipal ventana, ControladorPrincipal ctx,
                         ControladorProductos ctrlProductos, ControladorCarrito ctrlCarrito) {
        this.ventana       = ventana;
        this.ctx           = ctx;
        this.ctrlProductos = ctrlProductos;
        this.ctrlCarrito   = ctrlCarrito;
        construirUI();
    }

    private void construirUI() {
        setBackground(Estilos.FONDO_OSCURO);
        setLayout(new BorderLayout());

        // ── Barra superior ────────────────────────────────────────────────────
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(Estilos.FONDO_PANEL);
        barraTop.setBorder(new EmptyBorder(14, 24, 14, 24));

        // Logo y título
        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        izquierda.setOpaque(false);
        JLabel logo = new JLabel("🛒 PCE Commerce");
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(Estilos.TEXTO_PRIMARIO);
        lblBienvenida = new JLabel();
        lblBienvenida.setFont(Estilos.FUENTE_CUERPO);
        lblBienvenida.setForeground(Estilos.TEXTO_MUTED);
        izquierda.add(logo);
        izquierda.add(new JLabel(" | ") {{ setForeground(Estilos.BORDE_SUTIL); }});
        izquierda.add(lblBienvenida);

        // Acciones derecha
        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        derecha.setOpaque(false);

        lblContadorCarrito = new JLabel("🛒 Carrito (0)");
        lblContadorCarrito.setFont(Estilos.FUENTE_BOLD_SM);
        lblContadorCarrito.setForeground(Estilos.TEXTO_SECUNDARIO);

        JButton btnCarrito  = Estilos.botonPrimario("Ver carrito");
        JButton btnCerrar   = Estilos.botonFantasma("Cerrar sesión");

        btnCarrito.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.CARD_CARRITO));
        btnCerrar.addActionListener(e -> {
            ctx.cerrarSesion();
            ctrlCarrito.vaciarCarrito();
            ventana.mostrarPanel(VentanaPrincipal.CARD_LOGIN);
        });

        derecha.add(lblContadorCarrito);
        derecha.add(btnCarrito);
        derecha.add(btnCerrar);

        barraTop.add(izquierda, BorderLayout.WEST);
        barraTop.add(derecha,   BorderLayout.EAST);

        // ── Subtítulo de sección ──────────────────────────────────────────────
        JPanel seccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        seccion.setBackground(Estilos.FONDO_OSCURO);
        seccion.setBorder(new EmptyBorder(20, 12, 8, 12));

        JLabel lblTituloCatalogo = new JLabel("Nuestros Productos");
        lblTituloCatalogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTituloCatalogo.setForeground(Estilos.TEXTO_PRIMARIO);

        JLabel lblSub = new JLabel("Encuentra lo que necesitas");
        lblSub.setFont(Estilos.FUENTE_CUERPO);
        lblSub.setForeground(Estilos.TEXTO_MUTED);

        JPanel tituloCols = new JPanel();
        tituloCols.setOpaque(false);
        tituloCols.setLayout(new BoxLayout(tituloCols, BoxLayout.Y_AXIS));
        tituloCols.add(lblTituloCatalogo);
        tituloCols.add(lblSub);
        seccion.add(tituloCols);

        // ── Grid de tarjetas ──────────────────────────────────────────────────
        panelGrid = new JPanel(new WrapLayout(FlowLayout.LEFT, 16, 16));
        panelGrid.setBackground(Estilos.FONDO_OSCURO);
        panelGrid.setBorder(new EmptyBorder(0, 16, 24, 16));

        JScrollPane scroll = new JScrollPane(panelGrid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Estilos.FONDO_OSCURO);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ── Ensamblar ─────────────────────────────────────────────────────────
        JPanel norte = new JPanel(new BorderLayout());
        norte.setOpaque(false);
        norte.add(barraTop, BorderLayout.NORTH);
        norte.add(seccion,  BorderLayout.SOUTH);

        add(norte,  BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    @Override
    public void refrescar() {
        // Bienvenida
        if (ctx.getClienteActivo() != null) {
            lblBienvenida.setText("Hola, " + ctx.getClienteActivo().getNombre());
        }

        // Actualizar contador del carrito
        int items = ctrlCarrito.getCarrito().getItems().size();
        lblContadorCarrito.setText("🛒 Carrito (" + items + ")");

        // Reconstruir grid de productos
        panelGrid.removeAll();
        for (Producto p : ctrlProductos.obtenerTodos()) {
            TarjetaProducto tarjeta = new TarjetaProducto(p, ctrlCarrito, this::refrescar);
            panelGrid.add(tarjeta);
        }

        panelGrid.revalidate();
        panelGrid.repaint();
    }

    // ── WrapLayout: layout que hace wrap automático de componentes ────────────
    /**
     * Layout que permite que los componentes se reorganicen en filas
     * cuando no hay suficiente ancho (similar a FlexWrap en CSS).
     */
    private static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0, rowHeight = 0;

                for (Component m : target.getComponents()) {
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            dim.height += rowHeight + vgap;
                            rowWidth = 0;
                            rowHeight = 0;
                        }
                        rowWidth += d.width + hgap;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                dim.height += rowHeight + insets.top + insets.bottom + vgap * 2;
                dim.width = targetWidth;
                return dim;
            }
        }
    }
}
