package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.ItemPedidoRequest;
import edu.dosw.parcial.core.models.*;
import edu.dosw.parcial.persistence.entities.ExtraEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.ExtraRepository;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PedidoService {

    private static final double SUBTOTAL_MINIMO = 3500.0;

    private final ProductoRepository productoRepository;
    private final ExtraRepository extraRepository;

    public PedidoService(
            ProductoRepository productoRepository,
            ExtraRepository extraRepository) {
        this.productoRepository = productoRepository;
        this.extraRepository = extraRepository;
    }

    public Pedido crearPedido(CrearPedidoRequest request, Usuario cliente) {
        validarSolicitud(request);

        List<ItemPedido> items = new ArrayList<>();
        Set<String> codigosProductos = new HashSet<>();

        for (ItemPedidoRequest itemRequest : request.getItems()) {
            String codigoProducto = itemRequest.getCodigoProducto();

            if (codigoProducto == null || codigoProducto.isBlank()) {
                throw new IllegalArgumentException(
                        "El código del producto es obligatorio.");
            }

            if (!codigosProductos.add(codigoProducto)) {
                throw new IllegalArgumentException(
                        "No se permiten productos repetidos en el pedido.");
            }

            ProductoEntity producto = productoRepository
                    .findByCodigo(codigoProducto)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No existe el producto: " + codigoProducto));

            if (!producto.isActivo()) {
                throw new IllegalArgumentException(
                        "El producto está inactivo: " + codigoProducto);
            }

            List<ExtraAplicado> extrasAplicados = new ArrayList<>();
            List<String> codigosExtras = itemRequest.getCodigosExtras();

            if (codigosExtras != null) {
                Set<String> extrasVistos = new HashSet<>();

                for (String codigoExtra : codigosExtras) {
                    if (codigoExtra == null || codigoExtra.isBlank()) {
                        throw new IllegalArgumentException(
                                "El código del extra es obligatorio.");
                    }

                    if (!extrasVistos.add(codigoExtra)) {
                        throw new IllegalArgumentException(
                                "No se permite repetir el mismo extra: "
                                        + codigoExtra);
                    }

                    ExtraEntity extra = extraRepository
                            .findByCodigo(codigoExtra)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "No existe el extra: " + codigoExtra));

                    ExtraAplicado aplicado = new ExtraAplicado();
                    aplicado.setCodigo(extra.getCodigo());
                    aplicado.setNombre(extra.getNombre());
                    aplicado.setPrecio(extra.getPrecio());

                    extrasAplicados.add(aplicado);
                }
            }

            ItemPedido item = new ItemPedido(
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    extrasAplicados
            );

            items.add(item);
        }

        validarEntrega(request);

        double subtotal = items.stream()
                .mapToDouble(ItemPedido::getPrecioFinal)
                .sum();

        if (subtotal < SUBTOTAL_MINIMO) {
            throw new IllegalArgumentException(
                    "El subtotal mínimo del pedido es $3500.");
        }

        Entrega entrega = new Entrega(
                request.getTipoEntrega(),
                request.getBloque(),
                request.getSalon()
        );

        PedidoBuilder builder = new PedidoBuilder()
                .paraCliente(cliente)
                .conEntrega(entrega);

        for (ItemPedido item : items) {
            builder.agregarItem(item);
        }

        return builder.build();
    }

    private void validarSolicitud(CrearPedidoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "La solicitud del pedido es obligatoria.");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido debe tener al menos un producto.");
        }

        if (request.getItems().size() > 5) {
            throw new IllegalArgumentException(
                    "El pedido permite máximo 5 productos diferentes.");
        }

        if (request.getTipoEntrega() == null) {
            throw new IllegalArgumentException(
                    "El tipo de entrega es obligatorio.");
        }

        if (request.getItems().stream().anyMatch(item -> item == null)) {
            throw new IllegalArgumentException(
                    "El pedido contiene un producto inválido.");
        }
    }

    private void validarEntrega(CrearPedidoRequest request) {
        TipoEntrega tipo = request.getTipoEntrega();

        if (tipo == TipoEntrega.CONSUMO_LOCAL) {
            if (noVacio(request.getBloque()) || noVacio(request.getSalon())) {
                throw new IllegalArgumentException(
                        "El consumo local no requiere bloque ni salón.");
            }
            return;
        }

        if (tipo == TipoEntrega.PARA_LLEVAR) {
            if (noVacio(request.getBloque()) || noVacio(request.getSalon())) {
                throw new IllegalArgumentException(
                        "Para llevar no requiere bloque ni salón.");
            }
            return;
        }

        if (tipo == TipoEntrega.ENTREGA_SALON) {
            String bloque = request.getBloque();
            String salon = request.getSalon();

            if (bloque == null || bloque.isBlank()
                    || salon == null || salon.isBlank()) {
                throw new IllegalArgumentException(
                        "La entrega en salón requiere bloque y salón.");
            }

            if (!bloque.equalsIgnoreCase("A")
                    && !bloque.equalsIgnoreCase("B")) {
                throw new IllegalArgumentException(
                        "La entrega en salón solo está disponible en los bloques A y B.");
            }
        }
    }

    private boolean noVacio(String valor) {
        return valor != null && !valor.isBlank();
    }
}