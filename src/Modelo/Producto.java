package Modelo;

public class Producto {

    public static final String TIPO_FISICO = "FISICO";
    public static final String TIPO_DIGITAL = "DIGITAL";

    public static final String ESTADO_DISPONIBLE = "DISPONIBLE";
    public static final String ESTADO_AGOTADO = "AGOTADO";

    private String identificador;
    private String nombre;
    private String descripcion;
    private String urlImagen;
    private double precio;
    private int cantidadDisponible;
    private String estado;
    private String tipoProducto;

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean verificarDisponibilidad(int cantidad) {
        if (cantidad <= 0) {
            return false;
        }
        return this.cantidadDisponible >= cantidad && ESTADO_DISPONIBLE.equals(this.estado);
    }

    public void actualizarInventario(int cantidadVendida) {
        if (cantidadVendida <= 0) {
            return;
        }
        this.cantidadDisponible = Math.max(0, this.cantidadDisponible - cantidadVendida);
        if (this.cantidadDisponible == 0) {
            this.estado = ESTADO_AGOTADO;
        }
    }

}
