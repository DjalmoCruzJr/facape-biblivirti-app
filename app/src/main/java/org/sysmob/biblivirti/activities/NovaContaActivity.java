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

public class NovaContaActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private Button buttonCriarConta;

    private EditText editEmail;
    private EditText editLogin;
    private EditText editSenha;
    private EditText editConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_conta);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        this.loadWidgets();
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
        this.buttonCriarConta.setEnabled(status);
        this.editEmail.setEnabled(status);
        this.editSenha.setEnabled(status);
        this.editConfirmarSenha.setEnabled(status);
    }

    private void loadWidgets() {
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.buttonCriarConta = (Button) this.findViewById(R.id.buttonCriarConta);
        this.editEmail = (EditText) this.findViewById(R.id.editEmail);
        this.editSenha = (EditText) this.findViewById(R.id.editSenha);
        this.editConfirmarSenha = (EditText) this.findViewById(R.id.editConfirmarSenha);
    }

    /*****************************************************
     * ACTION METHODS
     *****************************************************/
    public void actionCriarConta(View view) {
        this.progressBar.setVisibility(View.VISIBLE);
        this.enableWidgets(false);
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
        this.enableWidgets(true);
        this.progressBar.setVisibility(View.GONE);
    }

}
