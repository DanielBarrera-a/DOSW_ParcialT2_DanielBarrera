package edu.dosw.parcial.config;

import edu.dosw.parcial.core.models.Rol;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.persistence.mappers.UsuarioMapper;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final String correoAdmin;
    private final String contrasenaAdmin;
    private final String correoCliente;
    private final String contrasenaCliente;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           UsuarioMapper usuarioMapper,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.usuarios.admin.correo}") String correoAdmin,
                           @Value("${app.usuarios.admin.contrasena:}") String contrasenaAdmin,
                           @Value("${app.usuarios.cliente.correo}") String correoCliente,
                           @Value("${app.usuarios.cliente.contrasena:}") String contrasenaCliente) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.correoAdmin = correoAdmin;
        this.contrasenaAdmin = contrasenaAdmin;
        this.correoCliente = correoCliente;
        this.contrasenaCliente = contrasenaCliente;
    }

    @Override
    public void run(String... args) {
        crearSiNoExiste("Administrador cafetería", correoAdmin, contrasenaAdmin, Rol.ADMIN);
        crearSiNoExiste("Cliente de prueba", correoCliente, contrasenaCliente, Rol.CLIENTE);
    }

    private void crearSiNoExiste(String nombre, String correo, String contrasena, Rol rol) {
        if (contrasena == null || contrasena.isBlank()) {
            log.warn("No se creó el usuario {} porque no se definió su contraseña", rol);
            return;
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            log.info("El usuario {} ya existe en la base de datos", rol);
            return;
        }

        Usuario usuario = new Usuario(null, nombre, correo, passwordEncoder.encode(contrasena), rol);
        usuarioRepository.save(usuarioMapper.toEntity(usuario));
        log.info("Usuario {} creado en la base de datos", rol);
    }
}
