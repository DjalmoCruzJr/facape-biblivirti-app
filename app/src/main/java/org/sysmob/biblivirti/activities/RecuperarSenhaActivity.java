package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sysmob.biblivirti.R;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private Button buttonConfirmarEmail;

    private EditText editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        // habilita botao de voltar na acitonbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        loadWidgets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*****************************************************
     * PRIVATE METHODS
     *****************************************************/
    private void enableWidgets(boolean status) {
        this.editEmail.setEnabled(status);
        this.buttonConfirmarEmail.setEnabled(status);
    }

    private void loadWidgets() {
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.editEmail = (EditText) findViewById(R.id.editEmail);
        this.buttonConfirmarEmail = (Button) findViewById(R.id.buttonConfirmarEmail);
    }

    /*****************************************************
     * ACRION METHODS
     *****************************************************/

    public void actionConfirmarEmail(View view) {
        this.progressBar.setVisibility(View.VISIBLE);
        this.enableWidgets(false);
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
        this.enableWidgets(true);
        this.progressBar.setVisibility(View.GONE);
    }
}
