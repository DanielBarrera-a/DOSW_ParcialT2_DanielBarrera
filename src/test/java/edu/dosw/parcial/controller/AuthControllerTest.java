package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.handlers.GlobalExceptionHandler;
import edu.dosw.parcial.core.exceptions.CredencialesInvalidasException;
import edu.dosw.parcial.core.services.AuthService;
import edu.dosw.parcial.core.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final String RUTA = "/api/v1/auth/login";

    @Mock
    private AuthService authService;
    @Mock
    private JwtUtil jwtUtil;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService, jwtUtil))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void iniciarSesion_conCredencialesCorrectas_responde200ConToken() throws Exception {
        // Arrange
        when(authService.iniciarSesion("cliente@foodhub.edu.co", "Clave123")).thenReturn("token-generado");
        when(jwtUtil.getExpiracionMs()).thenReturn(3_600_000L);

        // Act y Assert
        mockMvc.perform(post(RUTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"correo":"cliente@foodhub.edu.co","contrasena":"Clave123"}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-generado"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.expiraEnMs").value(3_600_000));
    }

    @Test
    void iniciarSesion_conCorreoInvalidoYContrasenaVacia_responde400ConLosCampos() throws Exception {
        // Act y Assert
        mockMvc.perform(post(RUTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"correo":"no-es-correo","contrasena":""}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.campos.correo").value("El correo no tiene un formato válido"))
                .andExpect(jsonPath("$.campos.contrasena").value("La contraseña es obligatoria"));

        verify(authService, never()).iniciarSesion(anyString(), anyString());
    }

    @Test
    void iniciarSesion_conCredencialesIncorrectas_responde401() throws Exception {
        // Arrange
        when(authService.iniciarSesion("cliente@foodhub.edu.co", "Mala"))
                .thenThrow(new CredencialesInvalidasException());

        // Act y Assert
        mockMvc.perform(post(RUTA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"correo":"cliente@foodhub.edu.co","contrasena":"Mala"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Correo o contraseña incorrectos"))
                .andExpect(jsonPath("$.ruta").value(RUTA));
    }

    @Test
    void iniciarSesion_sinCuerpo_responde400() throws Exception {
        // Act y Assert
        mockMvc.perform(post(RUTA).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El cuerpo de la petición no tiene un formato válido"));
    }
}
