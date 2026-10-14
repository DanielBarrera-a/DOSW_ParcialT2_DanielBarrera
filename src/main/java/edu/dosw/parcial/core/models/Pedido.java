package edu.dosw.parcial.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private String id;
    private Usuario cliente;
    private List<ItemPedido> items;
    private Entrega entrega;
    private LocalDateTime fechaDeCreacion;
    private double subtotal;
    private double total;
    private EstadoPedido estado;

    public Pedido() {
        this.items = new ArrayList<>();
    }

    public Pedido(String id, Usuario cliente, List<ItemPedido> items,
                  Entrega entrega, LocalDateTime fechaDeCreacion,
                  double subtotal, double total, EstadoPedido estado) {
        this.id = id;
        this.cliente = cliente;
        this.items = items == null
                ? new ArrayList<>()
                : new ArrayList<>(items);
        this.entrega = entrega;
        this.fechaDeCreacion = fechaDeCreacion;
        this.subtotal = subtotal;
        this.total = total;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }

    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) {
        this.items = items == null
                ? new ArrayList<>()
                : new ArrayList<>(items);
    }

    public Entrega getEntrega() { return entrega; }
    public void setEntrega(Entrega entrega) { this.entrega = entrega; }

    public LocalDateTime getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(LocalDateTime fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }
}