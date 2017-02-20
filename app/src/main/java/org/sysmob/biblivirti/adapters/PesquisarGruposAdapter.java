package org.sysmob.biblivirti.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.fragments.PesquisarGruposFragment;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micro99 on 17/02/2017.
 */

public class PesquisarGruposAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENTS_COUNT = 3;

    private List<Grupo> gruposTodos;
    private List<Grupo> gruposMeus;
    private List<Grupo> gruposOutros;
    private Usuario loggedUser;

    public PesquisarGruposAdapter(FragmentManager fm, List<Grupo> grupos) {
        super(fm);
        this.gruposTodos = grupos;
        this.gruposMeus = new ArrayList<>();
        this.gruposOutros = new ArrayList<>();
        this.loggedUser = BiblivirtiApplication.getInstance().getLoggedUser();
        filterDatas();
    }

    @Override
    public Fragment getItem(int position) {
        PesquisarGruposFragment fragment = new PesquisarGruposFragment();
        switch (position) {
            case 0:
                fragment.setGrupos(gruposTodos);
                break;
            case 1:
                fragment.setGrupos(gruposMeus);
                break;
            case 2:
                fragment.setGrupos(gruposOutros);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }

    private void filterDatas() {
        if (this.gruposTodos != null) {
            for (Grupo g : this.gruposTodos) {
                // Verifica se o usuario logado eh admin do grupo
                if (g.getAdmin().getUsnid() == this.loggedUser.getUsnid()) {
                    this.gruposMeus.add(g);
                } else {
                    this.gruposOutros.add(g);
                }
            }
        }
    }
}
