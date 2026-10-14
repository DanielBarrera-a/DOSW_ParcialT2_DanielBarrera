package edu.dosw.parcial.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoBuilder {

    private Usuario cliente;
    private final List<ItemPedido> items = new ArrayList<>();
    private Entrega entrega;

    public PedidoBuilder paraCliente(Usuario cliente) {
        this.cliente = cliente;
        return this;
    }

    public PedidoBuilder agregarItem(ItemPedido item) {
        this.items.add(item);
        return this;
    }

    public PedidoBuilder conEntrega(Entrega entrega) {
        this.entrega = entrega;
        return this;
    }

    public Pedido build() {
        double subtotal = items.stream()
                .mapToDouble(ItemPedido::getPrecioFinal)
                .sum();

        double costoEntrega = 0;

        if (entrega != null && entrega.getTipo() != null) {
            switch (entrega.getTipo()) {
                case PARA_LLEVAR -> costoEntrega = 300;
                case ENTREGA_SALON -> costoEntrega = 1000;
                case CONSUMO_LOCAL -> costoEntrega = 0;
            }
        }

        return new Pedido(
                null,
                cliente,
                items,
                entrega,
                LocalDateTime.now(),
                subtotal,
                subtotal + costoEntrega,
                EstadoPedido.CONFIRMADO
        );
    }
}