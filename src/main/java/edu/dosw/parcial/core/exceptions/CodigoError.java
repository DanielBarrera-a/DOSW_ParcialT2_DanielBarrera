package edu.dosw.parcial.core.exceptions;

public enum CodigoError {
    E01("E01"),   // Datos de producto inválidos
    E02("E02"),   // Producto inexistente o inactivo
    E03("E03"),   // Extra inexistente
    E4("E4"),     // Subtotal por debajo del mínimo
    E5("E5"),     // Bloque no habilitado para ENTREGA_SALON
    E06("E06"),   // Cantidad de productos inválida
    E07("E07"),   // Producto duplicado
    E09("E09"),   // Sin autenticación
    E10("E10"),   // Sin permisos
    E11("E11"),   // Solicitud inválida (formato, parámetros)
    E12("E12"),   // Credenciales inválidas
    E13("E13"),   // Ruta inexistente
    E99("E99");   // Error interno

    private final String valor;

    CodigoError(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
