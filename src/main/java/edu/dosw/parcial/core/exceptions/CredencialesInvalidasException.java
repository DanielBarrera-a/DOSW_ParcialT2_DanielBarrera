package edu.dosw.parcial.core.exceptions;

public class CredencialesInvalidasException extends ApiException {

    public CredencialesInvalidasException() {
        super(CodigoError.E12, "Correo o contraseña incorrectos");
    }
}
