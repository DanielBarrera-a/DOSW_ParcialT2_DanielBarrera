package edu.dosw.parcial.controller.dtos;

import edu.dosw.parcial.core.models.TipoEntrega;
import java.util.List;

public class CrearPedidoRequest {

    private List<ItemPedidoRequest> items;
    private TipoEntrega tipoEntrega;
    private String bloque;
    private String salon;

    public List<ItemPedidoRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoRequest> items) {
        this.items = items;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

}