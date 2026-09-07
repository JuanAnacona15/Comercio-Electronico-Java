
package Modelo;

import java.util.Date;

public class PagoCuentaDigital implements Pago {
    private String correoCuenta;

    public String getCorreoCuenta() {
        return correoCuenta;
    }

    public void setCorreoCuenta(String correoCuenta) {
        this.correoCuenta = correoCuenta;
    }

    @Override
    public boolean procesarPago() {
        return correoCuenta != null && correoCuenta.contains("@");
    }

    @Override
    public Comprobante generarComprobante() {
        Comprobante comprobante = new Comprobante();
        comprobante.setNumeroUnico("CD-" + System.currentTimeMillis());
        comprobante.setFecha(new Date());
        comprobante.setEstado(Comprobante.ESTADO_APROBADO);
        return comprobante;
    }

}
