package Modelo;

public interface Pago {
    boolean procesarPago();

    Comprobante generarComprobante();
}
