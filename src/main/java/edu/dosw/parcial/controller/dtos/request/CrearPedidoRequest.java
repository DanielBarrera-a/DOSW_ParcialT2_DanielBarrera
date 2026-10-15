package edu.dosw.parcial.controller.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CrearPedidoRequest(
        @NotNull(message = "es obligatorio")
        @Size(min = 1, max = 5, message = "debe tener entre 1 y 5 productos")
        @Valid
        List<@NotNull(message = "no puede ser nulo") ItemPedidoRequest> items,

        @NotNull(message = "es obligatorio")
        @Valid
        EntregaRequest entrega) {
}
