package edu.dosw.parcial.core.utils;

import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.core.models.UsuarioAutenticado;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private static final String SECRETO = "dGVzdHNlY3JldHRlc3RzZWNyZXR0ZXN0c2VjcmV0dGVzdHNlY3JldA==";
    private static final String OTRO_SECRETO = "b3Ryb3NlY3JldG90cm9zZWNyZXRvdHJvc2VjcmV0b3Ryb3NlY3JldA==";

    private final Usuario usuario = new Usuario("u1", "Admin", "admin@foodhub.edu.co", "hash", Rol.ADMIN);

    @Test
    void leerToken_deUnTokenGenerado_devuelveLosDatosDelUsuario() {
        // Arrange
        JwtUtil jwtUtil = new JwtUtil(SECRETO, 3_600_000);
        String token = jwtUtil.generarToken(usuario);

        // Act
        UsuarioAutenticado leido = jwtUtil.leerToken(token);

        // Assert
        assertEquals("u1", leido.id());
        assertEquals("admin@foodhub.edu.co", leido.correo());
        assertEquals(Rol.ADMIN, leido.rol());
        assertEquals(3_600_000, jwtUtil.getExpiracionMs());
    }

    @Test
    void leerToken_conTokenVencido_lanzaExpiredJwtException() {
        // Arrange
        JwtUtil jwtUtil = new JwtUtil(SECRETO, -1_000);
        String token = jwtUtil.generarToken(usuario);

        // Act y Assert
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.leerToken(token));
    }

    @Test
    void leerToken_firmadoConOtraClave_lanzaJwtException() {
        // Arrange
        String tokenAjeno = new JwtUtil(OTRO_SECRETO, 3_600_000).generarToken(usuario);
        JwtUtil jwtUtil = new JwtUtil(SECRETO, 3_600_000);

        // Act y Assert
        assertThrows(JwtException.class, () -> jwtUtil.leerToken(tokenAjeno));
    }

    @Test
    void leerToken_conTextoQueNoEsToken_lanzaJwtException() {
        // Arrange
        JwtUtil jwtUtil = new JwtUtil(SECRETO, 3_600_000);

        // Act y Assert
        assertThrows(JwtException.class, () -> jwtUtil.leerToken("esto-no-es-un-token"));
    }
}
