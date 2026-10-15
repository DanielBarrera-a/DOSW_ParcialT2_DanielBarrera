package edu.dosw.parcial.config;

import edu.dosw.parcial.persistence.entities.ExtraEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.ExtraRepository;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.catalogo.inicializar", havingValue = "true", matchIfMissing = true)
public class CatalogoInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogoInitializer.class);

    private final ProductoRepository productoRepository;
    private final ExtraRepository extraRepository;

    public CatalogoInitializer(ProductoRepository productoRepository,
                               ExtraRepository extraRepository) {
        this.productoRepository = productoRepository;
        this.extraRepository = extraRepository;
    }

    @Override
    public void run(String... args) {
        List.of(
                new ProductoEntity(null, "PROD-001", "Bandeja universitaria", 8500, true),
                new ProductoEntity(null, "PROD-002", "Sándwich", 5000, true),
                new ProductoEntity(null, "PROD-003", "Ensalada", 4500, true),
                new ProductoEntity(null, "PROD-004", "Sopa del día", 3500, true),
                new ProductoEntity(null, "PROD-005", "Jugo natural", 2500, true)
        ).forEach(this::crearProductoSiNoExiste);

        List.of(
                new ExtraEntity(null, "EXT-001", "Porción extra de proteína", 2000),
                new ExtraEntity(null, "EXT-002", "Adición de aguacate", 1500),
                new ExtraEntity(null, "EXT-003", "Cambio a pan integral", 500),
                new ExtraEntity(null, "EXT-004", "Adición de queso", 800),
                new ExtraEntity(null, "EXT-005", "Bebida adicional", 2000)
        ).forEach(this::crearExtraSiNoExiste);
    }

    private void crearProductoSiNoExiste(ProductoEntity producto) {
        if (!productoRepository.existsByCodigo(producto.getCodigo())) {
            productoRepository.save(producto);
            log.info("Producto base {} cargado en el catálogo", producto.getCodigo());
        }
    }

    private void crearExtraSiNoExiste(ExtraEntity extra) {
        if (!extraRepository.existsByCodigo(extra.getCodigo())) {
            extraRepository.save(extra);
            log.info("Extra {} cargado en el catálogo", extra.getCodigo());
        }
    }
}
