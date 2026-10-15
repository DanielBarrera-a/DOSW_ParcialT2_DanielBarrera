package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = new UsuarioMapper();

    @Test
    void toEntity_copiaTodosLosDatosDelUsuario() {
        // Arrange
        Usuario usuario = new Usuario("u1", "Admin", "admin@foodhub.edu.co", "hash", Rol.ADMIN);

        // Act
        UsuarioEntity entity = mapper.toEntity(usuario);

        // Assert
        assertEquals("u1", entity.getId());
        assertEquals("Admin", entity.getNombre());
        assertEquals("admin@foodhub.edu.co", entity.getCorreo());
        assertEquals("hash", entity.getPasswordHash());
        assertEquals(Rol.ADMIN, entity.getRol());
    }

    @Test
    void toDomain_copiaTodosLosDatosDelDocumento() {
        // Arrange
        UsuarioEntity entity = new UsuarioEntity("u2", "Cliente", "cliente@foodhub.edu.co", "hash", Rol.CLIENTE);

        // Act
        Usuario usuario = mapper.toDomain(entity);

        // Assert
        assertEquals("u2", usuario.getId());
        assertEquals("Cliente", usuario.getNombre());
        assertEquals("cliente@foodhub.edu.co", usuario.getCorreo());
        assertEquals("hash", usuario.getContrasena());
        assertEquals(Rol.CLIENTE, usuario.getRol());
    }
}
