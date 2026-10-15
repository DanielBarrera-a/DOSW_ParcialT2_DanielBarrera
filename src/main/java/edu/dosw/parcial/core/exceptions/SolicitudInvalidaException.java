package edu.dosw.parcial.core.exceptions;

public class SolicitudInvalidaException extends ApiException {

    public SolicitudInvalidaException(String mensaje) {
        super(null, mensaje);
    }

    public SolicitudInvalidaException(CodigoError codigoError, String mensaje) {
        super(codigoError, mensaje);
    }
}
