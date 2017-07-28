package org.sysmob.biblivirti.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

/**
 * Created by micro99 on 20/04/2017.
 */

public class EditarSenhaActivity extends AppCompatActivity {

    private View viewEditarSenha;
    private ProgressBar progressBar;
    private EditText editSenha;
    private EditText editConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_senha);

        // Habilita o botao de voltar na actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        loadWidgets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_activity_editar_senha, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiDialogs.showConfirmationDialog(
                        this,
                        "Confirmação!",
                        "Deseja realmente cancelar a operação atual ?",
                        "Sim",
                        "Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BiblivirtiApplication.getInstance().cancelPendingRequests(EditarSenhaActivity.class.getSimpleName());
                                EditarSenhaActivity.this.finish();
                            }
                        }
                );
                break;
            case R.id.activity_editar_senha_menu_salvar:
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(EditarSenhaActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditarSenhaActivity.this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    /********************************************************
     * PIRVATE METHODS
     *******************************************************/
    private void loadWidgets() {
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.editSenha = (EditText) this.findViewById(R.id.editSenha);
        this.editConfirmarSenha = (EditText) this.findViewById(R.id.editConfirmarSenha);
    }

}
