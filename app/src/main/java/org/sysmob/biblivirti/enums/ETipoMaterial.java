package org.sysmob.biblivirti.enums;

/**
 * Created by micro99 on 23/02/2017.
 */
public enum ETipoMaterial {

    APRESENTACAO('A'),
    EXERCICIO('E'),
    FORMULA('F'),
    JOGO('J'),
    LIVRO('L'),
    SIMULADO('S'),
    VIDEO('V');

    private char value;

    ETipoMaterial() {
    }

    ETipoMaterial(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
