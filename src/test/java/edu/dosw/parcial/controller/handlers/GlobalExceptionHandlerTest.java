package edu.dosw.parcial.controller.handlers;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.core.exceptions.ConflictoException;
import edu.dosw.parcial.core.exceptions.RecursoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.ReglaNegocioException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/pedidos");

    @Test
    void manejarNoEncontrado_responde404ConElMensaje() {
        // Act
        ResponseEntity<ErrorResponse> respuesta = handler.manejarNoEncontrado(
                new RecursoNoEncontradoException("No existe el producto PROD-009"), request);

        // Assert
        assertEquals(404, respuesta.getStatusCode().value());
        assertEquals("No existe el producto PROD-009", respuesta.getBody().mensaje());
        assertEquals("/api/v1/pedidos", respuesta.getBody().ruta());
        assertNull(respuesta.getBody().campos());
    }

    @Test
    void manejarConflicto_responde409() {
        // Act
        ResponseEntity<ErrorResponse> respuesta = handler.manejarConflicto(
                new ConflictoException("Ya existe un producto con el código PROD-001"), request);

        // Assert
        assertEquals(409, respuesta.getStatusCode().value());
        assertEquals("Conflict", respuesta.getBody().error());
    }

    @Test
    void manejarReglaNegocio_responde422() {
        // Act
        ResponseEntity<ErrorResponse> respuesta = handler.manejarReglaNegocio(
                new ReglaNegocioException("El subtotal de $2.500 no alcanza el mínimo de $3.500"), request);

        // Assert
        assertEquals(422, respuesta.getStatusCode().value());
        assertEquals("El subtotal de $2.500 no alcanza el mínimo de $3.500", respuesta.getBody().mensaje());
    }

    @Test
    void manejarInesperado_responde500SinMostrarElDetalleInterno() {
        // Act
        ResponseEntity<ErrorResponse> respuesta = handler.manejarInesperado(
                new IllegalStateException("conexión a Mongo perdida"), request);

        // Assert
        assertEquals(500, respuesta.getStatusCode().value());
        assertEquals("Ocurrió un error interno, intente más tarde", respuesta.getBody().mensaje());
    }
}
