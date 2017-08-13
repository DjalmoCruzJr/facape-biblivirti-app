package org.sysmob.biblivirti.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.sysmob.biblivirti.comparators.UsuarioComparatorByUsnid;
import org.sysmob.biblivirti.fragments.PesquisarUsuariosFragment;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by micro99 on 17/02/2017.
 */
public class PesquisarUsuariosPagerAdapter extends FragmentStatePagerAdapter {

    private static final int FRAGMENTS_COUNT = 3;
    private static final int FRAGMENT_TODOS = 0;
    private static final int FRAGMENT_MEMBROS = 1;
    private static final int FRAGMENT_NAO_MEMBROS = 2;
    private static final String TITLE_TODOS = "Todos";
    private static final String TITLE_MEMBROS = "Membros";
    private static final String TITLE_NAO_MEMBROS = "Não Membros";

    private List<Usuario> listTodos;
    private List<Usuario> listMembros;
    private List<Usuario> listNaoMembros;
    private Map<Integer, String> pagesTitles;
    private Grupo grupo;

    public PesquisarUsuariosPagerAdapter(FragmentManager fm, List<Usuario> usuarios, Grupo grupo) {
        super(fm);
        this.listTodos = usuarios;
        this.listMembros = null;
        this.listNaoMembros = null;
        this.grupo = grupo;
        filterDatas();
    }

    @Override
    public Fragment getItem(int position) {
        PesquisarUsuariosFragment fragment = new PesquisarUsuariosFragment();
        switch (position) {
            case FRAGMENT_TODOS:
                fragment.setUsuarios(listTodos);
                break;
            case FRAGMENT_MEMBROS:
                fragment.setUsuarios(listMembros);
                break;
            case FRAGMENT_NAO_MEMBROS:
                fragment.setUsuarios(listNaoMembros);
                break;
        }
        fragment.setGrupo(this.grupo);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case FRAGMENT_TODOS:
                pageTitle = TITLE_TODOS;
                break;
            case FRAGMENT_MEMBROS:
                pageTitle = TITLE_MEMBROS;
                break;
            case FRAGMENT_NAO_MEMBROS:
                pageTitle = TITLE_NAO_MEMBROS;
                break;
        }
        return pageTitle;
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }

    private void filterDatas() {
        if (this.listTodos != null) {
            this.listMembros = new ArrayList<>();
            this.listNaoMembros = new ArrayList<>();

            for (Usuario u : this.listTodos) {
                // Verifica se o usuario logado eh admin do grupo
                if (Collections.binarySearch(this.grupo.getUsuarios(), u, new UsuarioComparatorByUsnid()) >= 0) {
                    this.listMembros.add(u);
                } else {
                    this.listNaoMembros.add(u);
                }
            }

            this.listMembros = this.listMembros.size() > 0 ? this.listMembros : null;
            this.listNaoMembros = this.listNaoMembros.size() > 0 ? this.listNaoMembros : null;
        }
    }
}
