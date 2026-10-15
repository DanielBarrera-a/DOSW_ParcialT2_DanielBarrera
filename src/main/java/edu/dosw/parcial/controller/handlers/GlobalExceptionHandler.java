package edu.dosw.parcial.controller.handlers;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.core.exceptions.ConflictoException;
import edu.dosw.parcial.core.exceptions.CredencialesInvalidasException;
import edu.dosw.parcial.core.exceptions.RecursoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.ReglaNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarDatosInvalidos(MethodArgumentNotValidException ex,
                                                               HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> campos.putIfAbsent(error.getField(), error.getDefaultMessage()));

        log.warn("Petición rechazada por datos inválidos en {}: {}", request.getRequestURI(), campos.keySet());
        return construir(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos", request, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoIlegible(HttpMessageNotReadableException ex,
                                                               HttpServletRequest request) {
        log.warn("Petición rechazada por cuerpo ilegible en {}", request.getRequestURI());
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no tiene un formato válido",
                request, null);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> manejarCredenciales(CredencialesInvalidasException ex,
                                                             HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex,
                                                             HttpServletRequest request) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponse> manejarConflicto(ConflictoException ex,
                                                          HttpServletRequest request) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaNegocio(ReglaNegocioException ex,
                                                             HttpServletRequest request) {
        return construir(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarInesperado(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error interno, intente más tarde", request, null);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus estado, String mensaje,
                                                    HttpServletRequest request,
                                                    Map<String, String> campos) {
        ErrorResponse cuerpo = new ErrorResponse(
                LocalDateTime.now(),
                estado.value(),
                estado.getReasonPhrase(),
                mensaje,
                request.getRequestURI(),
                campos
        );
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
