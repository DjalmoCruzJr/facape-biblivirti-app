package org.sysmob.biblivirti.enums;

/**
 * Created by djalmocruzjr on 30/01/2017.
 */
public enum ETipoGrupo {
    ABERTO('A'),
    FECHADO('F');

    private char value;

    ETipoGrupo() {
    }

    ETipoGrupo(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
