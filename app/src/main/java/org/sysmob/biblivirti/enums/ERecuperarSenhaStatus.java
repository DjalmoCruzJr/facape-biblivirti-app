package org.sysmob.biblivirti.enums;

/**
 * Created by djalmocruzjr on 16/01/2017.
 */
public enum ERecuperarSenhaStatus {

    ATIVO('A'),
    INATIVO('I');

    private char value;

    ERecuperarSenhaStatus() {
    }

    ERecuperarSenhaStatus(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
