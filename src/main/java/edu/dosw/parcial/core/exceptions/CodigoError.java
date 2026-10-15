package edu.dosw.parcial.core.exceptions;

public enum CodigoError {
    E01("E01"),
    E02("E02"),
    E03("E03"),
    E4("E4"),
    E5("E5"),
    E06("E06"),
    E07("E07"),
    E09("E09"),
    E10("E10"),
    E11("E11"),
    E12("E12"),
    E13("E13"),
    E99("E99");

    private final String valor;

    CodigoError(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
