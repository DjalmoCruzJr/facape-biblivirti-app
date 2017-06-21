package org.sysmob.biblivirti.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.sysmob.biblivirti.fragments.ConteudosFragment;
import org.sysmob.biblivirti.fragments.DuvidasFragment;
import org.sysmob.biblivirti.fragments.MateriaisFragment;
import org.sysmob.biblivirti.fragments.MembrosFragment;
import org.sysmob.biblivirti.fragments.MensagensFragment;

/**
 * Created by micro99 on 23/02/2017.
 */

public class GrupoPagerAdapter extends FragmentStatePagerAdapter {

    private static final int FRAGMENTS_COUNT = 5;

    public static final int FRAGMENT_MATERIAIS = 0;
    public static final int FRAGMENT_CONTEUDOS = 1;
    public static final int FRAGMENT_MEMBROS = 2;
    public static final int FRAGMENT_DUVIDAS = 3;
    public static final int FRAGMENT_MENSAGENS = 4;


    public GrupoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case FRAGMENT_MATERIAIS: // Fragment de Materiais
                fragment = new MateriaisFragment();
                break;
            case FRAGMENT_CONTEUDOS: // Fragment de Conteudos
                fragment = new ConteudosFragment();
                break;
            case FRAGMENT_MEMBROS: // Fragment de Membros
                fragment = new MembrosFragment();
                break;
            case FRAGMENT_DUVIDAS: // Fragment de Duvidas
                fragment = new DuvidasFragment();
                break;
            case FRAGMENT_MENSAGENS: // Fragment de Mensagens
                fragment = new MensagensFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }
}
