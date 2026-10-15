package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.CredencialesInvalidasException;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.core.utils.JwtUtil;
import edu.dosw.parcial.persistence.mappers.UsuarioMapper;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                       UsuarioMapper usuarioMapper,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String iniciarSesion(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .map(usuarioMapper::toDomain)
                .orElseThrow(() -> {
                    log.warn("Inicio de sesión rechazado: correo no registrado");
                    return new CredencialesInvalidasException();
                });

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            log.warn("Inicio de sesión rechazado: contraseña incorrecta para el usuario {}",
                    usuario.getId());
            throw new CredencialesInvalidasException();
        }

        log.info("Inicio de sesión exitoso: usuario {} con rol {}",
                usuario.getId(), usuario.getRol());
        return jwtUtil.generarToken(usuario);
    }
}
