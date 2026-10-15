package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.CrearProductoRequest;
import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.dtos.response.ProductoResponse;
import edu.dosw.parcial.controller.mappers.ProductoDtoMapper;
import edu.dosw.parcial.core.models.Producto;
import edu.dosw.parcial.core.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Gestión del catálogo (solo ADMIN)")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoDtoMapper mapper;

    public ProductoController(ProductoService productoService,
                              ProductoDtoMapper mapper) {
        this.productoService = productoService;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Crear producto",
            description = "Acceso: ADMIN. El código PROD-NNN lo genera el servidor.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "E01: datos de producto inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "E09: sin token válido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "E10: el rol no tiene permiso (CLIENTE)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "E07: ya existe un producto con ese nombre",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProductoResponse> crearProducto(
            @Valid @RequestBody CrearProductoRequest request) {
        Producto creado = productoService.crearProducto(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(creado));
    }
}
