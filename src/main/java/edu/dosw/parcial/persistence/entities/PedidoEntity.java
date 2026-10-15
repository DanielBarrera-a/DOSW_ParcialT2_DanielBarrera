package edu.dosw.parcial.persistence.entities;

import edu.dosw.parcial.core.models.Entrega;
import edu.dosw.parcial.core.models.EstadoPedido;
import edu.dosw.parcial.core.models.ItemPedido;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pedidos")
public class PedidoEntity {

    @Id
    private String id;

    @Indexed
    private String clienteId;
    private String correoCliente;
    private List<ItemPedido> items = new ArrayList<>();
    private Entrega entrega;
    @Indexed
    private LocalDateTime fechaDeCreacion;
    private double subtotal;
    private double total;
    private EstadoPedido estado;

    public PedidoEntity() {
    }

    public PedidoEntity(String id, String clienteId, String correoCliente,
                        List<ItemPedido> items, Entrega entrega,
                        LocalDateTime fechaDeCreacion, double subtotal,
                        double total, EstadoPedido estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.correoCliente = correoCliente;
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

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }

    public List<ItemPedido> getItems() {
        return new ArrayList<>(items);
    }

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
