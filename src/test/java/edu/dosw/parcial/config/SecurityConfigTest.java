package edu.dosw.parcial.config;

import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.core.utils.JwtUtil;
import edu.dosw.parcial.main.ParcialApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ParcialApplication.class,
        properties = {"jwt.secret=dGVzdHNlY3JldHRlc3RzZWNyZXR0ZXN0c2VjcmV0dGVzdHNlY3JldA==",
                "app.catalogo.inicializar=false",
                "spring.data.mongodb.auto-index-creation=false"})
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private JwtUtil jwtUtil;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private String tokenDe(Rol rol) {
        return "Bearer " + jwtUtil.generarToken(new Usuario("u1", "Prueba", "prueba@foodhub.edu.co", "hash", rol));
    }

    @Test
    void endpointProtegido_sinToken_responde401() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Debe iniciar sesión y enviar un token válido"));
    }

    @Test
    void endpointProtegido_conTokenAlterado_responde401() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos").header("Authorization", "Bearer token-alterado"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listarTodosLosPedidos_conTokenDeCliente_responde403() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos").header("Authorization", tokenDe(Rol.CLIENTE)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensaje").value("No tiene permisos para realizar esta operación"));
    }

    @Test
    void crearProducto_conTokenDeCliente_responde403() throws Exception {
        mockMvc.perform(post("/api/v1/productos").header("Authorization", tokenDe(Rol.CLIENTE))
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void hacerPedido_conTokenDeAdmin_responde403() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos").header("Authorization", tokenDe(Rol.ADMIN))
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void login_esPublico_yValidaLosDatosDeEntrada() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void health_esPublico() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk());
    }
}
