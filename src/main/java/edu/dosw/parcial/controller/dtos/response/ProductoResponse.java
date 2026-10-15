package edu.dosw.parcial.controller.dtos.response;

public record ProductoResponse(
        String id,
        String codigo,
        String nombre,
        double precio,
        boolean activo) {
}
