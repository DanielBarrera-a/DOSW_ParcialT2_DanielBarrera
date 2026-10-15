package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.core.services.AuthService;
import edu.dosw.parcial.core.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Inicio de sesión de administradores y clientes")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Iniciar sesión",
            description = "Recibe correo y contraseña y devuelve un token JWT para usar en el botón Authorize")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión iniciada, se devuelve el token"),
            @ApiResponse(responseCode = "400", description = "Correo o contraseña vacíos o con formato inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoginResponse> iniciarSesion(@Valid @RequestBody LoginRequest request) {
        String token = authService.iniciarSesion(request.correo(), request.contrasena());
        return ResponseEntity.ok(new LoginResponse(token, "Bearer", jwtUtil.getExpiracionMs()));
    }
}
