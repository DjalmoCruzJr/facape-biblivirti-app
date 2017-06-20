package org.sysmob.biblivirti.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.fragments.GruposFragment;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiPreferences;

public class HomeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Fragment currentFragment;
    private int currentItemId;
    private Usuario usuario;
    private ImageView imageUSCFOTO;
    private TextView textUSCNOME;
    private TextView textUSCLOGN;
    private TextView textUSCMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();

        if (currentFragment == null) {
            currentFragment = new GruposFragment();
            changeFragment(navigationView.getMenu().getItem(0));
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return this.drawerToggle.onOptionsItemSelected(item);
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void enableWidgets(boolean status) {
        this.navigationView.setEnabled(status);
    }

    private void loadWidgets() {
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawerLayout);
        this.drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_toggle_open, R.string.drawer_toggle_close);
        this.drawerLayout.addDrawerListener(drawerToggle);

        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.navigationView = (NavigationView) this.findViewById(R.id.navigationView);

        this.imageUSCFOTO = (ImageView) this.navigationView.getHeaderView(0).findViewById(R.id.imageUSCFOTO);
        this.textUSCNOME = (TextView) this.navigationView.getHeaderView(0).findViewById(R.id.textUSCNOME);
        this.textUSCMAIL = (TextView) this.navigationView.getHeaderView(0).findViewById(R.id.textUSCMAIL);
        this.textUSCLOGN = (TextView) this.navigationView.getHeaderView(0).findViewById(R.id.textUSCLOGN);

        // Carrega os dados do usuario logado na tela
        if (getIntent() != null) {
            if (getIntent().getExtras() != null && this.usuario == null) {
                Bundle extras = getIntent().getExtras();
                this.usuario = (Usuario) extras.get(Usuario.KEY_USUARIO);
                this.imageUSCFOTO.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_app_user_80px));
                if (this.usuario.getUscfoto() != null && !this.usuario.getUscfoto().equals("null")) {
                    Picasso.with(this).load(usuario.getUscfoto().toString()).into(this.imageUSCFOTO);
                }
                this.textUSCNOME.setText((usuario.getUscnome() != null && !usuario.getUscnome().equals("null")) ? usuario.getUscnome().toString() : usuario.getUsclogn().toString());
                this.textUSCLOGN.setText(usuario.getUsclogn().toString());
                this.textUSCMAIL.setText(usuario.getUscmail().toString());
            }
        }
    }

    private void loadListeners() {
        this.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return changeFragment(item);
            }
        });
    }

    private boolean changeFragment(MenuItem item) {
        Fragment fragment = currentFragment;
        switch (item.getItemId()) {
            case R.id.drawer_menu_grupos:
                fragment = currentItemId != item.getItemId() ? new GruposFragment() : currentFragment;
                break;
            case R.id.drawer_menu_perfil:
                break;
            case R.id.drawer_menu_notificacoes:
                break;
            case R.id.drawer_menu_configuracoes:
                break;
            case R.id.drawer_menu_sobre:
                startActivity(new Intent(this, SobreActivity.class));
                break;
            case R.id.drawer_menu_sair:
                actionSair(null);
                break;
        }
        if (currentFragment != fragment) {
            currentFragment = fragment;
            currentItemId = item.getItemId();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragmentContainer,
                            currentFragment,
                            currentFragment.getClass().getSimpleName()
                    )
                    .commit();
            toolbar.setTitle(item.getTitle().toString());
            getSupportActionBar().setTitle(item.getTitle().toString());
            item.setChecked(true);
        }
        drawerLayout.closeDrawers();
        return currentFragment == null ? false : true;
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    private void actionSair(Bundle fields) {
        this.progressBar.setVisibility(View.VISIBLE);
        this.enableWidgets(false);
        BiblivirtiPreferences.deleteProperty(this, BiblivirtiConstants.PREFERENCE_PROPERTY_EMAIL);
        BiblivirtiPreferences.deleteProperty(this, BiblivirtiConstants.PREFERENCE_PROPERTY_SENHA);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
