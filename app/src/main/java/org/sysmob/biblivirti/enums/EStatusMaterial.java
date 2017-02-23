package org.sysmob.biblivirti.enums;

/**
 * Created by micro99 on 23/02/2017.
 */
public enum EStatusMaterial {

    ATIVO('A'),
    INATIVO('I');

    private char value;

    EStatusMaterial() {
    }

    EStatusMaterial(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
