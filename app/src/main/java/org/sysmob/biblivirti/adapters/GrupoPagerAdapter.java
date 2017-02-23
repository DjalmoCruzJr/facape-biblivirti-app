package org.sysmob.biblivirti.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by micro99 on 23/02/2017.
 */

public class GrupoPagerAdapter extends FragmentStatePagerAdapter {

    private static final int FRAGMENTS_COUNT = 5;

    public GrupoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0: // Fragment de Materiais
                break;
            case 1: // Fragment de Conteudos
                break;
            case 2: // Fragment de Membros
                break;
            case 3: // Fragment de Duvidas
                break;
            case 4: // Fragment de Mensagens
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }
}
