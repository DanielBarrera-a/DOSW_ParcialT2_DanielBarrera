package edu.dosw.parcial.controller.dtos;

import java.util.List;

public class ItemPedidoRequest {

    private String codigoProducto;
    private List<String> codigosExtras;

    public String getCodigoProducto() {
        return codigoProducto;
    }
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    public List<String> getCodigosExtras() {
        return codigosExtras;
    }
    public void setCodigosExtras(List<String> codigosExtras) {
        this.codigosExtras = codigosExtras;
    }
}