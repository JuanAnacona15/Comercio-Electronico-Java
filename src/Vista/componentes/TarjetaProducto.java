package Vista.componentes;

import Controlador.ControladorCarrito;
import Modelo.*;
import Vista.Estilos;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Componente visual de tarjeta de producto.
 * Muestra la información del producto con badge de tipo (FÍSICO/DIGITAL)
 * y estado, e incluye controles para agregar al carrito.
 */
public class TarjetaProducto extends JPanel {

    private final Producto          producto;
    private final ControladorCarrito ctrlCarrito;
    private final Runnable          onAgregado; // Callback para refrescar el catálogo

    private JLabel lblStock;

    public TarjetaProducto(Producto producto, ControladorCarrito ctrlCarrito, Runnable onAgregado) {
        this.producto    = producto;
        this.ctrlCarrito = ctrlCarrito;
        this.onAgregado  = onAgregado;
        construirUI();
    }

    private void construirUI() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(new Dimension(240, 310));
        setBorder(new EmptyBorder(4, 4, 4, 4));

        // Fondo de la tarjeta
        JPanel fondo = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradiente sutil según tipo
                Color c1 = Producto.TIPO_DIGITAL.equals(producto.getTipoProducto())
                        ? new Color(40, 28, 70) : new Color(25, 35, 60);
                Color c2 = Estilos.FONDO_TARJETA;
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(Estilos.BORDE_SUTIL);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        fondo.setOpaque(false);
        fondo.setBorder(new EmptyBorder(14, 14, 14, 14));

        // ── Sección superior: emoji + badges ─────────────────────────────────
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        // Emoji de ícono según tipo
        String emoji = Producto.TIPO_DIGITAL.equals(producto.getTipoProducto()) ? "💻" : "📦";
        JLabel lblEmoji = new JLabel(emoji, SwingConstants.LEFT);
        lblEmoji.setFont(new Font("SansSerif", Font.PLAIN, 30));
        topRow.add(lblEmoji, BorderLayout.WEST);

        // Badges en la esquina derecha
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        badges.setOpaque(false);
        badges.add(crearBadgeTipo());
        if (Producto.ESTADO_AGOTADO.equals(producto.getEstado())) {
            badges.add(crearBadgeAgotado());
        }
        topRow.add(badges, BorderLayout.EAST);

        // ── Nombre ────────────────────────────────────────────────────────────
        JLabel lblNombre = new JLabel("<html><b>" + producto.getNombre() + "</b></html>");
        lblNombre.setFont(Estilos.FUENTE_SUBTITULO);
        lblNombre.setForeground(Estilos.TEXTO_PRIMARIO);

        // ── Descripción ───────────────────────────────────────────────────────
        JLabel lblDesc = new JLabel("<html><font color='#94A3B8'>" + producto.getDescripcion() + "</font></html>");
        lblDesc.setFont(Estilos.FUENTE_PEQUEÑA);
        lblDesc.setVerticalAlignment(SwingConstants.TOP);

        // ── Precio ────────────────────────────────────────────────────────────
        JLabel lblPrecio = new JLabel(Estilos.formatearPrecio(producto.getPrecio()));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPrecio.setForeground(Estilos.ACENTO_PRIMARIO);

        // ── Stock ─────────────────────────────────────────────────────────────
        lblStock = new JLabel();
        actualizarStock();
        lblStock.setFont(Estilos.FUENTE_PEQUEÑA);

        // ── Panel de acción (spinner + botón) ─────────────────────────────────
        JPanel accion = new JPanel(new BorderLayout(6, 0));
        accion.setOpaque(false);

        boolean disponible = Producto.ESTADO_DISPONIBLE.equals(producto.getEstado())
                && producto.getCantidadDisponible() > 0;

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1,
                disponible ? producto.getCantidadDisponible() : 1, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(60, 32));
        spinner.setFont(Estilos.FUENTE_CUERPO);
        spinner.setBackground(Estilos.FONDO_INPUT);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setBackground(Estilos.FONDO_INPUT);
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setForeground(Estilos.TEXTO_PRIMARIO);
        spinner.setEnabled(disponible);

        JButton btnAgregar = Estilos.botonPrimario(disponible ? "Agregar" : "Agotado");
        btnAgregar.setEnabled(disponible);
        if (!disponible) {
            btnAgregar.setForeground(Estilos.TEXTO_MUTED);
        }
        btnAgregar.setFont(Estilos.FUENTE_BOLD_SM);

        btnAgregar.addActionListener(e -> {
            int cantidad = (int) spinner.getValue();
            boolean ok = ctrlCarrito.agregarProducto(producto, cantidad);
            if (ok) {
                actualizarStock();
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "\"" + producto.getNombre() + "\" agregado al carrito.",
                    "Carrito", JOptionPane.INFORMATION_MESSAGE
                );
                if (onAgregado != null) onAgregado.run();
            } else {
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "No hay suficiente stock disponible.",
                    "Sin stock", JOptionPane.WARNING_MESSAGE
                );
            }
        });

        accion.add(spinner,    BorderLayout.WEST);
        accion.add(btnAgregar, BorderLayout.CENTER);

        // Ensamblar fondo
        fondo.add(topRow,    BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(lblNombre);
        centro.add(Box.createVerticalStrut(4));
        centro.add(lblDesc);
        centro.add(Box.createVerticalStrut(8));
        centro.add(lblPrecio);
        centro.add(Box.createVerticalStrut(2));
        centro.add(lblStock);
        centro.add(Box.createVerticalStrut(10));
        centro.add(accion);

        fondo.add(centro, BorderLayout.CENTER);
        add(fondo, BorderLayout.CENTER);
    }

    private void actualizarStock() {
        int stock = producto.getCantidadDisponible();
        if (stock == 0) {
            lblStock.setText("Sin stock");
            lblStock.setForeground(Estilos.ACENTO_PELIGRO);
        } else if (stock <= 5) {
            lblStock.setText("¡Últimas " + stock + " unidades!");
            lblStock.setForeground(Estilos.ACENTO_ADVERTENCIA);
        } else {
            lblStock.setText("Stock: " + stock + " unidades");
            lblStock.setForeground(Estilos.TEXTO_MUTED);
        }
    }

    /** Badge pequeño para el tipo de producto. */
    private JLabel crearBadgeTipo() {
        boolean esDigital = Producto.TIPO_DIGITAL.equals(producto.getTipoProducto());
        String texto = esDigital ? "DIGITAL" : "FÍSICO";
        Color bg = esDigital ? Estilos.BADGE_DIGITAL_BG : Estilos.BADGE_FISICO_BG;
        Color fg = esDigital ? Estilos.BADGE_DIGITAL_FG : Estilos.BADGE_FISICO_FG;
        return crearBadge(texto, bg, fg);
    }

    private JLabel crearBadgeAgotado() {
        return crearBadge("AGOTADO", Estilos.BADGE_AGOTADO_BG, Estilos.BADGE_AGOTADO_FG);
    }

    private JLabel crearBadge(String texto, Color bg, Color fg) {
        JLabel badge = new JLabel(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("SansSerif", Font.BOLD, 9));
        badge.setForeground(fg);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(2, 6, 2, 6));
        return badge;
    }
}
