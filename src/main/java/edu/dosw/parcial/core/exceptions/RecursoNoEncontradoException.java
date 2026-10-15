package edu.dosw.parcial.core.exceptions;

public class RecursoNoEncontradoException extends ApiException {

    public RecursoNoEncontradoException(String mensaje) {
        super(null, mensaje);
    }

    public RecursoNoEncontradoException(CodigoError codigoError, String mensaje) {
        super(codigoError, mensaje);
    }
}
