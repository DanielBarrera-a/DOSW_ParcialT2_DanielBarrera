package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.CrearPedidoRequest;
import edu.dosw.parcial.core.models.Pedido;
import edu.dosw.parcial.core.services.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestBody CrearPedidoRequest request) {
        try {
            /*
             * Temporalmente no se puede obtener el cliente real,
             * porque todavía falta implementar el login y el JWT.
             * No se debe aceptar la identidad del cliente desde el JSON.
             */
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "mensaje",
                            "Debes iniciar sesión para crear un pedido."
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}