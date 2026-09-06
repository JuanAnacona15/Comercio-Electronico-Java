package Vista;

import Controlador.*;
import Modelo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel del carrito de compras.
 * Muestra los ítems seleccionados, totales y acciones para continuar o eliminar.
 */
public class PanelCarrito extends JPanel implements Refrescable {

    private final VentanaPrincipal   ventana;
    private final ControladorPrincipal ctx;
    private final ControladorCarrito ctrlCarrito;
    private final ControladorPedido  ctrlPedido;

    private DefaultTableModel modeloTabla;
    private JTable            tabla;
    private JLabel            lblSubtotal;
    private JLabel            lblImpuesto;
    private JLabel            lblTotal;
    private JLabel            lblVacio;

    public PanelCarrito(VentanaPrincipal ventana, ControladorPrincipal ctx,
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

        JLabel titulo = new JLabel("🛒  Mi Carrito de Compras");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Estilos.TEXTO_PRIMARIO);

        JButton btnVolver = Estilos.botonFantasma("← Seguir comprando");
        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.CARD_CATALOGO));

        barraTop.add(titulo,    BorderLayout.WEST);
        barraTop.add(btnVolver, BorderLayout.EAST);

        // ── Tabla de ítems ────────────────────────────────────────────────────
        String[] columnas = {"Producto", "Tipo", "Precio unitario", "Cantidad", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Estilos.BORDE_SUTIL));
        scroll.getViewport().setBackground(Estilos.FONDO_TARJETA);
        scroll.setBackground(Estilos.FONDO_TARJETA);

        // Mensaje de carrito vacío
        lblVacio = new JLabel("Tu carrito está vacío. ¡Agrega productos desde el catálogo!", SwingConstants.CENTER);
        lblVacio.setFont(Estilos.FUENTE_SUBTITULO);
        lblVacio.setForeground(Estilos.TEXTO_MUTED);

        JPanel panelContenidoCarrito = new JPanel(new CardLayout());
        panelContenidoCarrito.setOpaque(false);
        panelContenidoCarrito.add(scroll,    "TABLA");
        panelContenidoCarrito.add(lblVacio,  "VACIO");
        panelContenidoCarrito.setBorder(new EmptyBorder(16, 24, 0, 24));

        // ── Panel de totales ──────────────────────────────────────────────────
        JPanel panelTotales = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Estilos.FONDO_PANEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Estilos.BORDE_SUTIL);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelTotales.setOpaque(false);
        panelTotales.setBorder(new EmptyBorder(20, 24, 20, 24));
        panelTotales.setLayout(new BoxLayout(panelTotales, BoxLayout.Y_AXIS));

        lblSubtotal = etiquetaTotal("Subtotal:", "$ 0");
        lblImpuesto = etiquetaTotal("IVA (19%):", "$ 0");
        lblTotal    = crearLabelTotal();

        JPanel filaSub = filaTotal("Subtotal:", lblSubtotal);
        JPanel filaIva = filaTotal("IVA (19%):", lblImpuesto);

        JSeparator sep = new JSeparator();
        sep.setForeground(Estilos.BORDE_SUTIL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JPanel filaTotal = new JPanel(new BorderLayout());
        filaTotal.setOpaque(false);
        JLabel lblTotalTxt = new JLabel("TOTAL A PAGAR");
        lblTotalTxt.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTotalTxt.setForeground(Estilos.TEXTO_PRIMARIO);
        filaTotal.add(lblTotalTxt, BorderLayout.WEST);
        filaTotal.add(lblTotal,    BorderLayout.EAST);
        filaTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        panelTotales.add(filaSub);
        panelTotales.add(Box.createVerticalStrut(6));
        panelTotales.add(filaIva);
        panelTotales.add(Box.createVerticalStrut(10));
        panelTotales.add(sep);
        panelTotales.add(Box.createVerticalStrut(10));
        panelTotales.add(filaTotal);

        // ── Botones de acción ─────────────────────────────────────────────────
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botonesPanel.setOpaque(false);
        botonesPanel.setBorder(new EmptyBorder(14, 24, 14, 24));

        JButton btnEliminar = Estilos.botonPeligro("Eliminar ítem");
        JButton btnVaciar   = Estilos.botonFantasma("Vaciar carrito");
        JButton btnPagar    = Estilos.botonSecundario("Proceder al pago →");

        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnVaciar.addActionListener(e -> {
            ctrlCarrito.vaciarCarrito();
            refrescar();
        });
        btnPagar.addActionListener(e -> {
            if (ctrlCarrito.estaVacio()) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ctrlPedido.resetear();
            ctrlPedido.generarPedido();
            ventana.mostrarPanel(VentanaPrincipal.CARD_PAGO);
        });

        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnVaciar);
        botonesPanel.add(btnPagar);

        // ── Sur: totales + botones ─────────────────────────────────────────────
        JPanel sur = new JPanel(new BorderLayout());
        sur.setOpaque(false);
        sur.setBorder(new EmptyBorder(16, 24, 16, 24));
        sur.add(panelTotales, BorderLayout.CENTER);
        sur.add(botonesPanel, BorderLayout.SOUTH);

        add(barraTop,                BorderLayout.NORTH);
        add(panelContenidoCarrito,   BorderLayout.CENTER);
        add(sur,                     BorderLayout.SOUTH);

        // Guardar referencia para mostrar/ocultar tabla vs vacio
        this.putClientProperty("contenidoCarrito", panelContenidoCarrito);
    }

    @Override
    public void refrescar() {
        modeloTabla.setRowCount(0);
        boolean vacio = ctrlCarrito.estaVacio();

        // Mostrar tabla o mensaje vacío
        JPanel cont = (JPanel) getClientProperty("contenidoCarrito");
        if (cont != null) {
            ((CardLayout) cont.getLayout()).show(cont, vacio ? "VACIO" : "TABLA");
        }

        if (!vacio) {
            for (ItemCarrito item : ctrlCarrito.getCarrito().getItems()) {
                Producto p = item.getProducto();
                String tipo = p.getTipoProducto() != null ? p.getTipoProducto() : "—";
                modeloTabla.addRow(new Object[]{
                    p.getNombre(),
                    tipo,
                    Estilos.formatearPrecio(p.getPrecio()),
                    item.getCantidad(),
                    Estilos.formatearPrecio(item.calcularTotalItem())
                });
            }
        }

        lblSubtotal.setText(Estilos.formatearPrecio(ctrlCarrito.calcularSubtotal()));
        lblImpuesto.setText(Estilos.formatearPrecio(ctrlCarrito.calcularImpuesto()));
        lblTotal.setText(Estilos.formatearPrecio(ctrlCarrito.calcularTotal()));
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un ítem para eliminar.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ctrlCarrito.eliminarItem(fila);
        refrescar();
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────
    private void estilizarTabla(JTable t) {
        t.setBackground(Estilos.FONDO_TARJETA);
        t.setForeground(Estilos.TEXTO_PRIMARIO);
        t.setFont(Estilos.FUENTE_CUERPO);
        t.setRowHeight(36);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setBackground(Estilos.FONDO_PANEL);
        t.getTableHeader().setForeground(Estilos.TEXTO_SECUNDARIO);
        t.getTableHeader().setFont(Estilos.FUENTE_BOLD_SM);
        t.setSelectionBackground(new Color(99, 102, 241, 60));
        t.setSelectionForeground(Estilos.TEXTO_PRIMARIO);

        // Centrar columnas numéricas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        centrado.setBackground(Estilos.FONDO_TARJETA);
        centrado.setForeground(Estilos.TEXTO_PRIMARIO);
        for (int i = 2; i < 5; i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
        t.setBorder(null);
    }

    private JLabel etiquetaTotal(String label, String valor) {
        JLabel lbl = new JLabel(valor);
        lbl.setFont(Estilos.FUENTE_CUERPO);
        lbl.setForeground(Estilos.TEXTO_SECUNDARIO);
        return lbl;
    }

    private JLabel crearLabelTotal() {
        JLabel lbl = new JLabel("$ 0");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(Estilos.ACENTO_SECUNDARIO);
        return lbl;
    }

    private JPanel filaTotal(String etiqueta, JLabel lblValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(Estilos.FUENTE_CUERPO);
        lbl.setForeground(Estilos.TEXTO_MUTED);
        fila.add(lbl,      BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        return fila;
    }
}
