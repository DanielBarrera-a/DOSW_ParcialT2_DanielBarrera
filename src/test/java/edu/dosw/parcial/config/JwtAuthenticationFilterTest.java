package edu.dosw.parcial.config;

import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.core.models.UsuarioAutenticado;
import edu.dosw.parcial.core.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_conTokenValido_autenticaAlUsuarioConSuRol() throws Exception {
        // Arrange
        JwtAuthenticationFilter filtro = new JwtAuthenticationFilter(jwtUtil);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/pedidos/mis-pedidos");
        request.addHeader("Authorization", "Bearer token-valido");
        UsuarioAutenticado usuario = new UsuarioAutenticado("u1", "cliente@foodhub.edu.co", Rol.CLIENTE);
        when(jwtUtil.leerToken("token-valido")).thenReturn(usuario);
        MockFilterChain cadena = new MockFilterChain();

        // Act
        filtro.doFilter(request, new MockHttpServletResponse(), cadena);

        // Assert
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(autenticacion);
        assertEquals(usuario, autenticacion.getPrincipal());
        assertEquals("ROLE_CLIENTE", autenticacion.getAuthorities().iterator().next().getAuthority());
        assertNotNull(cadena.getRequest());
    }

    @Test
    void doFilter_sinEncabezado_noAutenticaYContinua() throws Exception {
        // Arrange
        JwtAuthenticationFilter filtro = new JwtAuthenticationFilter(jwtUtil);
        MockFilterChain cadena = new MockFilterChain();

        // Act
        filtro.doFilter(new MockHttpServletRequest("GET", "/api/v1/pedidos"), new MockHttpServletResponse(), cadena);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(cadena.getRequest());
        verify(jwtUtil, never()).leerToken(anyString());
    }

    @Test
    void doFilter_conEncabezadoSinBearer_noLeeElToken() throws Exception {
        // Arrange
        JwtAuthenticationFilter filtro = new JwtAuthenticationFilter(jwtUtil);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/pedidos");
        request.addHeader("Authorization", "Basic abc123");

        // Act
        filtro.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, never()).leerToken(anyString());
    }

    @Test
    void doFilter_conTokenInvalido_noAutenticaYContinua() throws Exception {
        // Arrange
        JwtAuthenticationFilter filtro = new JwtAuthenticationFilter(jwtUtil);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/pedidos");
        request.addHeader("Authorization", "Bearer token-alterado");
        when(jwtUtil.leerToken("token-alterado")).thenThrow(new JwtException("firma inválida"));
        MockFilterChain cadena = new MockFilterChain();

        // Act
        filtro.doFilter(request, new MockHttpServletResponse(), cadena);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(cadena.getRequest());
    }
}
