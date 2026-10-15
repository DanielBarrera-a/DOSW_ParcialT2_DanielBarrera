package edu.dosw.parcial.core.exceptions;

public class ConflictoException extends ApiException {

    public ConflictoException(String mensaje) {
        super(null, mensaje);
    }

    public ConflictoException(CodigoError codigoError, String mensaje) {
        super(codigoError, mensaje);
    }
}
