package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;

public class ConfirmarRecuperacaoActivity extends AppCompatActivity {

    private EditText editRSCTOKN;
    private Button buttonValidar;
    private Button buttonReenviarToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_recuperacao);

        // Habilita o botao de voltar na actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        }
        return super.onOptionsItemSelected(item);
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/


    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadWidgets() {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (loadWidgets)!", Toast.LENGTH_SHORT).show();
    }

    private void enableWidgets(boolean status) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (enableWidgets)!", Toast.LENGTH_SHORT).show();
    }

    private void loadListeners() {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (loadListeners)!", Toast.LENGTH_SHORT).show();
    }

    private void loadErrors(JSONObject errors) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (loadErrors)!", Toast.LENGTH_SHORT).show();
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionReenviarToken(Bundle fields) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (actionReenviarToken)!", Toast.LENGTH_SHORT).show();
    }

    public void actionValidarToken(Bundle fields) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (actionValidarToken)!", Toast.LENGTH_SHORT).show();
    }
}
