package org.sysmob.biblivirti.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.AccountBO;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
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
                    try {
                        if (new AccountBO(EditarSenhaActivity.this).validatePasswordEdit()) {
                            Bundle fields = new Bundle();
                            fields.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                            fields.putString(Usuario.FIELD_USCSENH, editSenha.getText().toString().trim());
                            fields.putString(Usuario.FIELD_USCSENH2, editConfirmarSenha.getText().toString().trim());
                            actionEditarSenha(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
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

    private void enableWidgets(boolean status) {
        this.viewEditarSenha.setEnabled(status);
        this.progressBar.setEnabled(status);
        this.editSenha.setEnabled(status);
        this.editConfirmarSenha.setEnabled(status);
    }

    private void loadErrors(JSONObject errors) {
        try {
            this.editSenha.setError(errors.opt(Usuario.FIELD_USCSENH) != null ? errors.getString(Usuario.FIELD_USCSENH) : null);
            this.editConfirmarSenha.setError(errors.opt(Usuario.FIELD_USCSENH2) != null ? errors.getString(Usuario.FIELD_USCSENH2) : null);
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

    /********************************************************
     * ACTIONS METHODS
     *******************************************************/
    private void actionEditarSenha(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            params.put(Usuario.FIELD_USCSENH, fields.getString(Usuario.FIELD_USCSENH));
            params.put(Usuario.FIELD_USCSENH2, fields.getString(Usuario.FIELD_USCSENH2));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_ACCOUNT_PASSWORD_EDIT,
                    params
            );
            new NetworkConnection(this).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(EditarSenhaActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        EditarSenhaActivity.this,
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE),
                                                BiblivirtiUtils.createStringErrors(response.opt(BiblivirtiConstants.RESPONSE_ERRORS) != null ? response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS) : null)
                                        ),
                                        "Ok"
                                );
                                // Carrega as mensagens de erro nos widgets
                                loadErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS));
                            } else {
                                Toast.makeText(EditarSenhaActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                Intent intent = new Intent(EditarSenhaActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    enableWidgets(true);
                }

                @Override
                public void onAfterRequest(String response) {
                }
            });
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

}
