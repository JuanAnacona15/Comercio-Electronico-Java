package Modelo;

public class ItemCarrito {
    private int cantidad;
    private Producto producto;

    public double calcularTotalItem() {
        return producto.getPrecio() * cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

}