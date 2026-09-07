package Modelo;

public class Administrador extends Usuario {

    public static final String CARGO_GERENTE = "GERENTE";
    public static final String CARGO_SUPERVISOR = "SUPERVISOR";
    public static final String CARGO_SOPORTE = "SOPORTE";

    private String cargo;

    @Override
    public Boolean iniciarSesion(String password) {

        if (this.cargo == null || this.cargo.trim().isEmpty()) {
            return false;
        }
        return password != null && password.length() >= 8;
    }

    public boolean gestionarProducto(Producto producto, int nuevoStock) {
        if (producto == null || nuevoStock < 0) {
            return false;
        }
        producto.setCantidadDisponible(nuevoStock);
        if (nuevoStock > 0 && Producto.ESTADO_AGOTADO.equals(producto.getEstado())) {
            producto.setEstado(Producto.ESTADO_DISPONIBLE);
        } else if (nuevoStock == 0) {
            producto.setEstado(Producto.ESTADO_AGOTADO);
        }
        return true;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
