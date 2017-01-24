package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.sysmob.biblivirti.R;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            String message = null;
            switch (item.getItemId()) {
                case R.id.drawer_menu_grupos:
                    message = String.format("MENU (ID %d): %s", item.getItemId(), item.getTitle());
                case R.id.drawer_menu_perfil:
                    message = String.format("MENU (ID %d): %s", item.getItemId(), item.getTitle());
                case R.id.drawer_menu_notificacoes:
                    message = String.format("MENU (ID %d): %s", item.getItemId(), item.getTitle());
                case R.id.drawer_menu_configuracoes:
                    message = String.format("MENU (ID %d): %s", item.getItemId(), item.getTitle());
                case R.id.drawer_menu_sobre:
                    message = String.format("MENU (ID %d): %s", item.getItemId(), item.getTitle());
                case R.id.drawer_menu_sair:
                    message = String.format("MENU (ID %d): %s", item.getItemId(), item.getTitle());
            }
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadWidgets() {
        this.drawerLayout = (DrawerLayout) this.findViewById(R.id.drawerLayout);
        this.drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_toggle_open, R.string.drawer_toggle_close);
        this.drawerLayout.addDrawerListener(drawerToggle);
        this.drawerToggle.syncState();

        this.toolbar = (Toolbar) this.findViewById(R.id.homeToolbar);

        // Seta a toolbar como actionbar default
        setSupportActionBar(toolbar);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_hamburger_50px_white);
    }

    private void loadListeners() {
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/


    /********************************************************
     * ACTION METHODS
     *******************************************************/
}
