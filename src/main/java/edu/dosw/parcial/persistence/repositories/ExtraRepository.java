package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.ExtraEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ExtraRepository
        extends MongoRepository<ExtraEntity, String> {

    Optional<ExtraEntity> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
