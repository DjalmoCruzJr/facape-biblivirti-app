package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.model.Grupo;

public class GrupoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Grupo currentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadWidgets() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);

        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }

    private void loadListeners() {
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/
    public Grupo getGrupo() {
        return this.currentGroup;
    }

}
