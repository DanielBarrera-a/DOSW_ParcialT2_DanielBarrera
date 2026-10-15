package edu.dosw.parcial.controller.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String codigoError,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> campos
) {
}
