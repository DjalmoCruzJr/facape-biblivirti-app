package org.sysmob.biblivirti.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.fragments.PesquisarMateriaisFragment;
import org.sysmob.biblivirti.model.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micro99 on 17/02/2017.
 */
public class PesquisarMateriaisPagerAdapter extends FragmentStatePagerAdapter {

    private static final int FRAGMENTS_COUNT = ETipoMaterial.values().length + 1;
    private static final int FRAGMENTS_TODOS = 0;
    private static final int FRAGMENTS_APRESENTACAO = 1;
    private static final int FRAGMENTS_EXERCICIO = 2;
    private static final int FRAGMENTS_FORMULA = 3;
    private static final int FRAGMENTS_JOGO = 4;
    private static final int FRAGMENTS_LIVRO = 5;
    private static final int FRAGMENTS_SIMULADO = 6;
    private static final int FRAGMENTS_VIDEO = 7;

    private static final String FRAGMENTS_TODOS_TITLE = "TODOS";

    private List<Material> apresentacoes;
    private List<Material> exercicios;
    private List<Material> formulas;
    private List<Material> jogos;
    private List<Material> livros;
    private List<Material> simulados;
    private List<Material> videos;
    private List<Material> todosMateriais;

    public PesquisarMateriaisPagerAdapter(FragmentManager fm, List<Material> materiais) {
        super(fm);
        this.todosMateriais = materiais;
        this.apresentacoes = null;
        this.exercicios = null;
        this.formulas = null;
        this.jogos = null;
        this.livros = null;
        this.simulados = null;
        this.videos = null;
        filterDatas();
    }

    @Override
    public Fragment getItem(int position) {
        PesquisarMateriaisFragment fragment = new PesquisarMateriaisFragment();
        switch (position) {
            case FRAGMENTS_TODOS:
                fragment.setMateriais(this.todosMateriais);
                break;
            case FRAGMENTS_APRESENTACAO:
                fragment.setMateriais(this.apresentacoes);
                break;
            case FRAGMENTS_EXERCICIO:
                fragment.setMateriais(this.exercicios);
                break;
            case FRAGMENTS_FORMULA:
                fragment.setMateriais(this.formulas);
                break;
            case FRAGMENTS_JOGO:
                fragment.setMateriais(this.jogos);
                break;
            case FRAGMENTS_LIVRO:
                fragment.setMateriais(this.livros);
                break;
            case FRAGMENTS_SIMULADO:
                fragment.setMateriais(this.simulados);
                break;
            case FRAGMENTS_VIDEO:
                fragment.setMateriais(this.videos);
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case FRAGMENTS_TODOS:
                pageTitle = FRAGMENTS_TODOS_TITLE;
                break;
            case FRAGMENTS_APRESENTACAO:
                pageTitle = ETipoMaterial.APRESENTACAO.name().toString();
                break;
            case FRAGMENTS_EXERCICIO:
                pageTitle = ETipoMaterial.EXERCICIO.name().toString();
                break;
            case FRAGMENTS_FORMULA:
                pageTitle = ETipoMaterial.FORMULA.name().toString();
                break;
            case FRAGMENTS_JOGO:
                pageTitle = ETipoMaterial.JOGO.name().toString();
                break;
            case FRAGMENTS_LIVRO:
                pageTitle = ETipoMaterial.LIVRO.name().toString();
                break;
            case FRAGMENTS_SIMULADO:
                pageTitle = ETipoMaterial.SIMULADO.name().toString();
                break;
            case FRAGMENTS_VIDEO:
                pageTitle = ETipoMaterial.VIDEO.name().toString();
                break;
        }
        return pageTitle;
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }

    private void filterDatas() {
        if (this.todosMateriais != null) {
            this.apresentacoes = new ArrayList<>();
            this.exercicios = new ArrayList<>();
            this.formulas = new ArrayList<>();
            this.jogos = new ArrayList<>();
            this.livros = new ArrayList<>();
            this.simulados = new ArrayList<>();
            this.videos = new ArrayList<>();

            for (Material m : this.todosMateriais) {
                if (m.getMactipo() == ETipoMaterial.APRESENTACAO) {
                    this.apresentacoes.add(m);
                } else if (m.getMactipo() == ETipoMaterial.EXERCICIO) {
                    this.exercicios.add(m);
                } else if (m.getMactipo() == ETipoMaterial.FORMULA) {
                    this.formulas.add(m);
                } else if (m.getMactipo() == ETipoMaterial.JOGO) {
                    this.jogos.add(m);
                } else if (m.getMactipo() == ETipoMaterial.LIVRO) {
                    this.livros.add(m);
                } else if (m.getMactipo() == ETipoMaterial.SIMULADO) {
                    this.simulados.add(m);
                } else if (m.getMactipo() == ETipoMaterial.VIDEO) {
                    this.videos.add(m);
                }
            }

            this.apresentacoes = this.apresentacoes.size() > 0 ? this.apresentacoes : null;
            this.exercicios = this.exercicios.size() > 0 ? this.exercicios : null;
            this.formulas = this.formulas.size() > 0 ? this.formulas : null;
            this.jogos = this.jogos.size() > 0 ? this.jogos : null;
            this.livros = this.livros.size() > 0 ? this.livros : null;
            this.simulados = this.simulados.size() > 0 ? this.simulados : null;
            this.videos = this.videos.size() > 0 ? this.videos : null;
        }
    }
}
