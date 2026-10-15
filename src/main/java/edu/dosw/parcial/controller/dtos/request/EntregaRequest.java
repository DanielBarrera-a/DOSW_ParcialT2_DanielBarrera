package edu.dosw.parcial.controller.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.dosw.parcial.core.models.TipoEntrega;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record EntregaRequest(
        @Schema(example = "ENTREGA_SALON")
        @NotNull(message = "es obligatorio")
        TipoEntrega tipo,

        @Schema(example = "A", description = "Obligatorio solo en ENTREGA_SALON")
        String bloque,

        @Schema(example = "A-301", description = "Obligatorio solo en ENTREGA_SALON")
        String salon) {

    @JsonIgnore
    @Schema(hidden = true)
    @AssertTrue(message = "para ENTREGA_SALON son obligatorios el bloque y el salón")
    public boolean isUbicacionCompleta() {
        if (tipo != TipoEntrega.ENTREGA_SALON) {
            return true;
        }
        return bloque != null && !bloque.isBlank() && salon != null && !salon.isBlank();
    }
}
