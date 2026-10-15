package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.CodigoError;
import edu.dosw.parcial.core.exceptions.ConflictoException;
import edu.dosw.parcial.core.exceptions.RecursoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.ReglaNegocioException;
import edu.dosw.parcial.core.exceptions.SolicitudInvalidaException;
import edu.dosw.parcial.core.models.Entrega;
import edu.dosw.parcial.core.models.ExtraAplicado;
import edu.dosw.parcial.core.models.ItemPedido;
import edu.dosw.parcial.core.models.Pedido;
import edu.dosw.parcial.core.models.PedidoBuilder;
import edu.dosw.parcial.core.models.SolicitudItem;
import edu.dosw.parcial.core.models.SolicitudPedido;
import edu.dosw.parcial.core.models.TipoEntrega;
import edu.dosw.parcial.core.models.UsuarioAutenticado;
import edu.dosw.parcial.persistence.entities.ExtraEntity;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.mappers.PedidoMapper;
import edu.dosw.parcial.persistence.repositories.ExtraRepository;
import edu.dosw.parcial.persistence.repositories.PedidoRepository;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private static final double SUBTOTAL_MINIMO = 3500.0;
    private static final int MAXIMO_PRODUCTOS = 5;
    private static final Set<String> BLOQUES_HABILITADOS = Set.of("A", "B");

    private final ProductoRepository productoRepository;
    private final ExtraRepository extraRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoService(
            ProductoRepository productoRepository,
            ExtraRepository extraRepository,
            PedidoRepository pedidoRepository,
            PedidoMapper pedidoMapper) {

        this.productoRepository = productoRepository;
        this.extraRepository = extraRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public Pedido crearPedido(SolicitudPedido solicitud, UsuarioAutenticado cliente) {
        log.debug("Creando pedido para el cliente {}", cliente.id());

        validarForma(solicitud);
        List<ItemPedido> items = construirItems(solicitud.items());
        Entrega entrega = construirEntrega(solicitud);

        double subtotal = items.stream().mapToDouble(ItemPedido::getPrecioFinal).sum();
        if (subtotal < SUBTOTAL_MINIMO) {
            log.warn("Pedido rechazado (E4): subtotal {} bajo el mínimo", subtotal);
            throw new ReglaNegocioException(CodigoError.E4,
                    "El subtotal de $" + formatoPesos(subtotal)
                            + " está por debajo del mínimo de $" + formatoPesos(SUBTOTAL_MINIMO));
        }

        PedidoBuilder builder = new PedidoBuilder()
                .paraCliente(cliente)
                .conEntrega(entrega);
        items.forEach(builder::agregarItem);

        PedidoEntity guardado = pedidoRepository.save(pedidoMapper.toEntity(builder.build()));
        log.info("Pedido {} creado para el cliente {}: subtotal {}, total {}",
                guardado.getId(), cliente.id(), guardado.getSubtotal(), guardado.getTotal());
        return pedidoMapper.toDomain(guardado);
    }

    private void validarForma(SolicitudPedido solicitud) {
        if (solicitud.items() == null || solicitud.items().isEmpty()
                || solicitud.items().size() > MAXIMO_PRODUCTOS) {
            int cantidad = solicitud.items() == null ? 0 : solicitud.items().size();
            log.warn("Pedido rechazado (E06): cantidad de productos inválida ({})", cantidad);
            throw new SolicitudInvalidaException(CodigoError.E06,
                    cantidad == 0
                            ? "El pedido debe contener al menos un producto"
                            : "El pedido contiene " + cantidad
                            + " productos y supera el máximo permitido de " + MAXIMO_PRODUCTOS);
        }

        if (solicitud.tipoEntrega() == null) {
            throw new SolicitudInvalidaException(CodigoError.E11, "El tipo de entrega es obligatorio");
        }

        boolean conUbicacion = hayTexto(solicitud.bloque()) || hayTexto(solicitud.salon());
        if (solicitud.tipoEntrega() == TipoEntrega.ENTREGA_SALON
                && !(hayTexto(solicitud.bloque()) && hayTexto(solicitud.salon()))) {
            throw new SolicitudInvalidaException(CodigoError.E11,
                    "La entrega en salón requiere bloque y salón");
        }
        if (solicitud.tipoEntrega() != TipoEntrega.ENTREGA_SALON && conUbicacion) {
            throw new SolicitudInvalidaException(CodigoError.E11,
                    "La modalidad " + solicitud.tipoEntrega() + " no admite bloque ni salón");
        }
    }

    private List<ItemPedido> construirItems(List<SolicitudItem> solicitados) {
        List<ItemPedido> items = new ArrayList<>();
        Set<String> productosVistos = new HashSet<>();

        for (SolicitudItem solicitado : solicitados) {
            String codigo = solicitado.codigoProducto();

            if (!productosVistos.add(codigo)) {
                throw new SolicitudInvalidaException(CodigoError.E11,
                        "No se permiten productos repetidos en el pedido: " + codigo);
            }

            ProductoEntity producto = productoRepository.findByCodigo(codigo)
                    .orElseThrow(() -> {
                        log.warn("Pedido rechazado (E02): el producto {} no existe", codigo);
                        return new RecursoNoEncontradoException(CodigoError.E02,
                                "El producto " + codigo + " no existe");
                    });

            if (!producto.isActivo()) {
                log.warn("Pedido rechazado (E02): el producto {} no está activo", codigo);
                throw new ConflictoException(CodigoError.E02,
                        "El producto " + codigo + " no está activo");
            }

            items.add(new ItemPedido(producto.getCodigo(), producto.getNombre(),
                    producto.getPrecio(), construirExtras(solicitado.codigosExtras())));
        }
        return items;
    }

    private List<ExtraAplicado> construirExtras(List<String> codigos) {
        List<ExtraAplicado> extras = new ArrayList<>();
        List<String> inexistentes = new ArrayList<>();
        Set<String> vistos = new HashSet<>();

        if (codigos == null) {
            return extras;
        }

        for (String codigo : codigos) {
            if (!vistos.add(codigo)) {
                throw new SolicitudInvalidaException(CodigoError.E11,
                        "No se permite repetir el mismo extra: " + codigo);
            }
            ExtraEntity extra = extraRepository.findByCodigo(codigo).orElse(null);
            if (extra == null) {
                inexistentes.add(codigo);
                continue;
            }
            ExtraAplicado aplicado = new ExtraAplicado();
            aplicado.setCodigo(extra.getCodigo());
            aplicado.setNombre(extra.getNombre());
            aplicado.setPrecio(extra.getPrecio());
            extras.add(aplicado);
        }

        if (!inexistentes.isEmpty()) {
            log.warn("Pedido rechazado (E03): extras inexistentes {}", inexistentes);
            throw new RecursoNoEncontradoException(CodigoError.E03,
                    "Uno o más extras seleccionados no existen: " + String.join(", ", inexistentes));
        }
        return extras;
    }

    private Entrega construirEntrega(SolicitudPedido solicitud) {
        if (solicitud.tipoEntrega() != TipoEntrega.ENTREGA_SALON) {
            return new Entrega(solicitud.tipoEntrega(), null, null);
        }

        String bloque = solicitud.bloque().trim().toUpperCase(Locale.ROOT);
        if (!BLOQUES_HABILITADOS.contains(bloque)) {
            log.warn("Pedido rechazado (E5): bloque {} no habilitado", bloque);
            throw new ReglaNegocioException(CodigoError.E5,
                    "El bloque " + bloque + " no está habilitado para la modalidad ENTREGA_SALON");
        }
        return new Entrega(TipoEntrega.ENTREGA_SALON, bloque, solicitud.salon().trim());
    }

    private boolean hayTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private String formatoPesos(double valor) {
        return NumberFormat.getIntegerInstance(Locale.of("es", "CO")).format(valor);
    }
}
