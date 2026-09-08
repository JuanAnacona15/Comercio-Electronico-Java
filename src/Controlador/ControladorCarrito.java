package Controlador;

import Modelo.*;

/**
 * Gestiona las operaciones sobre el carrito de compras del cliente activo.
 */
public class ControladorCarrito {

    private final ControladorPrincipal ctx;

    public ControladorCarrito(ControladorPrincipal ctx) {
        this.ctx = ctx;
    }

    /** Obtiene el carrito del cliente activo. Nunca devuelve null. */
    public CarritoDeCompra getCarrito() {
        Cliente cliente = ctx.getClienteActivo();
        if (cliente == null) return new CarritoDeCompra();
        if (cliente.getCarritoDeCompra() == null) {
            cliente.setCarritoDeCompra(new CarritoDeCompra());
        }
        return cliente.getCarritoDeCompra();
    }

    /**
     * Intenta agregar un producto al carrito.
     * Valida disponibilidad antes de agregarlo.
     * @return true si se agregó, false si no hay stock suficiente o datos inválidos.
     */
    public boolean agregarProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) return false;
        return getCarrito().agregarProducto(producto, cantidad);
    }

    /**
     * Elimina el ítem en la posición dada del carrito.
     */
    public void eliminarItem(int index) {
        getCarrito().eliminarProducto(index);
    }

    /** Vacía completamente el carrito. */
    public void vaciarCarrito() {
        getCarrito().limpiarCarrito();
    }

    /** Calcula el subtotal del carrito. */
    public double calcularSubtotal() {
        return getCarrito().calcularSubtotal();
    }

    /** Calcula el impuesto del 19% sobre el subtotal. */
    public double calcularImpuesto() {
        return calcularSubtotal() * 0.19;
    }

    /** Calcula el total (subtotal + IVA). */
    public double calcularTotal() {
        double subtotal = calcularSubtotal();
        return subtotal + (subtotal * 0.19);
    }

    /** Indica si el carrito está vacío. */
    public boolean estaVacio() {
        return getCarrito().getItems().isEmpty();
    }
}
