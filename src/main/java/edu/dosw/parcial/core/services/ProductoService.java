package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.models.Producto;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.mappers.ProductoMapper;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    public ProductoService(ProductoRepository repository,
                           ProductoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Producto crearProducto(Producto producto) {
        validarProducto(producto);

        if (repository.existsByCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException(
                    "Ya existe un producto con ese código"
            );
        }

        producto.setActivo(true);
        ProductoEntity entity = mapper.toEntity(producto);
        ProductoEntity guardado = repository.save(entity);

        return mapper.toDomain(guardado);
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto es obligatorio"
            );
        }

        if (producto.getNombre() == null
                || producto.getNombre().trim().length() < 3
                || producto.getNombre().trim().length() > 60) {
            throw new IllegalArgumentException(
                    "El nombre debe tener entre 3 y 60 caracteres"
            );
        }

        if (!Float.isFinite(producto.getPrecio())
                || producto.getPrecio() <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero"
            );
        }

        if (producto.getCodigo() == null
                || producto.getCodigo().isBlank()) {
            throw new IllegalArgumentException(
                    "El código es obligatorio"
            );
        }
    }
}