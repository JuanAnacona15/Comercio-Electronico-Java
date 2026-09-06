package Vista;

import Controlador.*;
import Modelo.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Panel de comprobante de pago.
 * Muestra el resultado de la transacción y los detalles del pedido.
 */
public class PanelComprobante extends JPanel implements Refrescable {

    private final VentanaPrincipal   ventana;
    private final ControladorPrincipal ctx;
    private final ControladorPedido  ctrlPedido;

    private JLabel lblIconoEstado;
    private JLabel lblTituloEstado;
    private JLabel lblSubtituloEstado;
    private JLabel lblNumeroUnico;
    private JLabel lblFechaComp;
    private JLabel lblEstadoComp;
    private JLabel lblPedidoRef;
    private JLabel lblTotalPagado;

    public PanelComprobante(VentanaPrincipal ventana, ControladorPrincipal ctx,
                            ControladorPedido ctrlPedido) {
        this.ventana    = ventana;
        this.ctx        = ctx;
        this.ctrlPedido = ctrlPedido;
        construirUI();
    }

    private void construirUI() {
        setBackground(Estilos.FONDO_OSCURO);
        setLayout(new GridBagLayout());

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
        tarjeta.setBorder(new EmptyBorder(40, 60, 40, 60));
        tarjeta.setPreferredSize(new Dimension(520, 580));

        // ── Ícono de estado ───────────────────────────────────────────────────
        lblIconoEstado = new JLabel("✅", SwingConstants.CENTER);
        lblIconoEstado.setFont(new Font("SansSerif", Font.PLAIN, 56));
        lblIconoEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTituloEstado = new JLabel("¡Pago Aprobado!", SwingConstants.CENTER);
        lblTituloEstado.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTituloEstado.setForeground(Estilos.ACENTO_SECUNDARIO);
        lblTituloEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSubtituloEstado = new JLabel("Tu pedido ha sido procesado exitosamente", SwingConstants.CENTER);
        lblSubtituloEstado.setFont(Estilos.FUENTE_CUERPO);
        lblSubtituloEstado.setForeground(Estilos.TEXTO_MUTED);
        lblSubtituloEstado.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Separador ─────────────────────────────────────────────────────────
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(Estilos.BORDE_SUTIL);
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // ── Detalles del comprobante ──────────────────────────────────────────
        lblNumeroUnico  = crearValor("—");
        lblFechaComp    = crearValor("—");
        lblEstadoComp   = crearValor("—");
        lblPedidoRef    = crearValor("—");
        lblTotalPagado  = new JLabel("—");
        lblTotalPagado.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTotalPagado.setForeground(Estilos.ACENTO_PRIMARIO);
        lblTotalPagado.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(Estilos.BORDE_SUTIL);
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // ── Botones ───────────────────────────────────────────────────────────
        JButton btnCatalogo  = Estilos.botonPrimario("🛒  Seguir comprando");
        JButton btnHistorial = Estilos.botonFantasma("📋  Ver mis pedidos");
        btnCatalogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnHistorial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        btnCatalogo.addActionListener(e -> {
            ctrlPedido.resetear();
            ventana.mostrarPanel(VentanaPrincipal.CARD_CATALOGO);
        });

        btnHistorial.addActionListener(e -> mostrarHistorial());

        // ── Ensamblar ─────────────────────────────────────────────────────────
        tarjeta.add(lblIconoEstado);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(lblTituloEstado);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblSubtituloEstado);
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(sep1);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(filaDetalle("N° Comprobante:", lblNumeroUnico));
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(filaDetalle("Fecha:", lblFechaComp));
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(filaDetalle("Estado:", lblEstadoComp));
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(filaDetalle("Referencia pedido:", lblPedidoRef));
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(sep2);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(filaDetalle("Total pagado:", lblTotalPagado));
        tarjeta.add(Box.createVerticalStrut(30));
        tarjeta.add(btnCatalogo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(btnHistorial);

        add(tarjeta);
    }

    @Override
    public void refrescar() {
        Comprobante comp   = ctrlPedido.getComprobanteActual();
        Pedido      pedido = ctrlPedido.getPedidoActual();

        if (comp == null) return;

        boolean aprobado = Comprobante.ESTADO_APROBADO.equals(comp.getEstado());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        lblIconoEstado.setText(aprobado ? "✅" : "❌");

        lblTituloEstado.setText(aprobado ? "¡Pago Aprobado!" : "Pago Rechazado");
        lblTituloEstado.setForeground(aprobado ? Estilos.ACENTO_SECUNDARIO : Estilos.ACENTO_PELIGRO);

        lblSubtituloEstado.setText(aprobado
                ? "Tu pedido ha sido procesado exitosamente"
                : "No se pudo completar la transacción. Intenta de nuevo.");

        lblNumeroUnico.setText(comp.getNumeroUnico());
        lblFechaComp.setText(comp.getFecha() != null ? sdf.format(comp.getFecha()) : "—");

        lblEstadoComp.setText(comp.getEstado());
        lblEstadoComp.setForeground(aprobado ? Estilos.ACENTO_SECUNDARIO : Estilos.ACENTO_PELIGRO);

        lblPedidoRef.setText(pedido != null ? pedido.getIdentificador() : "—");
        lblTotalPagado.setText(pedido != null ? Estilos.formatearPrecio(pedido.getValorTotal()) : "—");
    }

    private void mostrarHistorial() {
        Cliente cliente = ctx.getClienteActivo();
        if (cliente == null || cliente.getLista_pedidos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes pedidos registrados todavía.",
                    "Historial", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("<html><body style='font-family:monospace;'>");
        sb.append("<h3>📋 Historial de pedidos</h3>");
        for (Pedido p : cliente.getLista_pedidos()) {
            String fecha = p.getFecha() != null
                    ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(p.getFecha()) : "—";
            sb.append("<hr>")
              .append("<b>").append(p.getIdentificador()).append("</b><br>")
              .append("Fecha: ").append(fecha).append("<br>")
              .append("Total: ").append(Estilos.formatearPrecio(p.getValorTotal())).append("<br>");
        }
        sb.append("</body></html>");

        JLabel contenido = new JLabel(sb.toString());
        JScrollPane scrollPane = new JScrollPane(contenido);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Mis Pedidos", JOptionPane.PLAIN_MESSAGE);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel crearValor(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Estilos.FUENTE_CUERPO);
        lbl.setForeground(Estilos.TEXTO_PRIMARIO);
        lbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return lbl;
    }

    private JPanel filaDetalle(String etiqueta, JLabel lblValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(Estilos.FUENTE_CUERPO);
        lbl.setForeground(Estilos.TEXTO_MUTED);
        fila.add(lbl,      BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        return fila;
    }
}
