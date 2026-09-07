package Modelo;

import java.util.ArrayList;

public class Cliente extends Usuario {
    private String direccionEnvio;
    private CarritoDeCompra carritoDeCompra;
    ArrayList<Pedido> Lista_pedidos = new ArrayList<>();

    @Override
    public Boolean iniciarSesion(String contraseña) {
        if (getCorreo() == null || !getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return false;
        }
        return contraseña != null && !contraseña.trim().isEmpty();
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public CarritoDeCompra getCarritoDeCompra() {
        return carritoDeCompra;
    }

    public void setCarritoDeCompra(CarritoDeCompra carritoDeCompra) {
        this.carritoDeCompra = carritoDeCompra;
    }

    public ArrayList<Pedido> getLista_pedidos() {
        return Lista_pedidos;
    }

    public void setLista_pedidos(ArrayList<Pedido> Lista_pedidos) {
        this.Lista_pedidos = Lista_pedidos;
    }

    public boolean registrarse() {
        if (getNombre() == null || getNombre().trim().isEmpty()) {
            return false;
        }

        return !(direccionEnvio == null || direccionEnvio.trim().isEmpty());
    }

    public void agregarPedidoAlHistorial(Pedido pedido) {
        if (pedido != null) {
            Lista_pedidos.add(pedido);
        }
    }

}