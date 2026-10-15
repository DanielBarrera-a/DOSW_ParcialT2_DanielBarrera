package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record ItemPedidoRequest(
        @Schema(example = "PROD-002")
        @NotBlank(message = "es obligatorio")
        @Pattern(regexp = "PROD-\\d{3}", message = "debe tener el formato PROD-NNN")
        String codigoProducto,

        @Schema(example = "[\"EXT-004\", \"EXT-002\"]", description = "Opcional")
        List<@NotBlank(message = "no puede estar vacío") String> extras) {
}
