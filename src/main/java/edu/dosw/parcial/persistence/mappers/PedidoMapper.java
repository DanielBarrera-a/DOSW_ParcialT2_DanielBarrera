package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Pedido;
import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.core.models.UsuarioAutenticado;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public PedidoEntity toEntity(Pedido pedido) {
        return new PedidoEntity(
                pedido.getId(),
                pedido.getCliente().id(),
                pedido.getCliente().correo(),
                pedido.getItems(),
                pedido.getEntrega(),
                pedido.getFechaDeCreacion(),
                pedido.getSubtotal(),
                pedido.getTotal(),
                pedido.getEstado()
        );
    }

    public Pedido toDomain(PedidoEntity entity) {
        return new Pedido(
                entity.getId(),
                new UsuarioAutenticado(entity.getClienteId(), entity.getCorreoCliente(), Rol.CLIENTE),
                entity.getItems(),
                entity.getEntrega(),
                entity.getFechaDeCreacion(),
                entity.getSubtotal(),
                entity.getTotal(),
                entity.getEstado()
        );
    }
}
