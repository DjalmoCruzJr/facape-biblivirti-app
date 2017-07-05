package org.sysmob.biblivirti.comparators;

import org.sysmob.biblivirti.model.Conteudo;

import java.util.Comparator;

/**
 * Created by djalmocruzjr on 09/05/2017.
 */

public class ConteudoComparatorByUsnid implements Comparator<Conteudo> {
    @Override
    public int compare(Conteudo c1, Conteudo c2) {
        return Integer.compare(c1.getConid(), c2.getConid());
    }
}
