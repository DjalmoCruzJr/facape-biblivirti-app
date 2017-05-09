package org.sysmob.biblivirti.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.comparators.UsuarioComparatorByUsnid;
import org.sysmob.biblivirti.fragments.PesquisarGruposFragment;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by micro99 on 17/02/2017.
 */
public class PesquisarGruposPagerAdapter extends FragmentStatePagerAdapter {

    private static final int FRAGMENTS_COUNT = 3;
    private static final int FRAGMENT_GRUPOS_TODOS = 0;
    private static final int FRAGMENT_GRUPOS_SEUS = 1;
    private static final int FRAGMENT_GRUPOS_OUTROS = 2;
    private static final String TITLE_GRUPOS_TODOS = "Todos";
    private static final String TITLE_GRUPOS_SEUS = "Seus Grupos";
    private static final String TITLE_GRUPOS_OUTROS = "Outros Grupos";

    private List<Grupo> gruposTodos;
    private List<Grupo> gruposSeus;
    private List<Grupo> gruposOutros;
    private Map<Integer, String> pagesTitles;
    private Usuario loggedUser;


    public PesquisarGruposPagerAdapter(FragmentManager fm, List<Grupo> grupos) {
        super(fm);
        this.gruposTodos = grupos;
        this.gruposSeus = null;
        this.gruposOutros = null;
        this.loggedUser = BiblivirtiApplication.getInstance().getLoggedUser();
        filterDatas();
    }

    @Override
    public Fragment getItem(int position) {
        PesquisarGruposFragment fragment = new PesquisarGruposFragment();
        switch (position) {
            case FRAGMENT_GRUPOS_TODOS:
                fragment.setGrupos(gruposTodos);
                break;
            case FRAGMENT_GRUPOS_SEUS:
                fragment.setGrupos(gruposSeus);
                break;
            case FRAGMENT_GRUPOS_OUTROS:
                fragment.setGrupos(gruposOutros);
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case FRAGMENT_GRUPOS_TODOS:
                pageTitle = TITLE_GRUPOS_TODOS;
                break;
            case FRAGMENT_GRUPOS_SEUS:
                pageTitle = TITLE_GRUPOS_SEUS;
                break;
            case FRAGMENT_GRUPOS_OUTROS:
                pageTitle = TITLE_GRUPOS_OUTROS;
                break;
        }
        return pageTitle;
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }

    private void filterDatas() {
        if (this.gruposTodos != null) {
            this.gruposSeus = new ArrayList<>();
            this.gruposOutros = new ArrayList<>();

            for (Grupo g : this.gruposTodos) {
                // Verifica se o usuario logado eh admin do grupo
                if (Collections.binarySearch(g.getUsuarios(), this.loggedUser, new UsuarioComparatorByUsnid()) >= 0) {
                    this.gruposSeus.add(g);
                } else {
                    this.gruposOutros.add(g);
                }
            }

            this.gruposSeus = this.gruposSeus.size() > 0 ? this.gruposSeus : null;
            this.gruposOutros = this.gruposOutros.size() > 0 ? this.gruposOutros : null;
        }
    }
}
