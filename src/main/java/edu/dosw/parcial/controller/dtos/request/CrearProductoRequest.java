package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearProductoRequest(
        @Schema(example = "Arroz con pollo")
        @NotBlank(message = "es obligatorio")
        @Size(min = 3, max = 60, message = "debe tener entre 3 y 60 caracteres")
        String nombre,

        @Schema(example = "9000")
        @NotNull(message = "es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "debe ser mayor a 0")
        Double precio,

        @Schema(example = "true", description = "Opcional; por defecto true")
        Boolean activo) {
}
