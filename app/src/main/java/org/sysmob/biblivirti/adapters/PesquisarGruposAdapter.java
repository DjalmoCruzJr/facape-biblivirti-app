package org.sysmob.biblivirti.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.sysmob.biblivirti.fragments.PesquisarGruposFragment;
import org.sysmob.biblivirti.model.Grupo;

import java.util.List;

/**
 * Created by micro99 on 17/02/2017.
 */

public class PesquisarGruposAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENTS_COUNT = 3;

    private List<Grupo> gruposTodos;
    private List<Grupo> gruposMeus;
    private List<Grupo> gruposOutros;

    public PesquisarGruposAdapter(FragmentManager fm, List<Grupo> grupos) {
        super(fm);
        this.gruposTodos = grupos;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : new PesquisarGruposFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }
}
