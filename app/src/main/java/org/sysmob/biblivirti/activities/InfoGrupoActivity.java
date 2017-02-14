package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;

public class InfoGrupoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_grupo);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickParticiparGrupo(View view) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }
}
