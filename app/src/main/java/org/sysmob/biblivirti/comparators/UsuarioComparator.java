package org.sysmob.biblivirti.comparators;

import org.sysmob.biblivirti.model.Usuario;

import java.util.Comparator;

/**
 * Created by micro99 on 20/02/2017.
 */
public class UsuarioComparator implements Comparator<Usuario> {

    @Override
    public int compare(Usuario usuario1, Usuario usuario2) {
        return usuario1.getUsnid() == usuario1.getUsnid() ? 1 : 0;
    }
}
