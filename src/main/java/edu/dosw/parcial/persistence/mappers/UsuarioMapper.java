package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getContrasena(),
                usuario.getRol()
        );
    }

    public Usuario toDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getCorreo(),
                entity.getPasswordHash(),
                entity.getRol()
        );
    }
}
