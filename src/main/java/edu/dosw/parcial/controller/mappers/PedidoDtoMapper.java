package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.models.Pedido;
import edu.dosw.parcial.core.models.SolicitudItem;
import edu.dosw.parcial.core.models.SolicitudPedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoDtoMapper {

    public SolicitudPedido toSolicitud(CrearPedidoRequest request) {
        List<SolicitudItem> items = request.items().stream()
                .map(this::toSolicitudItem)
                .toList();
        return new SolicitudPedido(items, request.entrega().tipo(),
                request.entrega().bloque(), request.entrega().salon());
    }

    public PedidoResponse toResponse(Pedido pedido) {
        List<PedidoResponse.ItemResponse> items = pedido.getItems().stream()
                .map(item -> new PedidoResponse.ItemResponse(
                        item.getCodigoProducto(),
                        item.getNombreProducto(),
                        item.getPrecioBase(),
                        item.getExtras().stream()
                                .map(e -> new PedidoResponse.ExtraResponse(e.getCodigo(), e.getNombre(), e.getPrecio()))
                                .toList(),
                        item.getPrecioFinal()))
                .toList();

        double costoEntrega = pedido.getTotal() - pedido.getSubtotal();
        PedidoResponse.EntregaResponse entrega = new PedidoResponse.EntregaResponse(
                pedido.getEntrega().getTipo().name(),
                pedido.getEntrega().getBloque(),
                pedido.getEntrega().getSalon(),
                costoEntrega);

        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().correo(),
                pedido.getFechaDeCreacion().toString(),
                pedido.getEstado().name(),
                items,
                entrega,
                pedido.getSubtotal(),
                pedido.getTotal());
    }

    private SolicitudItem toSolicitudItem(ItemPedidoRequest item) {
        return new SolicitudItem(item.codigoProducto(),
                item.extras() == null ? List.of() : item.extras());
    }
}
