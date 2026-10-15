package edu.dosw.parcial.core.models;

import java.util.List;

public record SolicitudPedido(List<SolicitudItem> items,
                              TipoEntrega tipoEntrega,
                              String bloque,
                              String salon) {
}
