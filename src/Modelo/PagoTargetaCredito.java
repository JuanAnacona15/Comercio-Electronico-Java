package Modelo;

import java.util.Date;

public class PagoTargetaCredito implements Pago {
    private String numeroTarjeta;

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    @Override
    public boolean procesarPago() {

        return numeroTarjeta != null && !numeroTarjeta.trim().isEmpty();
    }

    @Override
    public Comprobante generarComprobante() {
        Comprobante comprobante = new Comprobante();
        comprobante.setNumeroUnico("TC-" + System.currentTimeMillis());
        comprobante.setFecha(new Date());
        comprobante.setEstado(Comprobante.ESTADO_APROBADO);
        return comprobante;
    }
}
