package edu.dosw.parcial.core.exceptions;

public abstract class ApiException extends RuntimeException {

    private final CodigoError codigoError;

    protected ApiException(CodigoError codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public CodigoError getCodigoError() {
        return codigoError;
    }
}
