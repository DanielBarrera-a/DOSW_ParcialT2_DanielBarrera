package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.PedidoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PedidoRepository
        extends MongoRepository<PedidoEntity, String> {

    List<PedidoEntity> findByClienteId(String clienteId);
}
