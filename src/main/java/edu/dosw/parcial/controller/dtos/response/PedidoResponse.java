package edu.dosw.parcial.controller.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record PedidoResponse(
        String id,
        String correoCliente,
        String fechaHora,
        String estado,
        List<ItemResponse> items,
        EntregaResponse entrega,
        double subtotal,
        double total) {

    public record ItemResponse(
            String codigoProducto,
            String nombre,
            double precioBase,
            List<ExtraResponse> extras,
            double precioFinal) {
    }

    public record ExtraResponse(String codigo, String nombre, double precio) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record EntregaResponse(String tipo, String bloque, String salon, double costo) {
    }
}
