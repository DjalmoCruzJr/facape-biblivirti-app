package org.sysmob.biblivirti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        // Carrega os listeners dos widgets
        this.loadListeners();
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

    /*****************************************************
     * PUBLIC METHODS
     *****************************************************/
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Button getButtonCriarConta() {
        return buttonCriarConta;
    }

    public void setButtonCriarConta(Button buttonCriarConta) {
        this.buttonCriarConta = buttonCriarConta;
    }

    public EditText getEditEmail() {
        return editEmail;
    }

    public void setEditEmail(EditText editEmail) {
        this.editEmail = editEmail;
    }

    public EditText getEditLogin() {
        return editLogin;
    }

    public void setEditLogin(EditText editLogin) {
        this.editLogin = editLogin;
    }

    public EditText getEditSenha() {
        return editSenha;
    }

    public void setEditSenha(EditText editSenha) {
        this.editSenha = editSenha;
    }

    public EditText getEditConfirmarSenha() {
        return editConfirmarSenha;
    }

    public void setEditConfirmarSenha(EditText editConfirmarSenha) {
        this.editConfirmarSenha = editConfirmarSenha;
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
        this.editEmail = (EditText) this.findViewById(R.id.editEmail);
        this.editLogin = (EditText) this.findViewById(R.id.editLogin);
        this.editSenha = (EditText) this.findViewById(R.id.editSenha);
        this.editConfirmarSenha = (EditText) this.findViewById(R.id.editConfirmarSenha);
        this.buttonCriarConta = (Button) this.findViewById(R.id.buttonCriarConta);
    }

    private void loadListeners() {
        this.buttonCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(NovaContaActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (new AccountBO(NovaContaActivity.this).validateRegister()) {
                            Bundle fields = new Bundle();
                            fields.putString(Usuario.FIELD_USCMAIL, editEmail.getText().toString().trim());
                            fields.putString(Usuario.FIELD_USCLOGN, editLogin.getText().toString().trim());
                            fields.putString(Usuario.FIELD_USCSENH, editSenha.getText().toString().trim());
                            fields.putString(Usuario.FIELD_USCSENH2, editConfirmarSenha.getText().toString().trim());
                            actionCriarConta(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadErrors(JSONObject errors) {
        try {
            getEditEmail().setError(errors.opt(Usuario.FIELD_USCMAIL) != null ? errors.getString(Usuario.FIELD_USCMAIL) : null);
            getEditLogin().setError(errors.opt(Usuario.FIELD_USCLOGN) != null ? errors.getString(Usuario.FIELD_USCLOGN) : null);
            getEditSenha().setError(errors.opt(Usuario.FIELD_USCSENH) != null ? errors.getString(Usuario.FIELD_USCSENH) : null);
            getEditConfirmarSenha().setError(errors.opt(Usuario.FIELD_USCSENH2) != null ? errors.getString(Usuario.FIELD_USCSENH2) : null);
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

    /*****************************************************
     * ACTION METHODS
     *****************************************************/
    public void actionCriarConta(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Usuario.FIELD_USCMAIL, fields.getString(Usuario.FIELD_USCMAIL));
            params.put(Usuario.FIELD_USCLOGN, fields.getString(Usuario.FIELD_USCLOGN));
            params.put(Usuario.FIELD_USCSENH, fields.getString(Usuario.FIELD_USCSENH));
            params.put(Usuario.FIELD_USCSENH2, fields.getString(Usuario.FIELD_USCSENH2));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_ACCOUNT_REGISTER,
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
                        Toast.makeText(NovaContaActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        NovaContaActivity.this,
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
                                int usnid = response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA).getInt(Usuario.FIELD_USNID);
                                Toast.makeText(NovaContaActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                Bundle bundle = new Bundle();
                                bundle.putInt(Usuario.FIELD_USNID, usnid);
                                Intent intent = new Intent(NovaContaActivity.this, ConfirmarEmailActivity.class);
                                intent.putExtras(bundle);
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
