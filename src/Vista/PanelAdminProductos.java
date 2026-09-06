package Vista;

import Controlador.*;
import Modelo.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Panel de gestión de productos para el Administrador.
 * Permite ver, agregar, actualizar stock y eliminar productos del catálogo.
 */
public class PanelAdminProductos extends JPanel implements Refrescable {

    private final VentanaPrincipal     ventana;
    private final ControladorPrincipal ctx;
    private final ControladorProductos ctrlProductos;

    private DefaultTableModel modeloTabla;
    private JTable            tabla;

    // Campos del formulario de nuevo producto
    private JTextField campoId;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoPrecio;
    private JTextField campoStock;
    private JComboBox<String> comboTipo;

    public PanelAdminProductos(VentanaPrincipal ventana, ControladorPrincipal ctx,
                               ControladorProductos ctrlProductos) {
        this.ventana       = ventana;
        this.ctx           = ctx;
        this.ctrlProductos = ctrlProductos;
        construirUI();
    }

    private void construirUI() {
        setBackground(Estilos.FONDO_OSCURO);
        setLayout(new BorderLayout());

        // ── Barra superior ────────────────────────────────────────────────────
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(Estilos.FONDO_PANEL);
        barraTop.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel titulo = new JLabel("⚙️  Panel de Administración — Productos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Estilos.TEXTO_PRIMARIO);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);

        JLabel lblAdmin = new JLabel();
        lblAdmin.setFont(Estilos.FUENTE_CUERPO);
        lblAdmin.setForeground(Estilos.TEXTO_MUTED);
        if (ctx.getAdminActivo() != null) {
            lblAdmin.setText("👤 " + ctx.getAdminActivo().getNombre());
        }

        JButton btnCerrar = Estilos.botonFantasma("Cerrar sesión");
        btnCerrar.addActionListener(e -> {
            ctx.cerrarSesion();
            ventana.mostrarPanel(VentanaPrincipal.CARD_LOGIN);
        });

        acciones.add(lblAdmin);
        acciones.add(btnCerrar);

        barraTop.add(titulo,   BorderLayout.WEST);
        barraTop.add(acciones, BorderLayout.EAST);

        // ── Contenido: tabla (izquierda) + formulario (derecha) ───────────────
        JPanel contenido = new JPanel(new BorderLayout(16, 0));
        contenido.setBackground(Estilos.FONDO_OSCURO);
        contenido.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tabla
        contenido.add(construirPanelTabla(),     BorderLayout.CENTER);
        // Formulario
        contenido.add(construirFormulario(),     BorderLayout.EAST);

        add(barraTop, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
    }

    private JPanel construirPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JLabel lbl = Estilos.etiquetaTitulo("Catálogo de Productos");
        lbl.setFont(Estilos.FUENTE_SUBTITULO);

        String[] cols = {"ID", "Nombre", "Tipo", "Precio", "Stock", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        estilizarTabla(tabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(Estilos.FONDO_TARJETA);
        scroll.setBorder(BorderFactory.createLineBorder(Estilos.BORDE_SUTIL));

        // Botones de acción sobre tabla
        JPanel botonesTabla = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botonesTabla.setOpaque(false);

        JButton btnActualizarStock = Estilos.botonPrimario("Actualizar Stock");
        JButton btnEliminar        = Estilos.botonPeligro("Eliminar producto");
        JButton btnRefrescar       = Estilos.botonFantasma("↺ Refrescar");

        btnActualizarStock.addActionListener(e -> actualizarStockSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnRefrescar.addActionListener(e -> refrescar());

        botonesTabla.add(btnActualizarStock);
        botonesTabla.add(btnEliminar);
        botonesTabla.add(btnRefrescar);

        panel.add(lbl,         BorderLayout.NORTH);
        panel.add(scroll,      BorderLayout.CENTER);
        panel.add(botonesTabla, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel construirFormulario() {
        JPanel panel = Estilos.panelTarjeta();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(280, 0));

        JLabel tit = new JLabel("➕  Nuevo Producto");
        tit.setFont(Estilos.FUENTE_SUBTITULO);
        tit.setForeground(Estilos.TEXTO_PRIMARIO);
        tit.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoId          = campo("Identificador (ej: P004)");
        campoNombre      = campo("Nombre del producto");
        campoDescripcion = campo("Descripción breve");
        campoPrecio      = campo("Precio en COP (ej: 150000)");
        campoStock       = campo("Cantidad inicial en stock");

        comboTipo = new JComboBox<>(new String[]{"FISICO", "DIGITAL"});
        comboTipo.setBackground(Estilos.FONDO_INPUT);
        comboTipo.setForeground(Estilos.TEXTO_PRIMARIO);
        comboTipo.setFont(Estilos.FUENTE_CUERPO);
        comboTipo.setBorder(Estilos.bordeInput());
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        comboTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnAgregar = Estilos.botonSecundario("Agregar al catálogo");
        btnAgregar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnAgregar.addActionListener(e -> agregarProducto());

        panel.add(tit);
        panel.add(Box.createVerticalStrut(16));
        panel.add(lbl("ID del producto")); panel.add(Box.createVerticalStrut(4)); panel.add(campoId);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl("Nombre")); panel.add(Box.createVerticalStrut(4)); panel.add(campoNombre);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl("Descripción")); panel.add(Box.createVerticalStrut(4)); panel.add(campoDescripcion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl("Precio (COP)")); panel.add(Box.createVerticalStrut(4)); panel.add(campoPrecio);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl("Stock inicial")); panel.add(Box.createVerticalStrut(4)); panel.add(campoStock);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl("Tipo")); panel.add(Box.createVerticalStrut(4)); panel.add(comboTipo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnAgregar);

        return panel;
    }

    @Override
    public void refrescar() {
        modeloTabla.setRowCount(0);
        for (Producto p : ctrlProductos.obtenerTodos()) {
            modeloTabla.addRow(new Object[]{
                p.getIdentificador(),
                p.getNombre(),
                p.getTipoProducto() != null ? p.getTipoProducto() : "—",
                Estilos.formatearPrecio(p.getPrecio()),
                p.getCantidadDisponible(),
                p.getEstado() != null ? p.getEstado() : "—"
            });
        }
    }

    private void agregarProducto() {
        try {
            String id   = campoId.getText().trim();
            String nombre = campoNombre.getText().trim();
            String desc = campoDescripcion.getText().trim();
            double precio = Double.parseDouble(campoPrecio.getText().trim());
            int stock     = Integer.parseInt(campoStock.getText().trim());
            String tipo = "DIGITAL".equals(comboTipo.getSelectedItem())
                    ? Producto.TIPO_DIGITAL : Producto.TIPO_FISICO;

            boolean ok = ctrlProductos.agregarProducto(id, nombre, desc, precio, stock, tipo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                refrescar();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo agregar el producto.\nVerifica que el ID no esté duplicado y los datos sean válidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "El precio y el stock deben ser valores numéricos.", "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarStockSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) modeloTabla.getValueAt(fila, 0);
        Producto producto = ctrlProductos.buscarPorId(id);
        if (producto == null) return;

        String input = JOptionPane.showInputDialog(this,
            "Nuevo stock para \"" + producto.getNombre() + "\":",
            "Actualizar Stock", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;

        try {
            int nuevoStock = Integer.parseInt(input.trim());
            boolean ok = ctrlProductos.actualizarStock(producto, nuevoStock);
            if (ok) {
                refrescar();
                JOptionPane.showMessageDialog(this, "Stock actualizado a " + nuevoStock + " unidades.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar. Verifica tu sesión de admin.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un número entero válido.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Seguro que deseas eliminar el producto con ID \"" + id + "\"?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            ctrlProductos.eliminarProducto(id);
            refrescar();
        }
    }

    private void limpiarFormulario() {
        campoId.setText("");
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoPrecio.setText("");
        campoStock.setText("");
        comboTipo.setSelectedIndex(0);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JTextField campo(String placeholder) {
        JTextField tf = Estilos.campoTexto(placeholder);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tf;
    }

    private JLabel lbl(String texto) {
        JLabel l = Estilos.etiqueta(texto);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void estilizarTabla(JTable t) {
        t.setBackground(Estilos.FONDO_TARJETA);
        t.setForeground(Estilos.TEXTO_PRIMARIO);
        t.setFont(Estilos.FUENTE_CUERPO);
        t.setRowHeight(34);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setBackground(Estilos.FONDO_PANEL);
        t.getTableHeader().setForeground(Estilos.TEXTO_SECUNDARIO);
        t.getTableHeader().setFont(Estilos.FUENTE_BOLD_SM);
        t.setSelectionBackground(new Color(99, 102, 241, 60));
        t.setSelectionForeground(Estilos.TEXTO_PRIMARIO);
        t.setBorder(null);
    }
}
