package org.sysmob.biblivirti.activities;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.GrupoPagerAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.model.Grupo;

public class GrupoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Grupo grupo;
    private TypedArray tabsImagesAtivo;
    private TypedArray tabsImagesInativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadWidgets() {
        if (getIntent().getExtras() != null) {
            this.grupo = (Grupo) getIntent().getExtras().getSerializable(Grupo.KEY_GRUPO);
        }

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(this.grupo.getGrcnome().toString());

        this.tabsImagesAtivo = getResources().obtainTypedArray(R.array.activity_grupo_tabs_images_ativo);
        this.tabsImagesInativo = getResources().obtainTypedArray(R.array.activity_grupo_tabs_images_inativo);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.viewPager.setAdapter(new GrupoPagerAdapter(getSupportFragmentManager()));

        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.tabLayout.setupWithViewPager(this.viewPager);
        // Seta os icones para cada tab
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // Verifica se a tab corrente esta selecionada
            if (tabLayout.getSelectedTabPosition() == i) {
                tabLayout.getTabAt(i).setIcon(this.tabsImagesAtivo.getResourceId(i, 0));
            } else {
                tabLayout.getTabAt(i).setIcon(this.tabsImagesInativo.getResourceId(i, 0));
            }
        }
    }

    private void loadListeners() {
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (tabLayout.getTabAt(i).equals(tab)) {
                        tabLayout.getTabAt(i).setIcon(tabsImagesAtivo.getResourceId(i, 0));
                    } else {
                        tabLayout.getTabAt(i).setIcon(tabsImagesInativo.getResourceId(i, 0));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/
    public Grupo getGrupo() {
        return this.grupo;
    }

}
