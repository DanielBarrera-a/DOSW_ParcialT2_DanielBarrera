package edu.dosw.parcial.controller.handlers;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.core.exceptions.ApiException;
import edu.dosw.parcial.core.exceptions.CodigoError;
import edu.dosw.parcial.core.exceptions.ConflictoException;
import edu.dosw.parcial.core.exceptions.CredencialesInvalidasException;
import edu.dosw.parcial.core.exceptions.RecursoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.ReglaNegocioException;
import edu.dosw.parcial.core.exceptions.SolicitudInvalidaException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

        CodigoError codigo = codigoSegunObjetivo(ex, campos);
        String mensaje = switch (codigo) {
            case E01 -> "Datos de producto inválidos";
            case E06 -> "La cantidad de productos del pedido es inválida";
            default -> "Los datos enviados no son válidos";
        };

        log.warn("Petición rechazada por datos inválidos [{}] en {}: {}",
                codigo.getValor(), request.getRequestURI(), campos.keySet());
        return construir(HttpStatus.BAD_REQUEST, codigo, mensaje, request, campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoIlegible(HttpMessageNotReadableException ex,
                                                               HttpServletRequest request) {
        log.warn("Petición rechazada por cuerpo ilegible en {}", request.getRequestURI());
        return construir(HttpStatus.BAD_REQUEST, CodigoError.E11,
                "El cuerpo de la petición no tiene un formato válido", request, null);
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarSolicitudInvalida(SolicitudInvalidaException ex,
                                                                  HttpServletRequest request) {
        log.warn("Solicitud inválida [{}] en {}: {}", ex.getCodigoError(), request.getRequestURI(), ex.getMessage());
        return construir(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> manejarCredenciales(CredencialesInvalidasException ex,
                                                             HttpServletRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(RecursoNoEncontradoException ex,
                                                             HttpServletRequest request) {
        log.warn("Recurso no encontrado [{}] en {}: {}", ex.getCodigoError(), request.getRequestURI(), ex.getMessage());
        return construir(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponse> manejarConflicto(ConflictoException ex,
                                                          HttpServletRequest request) {
        log.warn("Conflicto [{}] en {}: {}", ex.getCodigoError(), request.getRequestURI(), ex.getMessage());
        return construir(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaNegocio(ReglaNegocioException ex,
                                                             HttpServletRequest request) {
        log.warn("Regla de negocio incumplida [{}] en {}: {}", ex.getCodigoError(), request.getRequestURI(), ex.getMessage());
        return construir(HttpStatus.UNPROCESSABLE_CONTENT, ex, request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRutaInexistente(NoResourceFoundException ex,
                                                                HttpServletRequest request) {
        log.warn("Ruta inexistente: {}", request.getRequestURI());
        return construir(HttpStatus.NOT_FOUND, CodigoError.E13,
                "El recurso solicitado no existe", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarInesperado(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, CodigoError.E99,
                "Ocurrió un error interno, intente más tarde", request, null);
    }

    private CodigoError codigoSegunObjetivo(MethodArgumentNotValidException ex, Map<String, String> campos) {
        Object objetivo = ex.getBindingResult().getTarget();
        String nombre = objetivo == null ? "" : objetivo.getClass().getSimpleName();
        if ("CrearProductoRequest".equals(nombre)) {
            return CodigoError.E01;
        }
        if ("CrearPedidoRequest".equals(nombre) && campos.containsKey("items")) {
            return CodigoError.E06;
        }
        return CodigoError.E11;
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus estado, ApiException ex,
                                                    HttpServletRequest request) {
        return construir(estado, ex.getCodigoError(), ex.getMessage(), request, null);
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus estado, CodigoError codigo, String mensaje,
                                                    HttpServletRequest request,
                                                    Map<String, String> campos) {
        ErrorResponse cuerpo = new ErrorResponse(
                LocalDateTime.now(),
                estado.value(),
                codigo == null ? null : codigo.getValor(),
                estado.getReasonPhrase(),
                mensaje,
                request.getRequestURI(),
                campos
        );
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
