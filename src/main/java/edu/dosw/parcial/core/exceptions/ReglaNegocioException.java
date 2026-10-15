package edu.dosw.parcial.core.exceptions;

public class ReglaNegocioException extends ApiException {

    public ReglaNegocioException(String mensaje) {
        super(null, mensaje);
    }

    public ReglaNegocioException(CodigoError codigoError, String mensaje) {
        super(codigoError, mensaje);
    }
}
