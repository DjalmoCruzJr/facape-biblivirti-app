package org.sysmob.biblivirti.enums;

public enum EUsuarioStatus {

    ATIVO('A'),
    INATIVO('I');

    private char value;

    EUsuarioStatus() {
    }

    EUsuarioStatus(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
