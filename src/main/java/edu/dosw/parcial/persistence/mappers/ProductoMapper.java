package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Producto;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoEntity toEntity(Producto producto) {
        ProductoEntity entity = new ProductoEntity();
        entity.setId(producto.getId());
        entity.setCodigo(producto.getCodigo());
        entity.setNombre(producto.getNombre());
        entity.setPrecio(producto.getPrecio());
        entity.setActivo(producto.isActivo());
        return entity;
    }

    public Producto toDomain(ProductoEntity entity) {
        Producto producto = new Producto(
                entity.getCodigo(),
                entity.getNombre(),
                entity.getPrecio(),
                entity.isActivo()
        );
        producto.setId(entity.getId());
        return producto;
    }
}
