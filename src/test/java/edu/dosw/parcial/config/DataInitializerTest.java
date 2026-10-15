package edu.dosw.parcial.config;

import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.mappers.UsuarioMapper;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    private static final String CORREO_ADMIN = "admin@foodhub.edu.co";
    private static final String CORREO_CLIENTE = "cliente@foodhub.edu.co";

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private DataInitializer crear(String contrasenaAdmin, String contrasenaCliente) {
        return new DataInitializer(usuarioRepository, new UsuarioMapper(), passwordEncoder,
                CORREO_ADMIN, contrasenaAdmin, CORREO_CLIENTE, contrasenaCliente);
    }

    @Test
    void run_conUsuariosNuevos_losGuardaConLaContrasenaCifrada() {
        // Arrange
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode("ClaveAdmin")).thenReturn("hash-admin");
        when(passwordEncoder.encode("ClaveCliente")).thenReturn("hash-cliente");
        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);

        // Act
        crear("ClaveAdmin", "ClaveCliente").run();

        // Assert
        verify(usuarioRepository, times(2)).save(captor.capture());
        List<UsuarioEntity> guardados = captor.getAllValues();
        assertEquals(Rol.ADMIN, guardados.get(0).getRol());
        assertEquals("hash-admin", guardados.get(0).getPasswordHash());
        assertEquals(Rol.CLIENTE, guardados.get(1).getRol());
        assertEquals("hash-cliente", guardados.get(1).getPasswordHash());
    }

    @Test
    void run_conUsuariosExistentes_noLosVuelveACrear() {
        // Arrange
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(true);

        // Act
        crear("ClaveAdmin", "ClaveCliente").run();

        // Assert
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void run_sinContrasenasDefinidas_noConsultaLaBaseDeDatos() {
        // Act
        crear("", null).run();

        // Assert
        verifyNoInteractions(usuarioRepository, passwordEncoder);
    }
}
