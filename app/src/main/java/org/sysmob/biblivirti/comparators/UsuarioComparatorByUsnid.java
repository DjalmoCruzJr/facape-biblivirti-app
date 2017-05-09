package org.sysmob.biblivirti.comparators;

import org.sysmob.biblivirti.model.Usuario;

import java.util.Comparator;

/**
 * Created by djalmocruzjr on 09/05/2017.
 */

public class UsuarioComparatorByUsnid implements Comparator<Usuario> {
    @Override
    public int compare(Usuario u1, Usuario u2) {
        return Integer.compare(u1.getUsnid(), u2.getUsnid());
    }
}
