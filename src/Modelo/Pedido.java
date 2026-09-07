package Modelo;

import java.util.Date;

public class Pedido {
    private String identificador;
    private Date fecha;
    private double subtotal;
    private double impuesto19;
    private double valorTotal;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuesto19() {
        return impuesto19;
    }

    public void setImpuesto19(double impuesto19) {
        this.impuesto19 = impuesto19;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double calcularImpuesto() {
        impuesto19 = subtotal * 0.19;
        return impuesto19;
    }

    public double calcularTotal() {
        valorTotal = subtotal + calcularImpuesto();
        return valorTotal;
    }

}
