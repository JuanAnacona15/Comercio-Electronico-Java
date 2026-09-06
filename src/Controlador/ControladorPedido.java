package Controlador;

import Modelo.*;
import java.util.Date;
import java.util.UUID;

/**
 * Gestiona la generación de pedidos y el procesamiento de pagos.
 */
public class ControladorPedido {

    private final ControladorPrincipal ctx;
    private final ControladorCarrito   ctrlCarrito;

    /** Pedido y comprobante del proceso de compra actual. */
    private Pedido      pedidoActual      = null;
    private Comprobante comprobanteActual = null;

    public ControladorPedido(ControladorPrincipal ctx, ControladorCarrito ctrlCarrito) {
        this.ctx         = ctx;
        this.ctrlCarrito = ctrlCarrito;
    }

    /**
     * Genera un Pedido a partir del estado actual del carrito.
     * @return El pedido generado, o null si el carrito está vacío.
     */
    public Pedido generarPedido() {
        if (ctrlCarrito.estaVacio()) return null;

        Pedido pedido = new Pedido();
        pedido.setIdentificador("PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pedido.setFecha(new Date());
        pedido.setSubtotal(ctrlCarrito.calcularSubtotal());
        pedido.calcularImpuesto();
        pedido.calcularTotal();

        this.pedidoActual = pedido;
        return pedido;
    }

    /**
     * Procesa el pago con Tarjeta de Crédito.
     * Si es exitoso: actualiza inventario, registra pedido y genera comprobante.
     * @return El comprobante generado, o null si el pago falló.
     */
    public Comprobante procesarConTarjeta(String numeroTarjeta) {
        PagoTragetaCredito pago = new PagoTragetaCredito();
        pago.setNumeroTarjeta(numeroTarjeta);
        return ejecutarPago(pago);
    }

    /**
     * Procesa el pago con Cuenta Digital.
     * Si es exitoso: actualiza inventario, registra pedido y genera comprobante.
     * @return El comprobante generado, o null si el pago falló.
     */
    public Comprobante procesarConCuentaDigital(String correoCuenta) {
        PagoCuentaDigital pago = new PagoCuentaDigital();
        pago.setCorreoCuenta(correoCuenta);
        return ejecutarPago(pago);
    }

    /** Lógica común de procesamiento de pago. */
    private Comprobante ejecutarPago(Pago pago) {
        if (pedidoActual == null) return null;

        boolean aprobado = pago.procesarPago();
        Comprobante comprobante = pago.generarComprobante();

        if (!aprobado) {
            comprobante.setEstado(Comprobante.ESTADO_RECHAZADO);
        } else {
            // Actualizar inventario de cada producto comprado
            for (ItemCarrito item : ctrlCarrito.getCarrito().getItems()) {
                item.getProducto().actualizarInventario(item.getCantidad());
            }
            // Registrar pedido en el historial del cliente
            Cliente cliente = ctx.getClienteActivo();
            if (cliente != null) {
                cliente.agregarPedidoAlHistorial(pedidoActual);
            }
            ctx.registrarPedido(pedidoActual);
        }

        ctx.registrarComprobante(comprobante);
        this.comprobanteActual = comprobante;

        // Limpiar carrito tras el pago (aprobado o no)
        ctrlCarrito.vaciarCarrito();

        return comprobante;
    }

    public Pedido      getPedidoActual()      { return pedidoActual; }
    public Comprobante getComprobanteActual() { return comprobanteActual; }

    public void resetear() {
        pedidoActual      = null;
        comprobanteActual = null;
    }
}
