package edu.dosw.parcial.core.models;

import java.util.ArrayList;
import java.util.List;

public class ItemPedido {
    private String codigoProducto;
    private String nombreProducto;
    private double precioBase;
    private List<ExtraAplicado> extras;

    public ItemPedido() {
        this.extras = new ArrayList<ExtraAplicado>();
    }

    public ItemPedido(String codigoProducto, String nombreProducto,
                      double precioBase, List<ExtraAplicado> extras) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precioBase = precioBase;
        this.extras = extras == null
                ? new ArrayList<ExtraAplicado>()
                : new ArrayList<ExtraAplicado>(extras);
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public List<ExtraAplicado> getExtras() {
        return extras;
    }

    public void setExtras(List<ExtraAplicado> extras) {
        this.extras = extras == null
                ? new ArrayList<ExtraAplicado>()
                : new ArrayList<ExtraAplicado>(extras);
    }

    public double getPrecioFinal() {
        double total = precioBase;

        for (ExtraAplicado extra : extras) {
            total += extra.getPrecio();
        }
        return total;
    }
}
