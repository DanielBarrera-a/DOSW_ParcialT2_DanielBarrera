package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.controller.mappers.PedidoDtoMapper;
import edu.dosw.parcial.core.models.Pedido;
import edu.dosw.parcial.core.models.UsuarioAutenticado;
import edu.dosw.parcial.core.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Pedidos de comida de la cafetería")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoDtoMapper mapper;

    public PedidoController(PedidoService pedidoService, PedidoDtoMapper mapper) {
        this.pedidoService = pedidoService;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Hacer un pedido",
            description = "Acceso: CLIENTE. El cliente se obtiene del token y el total lo calcula el servidor. "
                    + "El pedido nace en estado CONFIRMADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "E14: pedido creado"),
            @ApiResponse(responseCode = "400", description = "E06 / E11: solicitud inválida (por ejemplo, más de 5 productos)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "E09: sin token válido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "E10: el rol no tiene permiso (ADMIN)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "E02 / E03: producto o extra inexistente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "E02: producto inactivo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "E4 (subtotal bajo el mínimo) / E5 (bloque no habilitado)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PedidoResponse> crearPedido(
            @Valid @RequestBody CrearPedidoRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado cliente) {
        Pedido creado = pedidoService.crearPedido(mapper.toSolicitud(request), cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }
}
