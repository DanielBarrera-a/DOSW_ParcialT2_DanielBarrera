package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.CodigoError;
import edu.dosw.parcial.core.exceptions.ConflictoException;
import edu.dosw.parcial.core.models.Producto;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.mappers.ProductoMapper;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);
    private static final String PREFIJO_CODIGO = "PROD-";

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    public ProductoService(ProductoRepository repository,
                           ProductoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Producto crearProducto(Producto producto) {
        log.debug("Creando producto con nombre '{}'", producto.getNombre());

        if (repository.existsByNombreIgnoreCase(producto.getNombre())) {
            log.warn("Producto duplicado: ya existe '{}'", producto.getNombre());
            throw new ConflictoException(CodigoError.E07,
                    "Ya existe un producto con el nombre indicado");
        }

        producto.setCodigo(generarCodigo());
        ProductoEntity guardado = repository.save(mapper.toEntity(producto));

        log.info("Producto creado: {} ({})", guardado.getCodigo(), guardado.getNombre());
        return mapper.toDomain(guardado);
    }

    private String generarCodigo() {
        int siguiente = repository.findFirstByOrderByCodigoDesc()
                .map(ProductoEntity::getCodigo)
                .map(codigo -> codigo.substring(PREFIJO_CODIGO.length()))
                .map(Integer::parseInt)
                .orElse(0) + 1;
        return String.format("%s%03d", PREFIJO_CODIGO, siguiente);
    }
}
