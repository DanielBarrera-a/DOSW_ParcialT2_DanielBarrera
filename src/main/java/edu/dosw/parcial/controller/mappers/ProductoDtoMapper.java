package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.CrearProductoRequest;
import edu.dosw.parcial.controller.dtos.response.ProductoResponse;
import edu.dosw.parcial.core.models.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoDtoMapper {

    public Producto toDomain(CrearProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre().trim());
        producto.setPrecio(request.precio());
        producto.setActivo(request.activo() == null || request.activo());
        return producto;
    }

    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.isActivo());
    }
}
