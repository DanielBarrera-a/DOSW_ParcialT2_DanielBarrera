package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.CredencialesInvalidasException;
import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.core.utils.JwtUtil;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.mappers.UsuarioMapper;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String CORREO = "cliente@foodhub.edu.co";
    private static final String HASH = "$2a$10$hash";

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    private AuthService authService;
    private UsuarioEntity entity;

    @BeforeEach
    void setUp() {
        authService = new AuthService(usuarioRepository, new UsuarioMapper(), passwordEncoder, jwtUtil);
        entity = new UsuarioEntity("u1", "Cliente", CORREO, HASH, Rol.CLIENTE);
    }

    @Test
    void iniciarSesion_conCredencialesCorrectas_devuelveToken() {
        // Arrange
        when(usuarioRepository.findByCorreo(CORREO)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("Clave123", HASH)).thenReturn(true);
        when(jwtUtil.generarToken(any(Usuario.class))).thenReturn("token-generado");

        // Act
        String token = authService.iniciarSesion(CORREO, "Clave123");

        // Assert
        assertEquals("token-generado", token);
    }

    @Test
    void iniciarSesion_conCorreoNoRegistrado_lanzaCredencialesInvalidas() {
        // Arrange
        when(usuarioRepository.findByCorreo("noexiste@foodhub.edu.co")).thenReturn(Optional.empty());

        // Act
        CredencialesInvalidasException ex = assertThrows(CredencialesInvalidasException.class,
                () -> authService.iniciarSesion("noexiste@foodhub.edu.co", "Clave123"));

        // Assert
        assertEquals("Correo o contraseña incorrectos", ex.getMessage());
        verify(jwtUtil, never()).generarToken(any());
    }

    @Test
    void iniciarSesion_conContrasenaIncorrecta_lanzaCredencialesInvalidas() {
        // Arrange
        when(usuarioRepository.findByCorreo(CORREO)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("Incorrecta", HASH)).thenReturn(false);

        // Act
        CredencialesInvalidasException ex = assertThrows(CredencialesInvalidasException.class,
                () -> authService.iniciarSesion(CORREO, "Incorrecta"));

        // Assert
        assertEquals("Correo o contraseña incorrectos", ex.getMessage());
        verify(jwtUtil, never()).generarToken(any());
    }
}
