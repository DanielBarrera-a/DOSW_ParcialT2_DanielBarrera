package edu.dosw.parcial.core.models;

public class Entrega {
    private TipoEntrega tipo;
    private String bloque;
    private String salon;

    public Entrega() {
    }

    public Entrega(TipoEntrega tipo, String bloque, String salon) {
        this.tipo = tipo;
        this.bloque = bloque;
        this.salon = salon;
    }

    public TipoEntrega getTipo() {
        return tipo;
    }
    public void setTipo(TipoEntrega tipo) {
        this.tipo = tipo;
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
