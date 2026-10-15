package edu.dosw.parcial.core.exceptions;

public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Correo o contraseña incorrectos");
    }
}
