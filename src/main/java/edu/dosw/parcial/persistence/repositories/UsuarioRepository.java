package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository
        extends MongoRepository<UsuarioEntity, String> {

    Optional<UsuarioEntity> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}
