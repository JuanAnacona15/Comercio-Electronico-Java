package Controlador;

import Modelo.*;
import java.util.ArrayList;

/**
 * Controlador central del sistema.
 * Mantiene los vectores en memoria y coordina la navegación.
 * 
 * ── DATOS DE EJEMPLO ─────────────────────────────────────────────────────────
 * Para cambiar o eliminar los datos de prueba, edita el método cargarDatosDePrueba().
 * Para desactivarlo completamente, comenta la llamada en el constructor.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class ControladorPrincipal {

    // ── Vectores de datos en memoria ─────────────────────────────────────────
    private final ArrayList<Producto>      listaProductos    = new ArrayList<>();
    private final ArrayList<Cliente>       listaClientes     = new ArrayList<>();
    private final ArrayList<Administrador> listaAdmins       = new ArrayList<>();
    private final ArrayList<Pedido>        listaPedidos      = new ArrayList<>();
    private final ArrayList<Comprobante>   listaComprobantes = new ArrayList<>();

    // ── Estado de sesión activa ───────────────────────────────────────────────
    private Cliente       clienteActivo = null;
    private Administrador adminActivo   = null;

    // ── Referencia a la ventana principal (para navegar entre paneles) ────────
    private Vista.VentanaPrincipal ventana;

    // ─────────────────────────────────────────────────────────────────────────
    public ControladorPrincipal() {
        cargarDatosDePrueba(); // Comenta esta línea para arrancar sin datos de ejemplo
    }

    // ── Datos de ejemplo ─────────────────────────────────────────────────────
    /**
     * Pre-carga productos, clientes y administradores de prueba.
     * Modifica este método libremente para ajustar los datos iniciales.
     */
    private void cargarDatosDePrueba() {
        // -- Productos físicos --
        listaProductos.add(crearProducto("P001", "Teclado Mecánico RGB",
                "Teclado gaming con switches Cherry MX Blue y retroiluminación RGB",
                250000, 15, Producto.TIPO_FISICO));

        listaProductos.add(crearProducto("P002", "Mouse Inalámbrico",
                "Mouse ergonómico 2.4GHz con 6 botones programables",
                89000, 30, Producto.TIPO_FISICO));

        listaProductos.add(crearProducto("P003", "Audífonos Bluetooth",
                "Audífonos over-ear con cancelación de ruido activa y 30h de batería",
                350000, 0, Producto.TIPO_FISICO)); // Agotado intencionalmente

        // -- Productos digitales --
        listaProductos.add(crearProducto("D001", "Licencia Microsoft Office 365",
                "Suscripción anual para 1 usuario con 1TB en OneDrive",
                320000, 100, Producto.TIPO_DIGITAL));

        listaProductos.add(crearProducto("D002", "Pack de Iconos Premium",
                "Colección de 5000 iconos vectoriales en SVG y PNG",
                45000, 50, Producto.TIPO_DIGITAL));

        listaProductos.add(crearProducto("D003", "Curso de Java Avanzado",
                "Acceso de por vida a 80 horas de contenido en video + certificado",
                180000, 200, Producto.TIPO_DIGITAL));

        // -- Administrador por defecto --
        Administrador admin = new Administrador();
        admin.setId("A001");
        admin.setNombre("Administrador");
        admin.setCorreo("admin@pce.com");
        admin.setCargo(Administrador.CARGO_GERENTE);
        // Contraseña: admin1234 (mínimo 8 caracteres según el modelo)
        listaAdmins.add(admin);

        // -- Cliente de prueba --
        Cliente cliente = new Cliente();
        cliente.setId("C001");
        cliente.setNombre("Juan Pérez");
        cliente.setCorreo("juan@example.com");
        cliente.setDireccionEnvio("Cra 15 #45-23, Bogotá");
        cliente.setCarritoDeCompra(new CarritoDeCompra());
        listaClientes.add(cliente);
    }

    /** Método auxiliar para construir un Producto rápidamente. */
    private Producto crearProducto(String id, String nombre, String descripcion,
                                   double precio, int stock, String tipo) {
        Producto p = new Producto();
        p.setIdentificador(id);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setCantidadDisponible(stock);
        p.setTipoProducto(tipo);
        p.setEstado(stock > 0 ? Producto.ESTADO_DISPONIBLE : Producto.ESTADO_AGOTADO);
        return p;
    }

    // ── Gestión de ventana ────────────────────────────────────────────────────
    public void setVentana(Vista.VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    public Vista.VentanaPrincipal getVentana() {
        return ventana;
    }

    // ── Getters de vectores ───────────────────────────────────────────────────
    public ArrayList<Producto>      getListaProductos()    { return listaProductos; }
    public ArrayList<Cliente>       getListaClientes()     { return listaClientes; }
    public ArrayList<Administrador> getListaAdmins()       { return listaAdmins; }
    public ArrayList<Pedido>        getListaPedidos()      { return listaPedidos; }
    public ArrayList<Comprobante>   getListaComprobantes() { return listaComprobantes; }

    // ── Sesión ────────────────────────────────────────────────────────────────
    public Cliente getClienteActivo()             { return clienteActivo; }
    public void    setClienteActivo(Cliente c)    { this.clienteActivo = c; }
    public Administrador getAdminActivo()         { return adminActivo; }
    public void    setAdminActivo(Administrador a){ this.adminActivo = a; }

    public void cerrarSesion() {
        clienteActivo = null;
        adminActivo   = null;
    }

    // ── Registro de pedidos y comprobantes ───────────────────────────────────
    public void registrarPedido(Pedido pedido) {
        if (pedido != null) listaPedidos.add(pedido);
    }

    public void registrarComprobante(Comprobante comprobante) {
        if (comprobante != null) listaComprobantes.add(comprobante);
    }
}
