package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.ProductoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductoRepository
        extends MongoRepository<ProductoEntity, String> {

    Optional<ProductoEntity> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    boolean existsByNombreIgnoreCase(String nombre);

    Optional<ProductoEntity> findFirstByOrderByCodigoDesc();

}
