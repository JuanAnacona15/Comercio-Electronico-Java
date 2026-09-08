package Controlador;

import Modelo.*;
import java.util.ArrayList;

/**
 * Gestiona las operaciones sobre el catálogo de productos.
 */
public class ControladorProductos {

    private final ControladorPrincipal ctx;

    public ControladorProductos(ControladorPrincipal ctx) {
        this.ctx = ctx;
    }

    /** Devuelve todos los productos. */
    public ArrayList<Producto> obtenerTodos() {
        return ctx.getListaProductos();
    }

    /**
     * Agrega un nuevo producto al catálogo.
     * @return true si se agregó correctamente, false si los datos son inválidos.
     */
    public boolean agregarProducto(String id, String nombre, String descripcion,
                                   double precio, int stock, String tipo) {
        if (id == null || id.trim().isEmpty()) return false;
        if (nombre == null || nombre.trim().isEmpty()) return false;
        if (precio < 0 || stock < 0) return false;

        // Verificar ID duplicado
        for (Producto p : ctx.getListaProductos()) {
            if (p.getIdentificador().equalsIgnoreCase(id.trim())) return false;
        }

        Producto nuevo = new Producto();
        nuevo.setIdentificador(id.trim());
        nuevo.setNombre(nombre.trim());
        nuevo.setDescripcion(descripcion != null ? descripcion.trim() : "");
        nuevo.setPrecio(precio);
        nuevo.setCantidadDisponible(stock);
        nuevo.setTipoProducto(tipo);
        nuevo.setEstado(stock > 0 ? Producto.ESTADO_DISPONIBLE : Producto.ESTADO_AGOTADO);

        ctx.getListaProductos().add(nuevo);
        return true;
    }

    /**
     * Actualiza el stock de un producto usando la lógica del Administrador.
     * @return true si se actualizó correctamente.
     */
    public boolean actualizarStock(Producto producto, int nuevoStock) {
        Administrador admin = ctx.getAdminActivo();
        if (admin == null) return false;
        return admin.gestionarProducto(producto, nuevoStock);
    }

    /**
     * Elimina un producto del catálogo por su identificador.
     * @return true si se encontró y eliminó.
     */
    public boolean eliminarProducto(String id) {
        return ctx.getListaProductos().removeIf(
            p -> p.getIdentificador().equalsIgnoreCase(id)
        );
    }

    /** Busca un producto por ID. */
    public Producto buscarPorId(String id) {
        for (Producto p : ctx.getListaProductos()) {
            if (p.getIdentificador().equalsIgnoreCase(id)) return p;
        }
        return null;
    }
}
