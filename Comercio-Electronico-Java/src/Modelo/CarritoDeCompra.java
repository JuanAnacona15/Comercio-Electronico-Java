package Modelo;

import java.util.ArrayList;

public class CarritoDeCompra {
    ArrayList<ItemCarrito> items = new ArrayList<>();

    public ArrayList<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemCarrito> items) {
        this.items = items;
    }

    public boolean agregarProducto(Producto producto, int cantidad) {
        if (producto != null && producto.verificarDisponibilidad(cantidad)) {
            ItemCarrito item = new ItemCarrito();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            items.add(item);
            return true;
        }
        return false;
    }

    public void eliminarProducto(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.calcularTotalItem();
        }
        return subtotal;
    }

    public void limpiarCarrito() {
        items.clear();
    }

}
