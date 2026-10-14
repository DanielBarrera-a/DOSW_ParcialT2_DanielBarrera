package edu.dosw.parcial.controller;

import edu.dosw.parcial.core.models.Producto;
import edu.dosw.parcial.core.services.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(
            @RequestBody Producto producto) {
        try {
            Producto creado = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("mensaje", e.getMessage())
            );
        }
    }
}