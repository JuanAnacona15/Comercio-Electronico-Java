package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Utilidades de diseño centralizadas.
 * Define la paleta de colores, tipografías y métodos fábrica de componentes estilizados.
 */
public final class Estilos {

    private Estilos() {}

    // ── Paleta de colores ─────────────────────────────────────────────────────
    public static final Color FONDO_OSCURO       = new Color(12, 12, 22);
    public static final Color FONDO_PANEL        = new Color(20, 20, 38);
    public static final Color FONDO_TARJETA      = new Color(28, 28, 50);
    public static final Color FONDO_INPUT        = new Color(35, 35, 60);
    public static final Color BORDE_SUTIL        = new Color(60, 60, 90);
    public static final Color ACENTO_PRIMARIO    = new Color(99, 102, 241);  // Indigo
    public static final Color ACENTO_HOVER       = new Color(129, 132, 255);
    public static final Color ACENTO_SECUNDARIO  = new Color(16, 185, 129);  // Emerald
    public static final Color ACENTO_PELIGRO     = new Color(239, 68, 68);   // Red
    public static final Color ACENTO_ADVERTENCIA = new Color(245, 158, 11);  // Amber
    public static final Color TEXTO_PRIMARIO     = new Color(240, 240, 255);
    public static final Color TEXTO_SECUNDARIO   = new Color(148, 163, 184);
    public static final Color TEXTO_MUTED        = new Color(100, 116, 139);
    public static final Color BADGE_FISICO_BG    = new Color(30, 64, 175, 180);
    public static final Color BADGE_FISICO_FG    = new Color(147, 197, 253);
    public static final Color BADGE_DIGITAL_BG   = new Color(109, 40, 217, 180);
    public static final Color BADGE_DIGITAL_FG   = new Color(196, 181, 253);
    public static final Color BADGE_AGOTADO_BG   = new Color(127, 29, 29, 180);
    public static final Color BADGE_AGOTADO_FG   = new Color(252, 165, 165);

    // ── Tipografías ───────────────────────────────────────────────────────────
    public static final Font FUENTE_TITULO   = new Font("SansSerif", Font.BOLD,  26);
    public static final Font FUENTE_SUBTITULO= new Font("SansSerif", Font.BOLD,  16);
    public static final Font FUENTE_CUERPO   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FUENTE_PEQUEÑA  = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FUENTE_BOLD_SM  = new Font("SansSerif", Font.BOLD,  12);
    public static final Font FUENTE_MONO     = new Font("Monospaced", Font.PLAIN, 12);

    // ── Bordes ────────────────────────────────────────────────────────────────
    public static Border bordeInput() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDE_SUTIL, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        );
    }

    public static Border bordeTarjeta() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDE_SUTIL, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        );
    }

    // ── Fábrica de componentes ────────────────────────────────────────────────

    /** Botón primario (indigo). */
    public static JButton botonPrimario(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed()  ? ACENTO_PRIMARIO.darker()
                           : getModel().isRollover() ? ACENTO_HOVER
                           : ACENTO_PRIMARIO;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        estilizarBoton(btn, TEXTO_PRIMARIO);
        return btn;
    }

    /** Botón secundario (verde esmeralda). */
    public static JButton botonSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed()  ? ACENTO_SECUNDARIO.darker()
                           : getModel().isRollover() ? ACENTO_SECUNDARIO.brighter()
                           : ACENTO_SECUNDARIO;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        estilizarBoton(btn, Color.WHITE);
        return btn;
    }

    /** Botón de peligro (rojo). */
    public static JButton botonPeligro(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed()  ? ACENTO_PELIGRO.darker()
                           : getModel().isRollover() ? ACENTO_PELIGRO.brighter()
                           : ACENTO_PELIGRO;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        estilizarBoton(btn, Color.WHITE);
        return btn;
    }

    /** Botón fantasma (borde sutil, sin relleno). */
    public static JButton botonFantasma(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 15));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.setColor(BORDE_SUTIL);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        estilizarBoton(btn, TEXTO_SECUNDARIO);
        return btn;
    }

    private static void estilizarBoton(JButton btn, Color fgColor) {
        btn.setFont(FUENTE_BOLD_SM);
        btn.setForeground(fgColor);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(8, 18, 8, 18));
    }

    /** Campo de texto con estilo oscuro. */
    public static JTextField campoTexto(String placeholder) {
        JTextField campo = new JTextField(20);
        campo.setBackground(FONDO_INPUT);
        campo.setForeground(TEXTO_PRIMARIO);
        campo.setCaretColor(TEXTO_PRIMARIO);
        campo.setFont(FUENTE_CUERPO);
        campo.setBorder(bordeInput());
        campo.setToolTipText(placeholder);
        return campo;
    }

    /** Campo de contraseña con estilo oscuro. */
    public static JPasswordField campoPassword(String placeholder) {
        JPasswordField campo = new JPasswordField(20);
        campo.setBackground(FONDO_INPUT);
        campo.setForeground(TEXTO_PRIMARIO);
        campo.setCaretColor(TEXTO_PRIMARIO);
        campo.setFont(FUENTE_CUERPO);
        campo.setBorder(bordeInput());
        campo.setToolTipText(placeholder);
        return campo;
    }

    /** Etiqueta de título. */
    public static JLabel etiquetaTitulo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(TEXTO_PRIMARIO);
        return lbl;
    }

    /** Etiqueta normal. */
    public static JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_CUERPO);
        lbl.setForeground(TEXTO_SECUNDARIO);
        return lbl;
    }

    /** Panel con fondo de tarjeta y bordes redondeados. */
    public static JPanel panelTarjeta() {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FONDO_TARJETA);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(BORDE_SUTIL);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        return panel;
    }

    /**
     * Formatea un valor double como precio en pesos colombianos.
     * Ejemplo: 250000 → "$ 250.000"
     */
    public static String formatearPrecio(double valor) {
        long entero = Math.round(valor);
        String s = Long.toString(entero);
        StringBuilder sb = new StringBuilder();
        int start = s.length() % 3;
        if (start > 0) sb.append(s, 0, start);
        for (int i = start; i < s.length(); i += 3) {
            if (sb.length() > 0) sb.append('.');
            sb.append(s, i, i + 3);
        }
        return "$ " + sb;
    }
}
