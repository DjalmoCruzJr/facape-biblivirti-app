package org.sysmob.biblivirti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiPreferences;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private EditText editEmail;
    private EditText editSenha;

    private Button buttonEntrar;
    private Button buttonRecuperarSenha;
    private Button buttonNovaConta;
    private Button buttonFacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String email = BiblivirtiPreferences.getProperty(this, BiblivirtiConstants.PREFERENCE_PROPERTY_EMAIL);
        String senha = BiblivirtiPreferences.getProperty(this, BiblivirtiConstants.PREFERENCE_PROPERTY_SENHA);

        // Verifica se o email e a senha ja estao salvos nas preferencias do app
        if ((email != null && !email.isEmpty()) && (senha != null && !senha.isEmpty())) {
            Bundle fields = new Bundle();
            fields.putString("uscmail", email);
            fields.putString("uscsenh", senha);
            actionLogin(fields);
        }

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void enableWidgets(boolean status) {
        this.editEmail.setEnabled(status);
        this.editSenha.setEnabled(status);
        this.buttonEntrar.setEnabled(status);
        this.buttonNovaConta.setEnabled(status);
        this.buttonRecuperarSenha.setEnabled(status);
        this.buttonFacebookLogin.setEnabled(status);
    }

    private void loadWidgets() {
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.editEmail = (EditText) findViewById(R.id.editEmail);
        this.editSenha = (EditText) findViewById(R.id.editSenha);
        this.buttonEntrar = (Button) findViewById(R.id.buttonEntar);
        this.buttonNovaConta = (Button) findViewById(R.id.buttonNovaConta);
        this.buttonRecuperarSenha = (Button) findViewById(R.id.buttonRecuperarSenha);
        this.buttonFacebookLogin = (Button) findViewById(R.id.buttonFacebookLogin);
    }

    private void loadListeners() {
        this.buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (new AccountBO(LoginActivity.this).validateLogin()) {
                            Bundle fields = new Bundle();
                            fields.putString(Usuario.FIELD_USCMAIL, editEmail.getText().toString().trim());
                            fields.putString(Usuario.FIELD_USCSENH, editSenha.getText().toString().trim());
                            actionLogin(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.buttonNovaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionNovaConta();
            }
        });
        this.buttonRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionRecuperarSenha();
            }
        });
        this.buttonFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionFacebookLogin();
            }
        });
    }

    private void loadErrors(JSONObject errors) {
        try {
            getEditEmail().setError(errors.opt(Usuario.FIELD_USCMAIL) != null ? errors.getString(Usuario.FIELD_USCMAIL) : null);
            getEditSenha().setError(errors.opt(Usuario.FIELD_USCSENH) != null ? errors.getString(Usuario.FIELD_USCSENH) : null);
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/
    public EditText getEditEmail() {
        return editEmail;
    }

    public void setEditEmail(EditText editEmail) {
        this.editEmail = editEmail;
    }

    public EditText getEditSenha() {
        return editSenha;
    }

    public void setEditSenha(EditText editSenha) {
        this.editSenha = editSenha;
    }

    public Button getButtonEntrar() {
        return buttonEntrar;
    }

    public void setButtonEntrar(Button buttonEntrar) {
        this.buttonEntrar = buttonEntrar;
    }

    public Button getButtonRecuperarSenha() {
        return buttonRecuperarSenha;
    }

    public void setButtonRecuperarSenha(Button buttonRecuperarSenha) {
        this.buttonRecuperarSenha = buttonRecuperarSenha;
    }

    public Button getButtonNovaConta() {
        return buttonNovaConta;
    }

    public void setButtonNovaConta(Button buttonNovaConta) {
        this.buttonNovaConta = buttonNovaConta;
    }

    public Button getButtonFacebookLogin() {
        return buttonFacebookLogin;
    }

    public void setButtonFacebookLogin(Button buttonFacebookLogin) {
        this.buttonFacebookLogin = buttonFacebookLogin;
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionRecuperarSenha() {
        Intent intent = new Intent(this, RecuperarSenhaActivity.class);
        startActivity(intent);
    }

    public void actionNovaConta() {
        Intent intent = new Intent(this, NovaContaActivity.class);
        startActivity(intent);
    }

    public void actionFacebookLogin() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.enableWidgets(false);
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementacada!", Toast.LENGTH_SHORT).show();
        this.enableWidgets(true);
        this.progressBar.setVisibility(View.GONE);
    }

    public void actionLogin(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Usuario.FIELD_USCMAIL, fields.getString(Usuario.FIELD_USCMAIL));
            params.put(Usuario.FIELD_USCSENH, fields.getString(Usuario.FIELD_USCSENH));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_ACCOUNT_LOGIN,
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
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) == BiblivirtiConstants.RESPONSE_CODE_UNAUTHORIZED) {
                                    Toast.makeText(LoginActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_LONG).show();
                                    Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                    Intent intent = new Intent(LoginActivity.this, ConfirmarEmailActivity.class);
                                    startActivity(intent);
                                } else {
                                    BiblivirtiDialogs.showMessageDialog(
                                            LoginActivity.this,
                                            "Mensagem",
                                            String.format(
                                                    "Código: %d\n%s",
                                                    response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                    response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                            ),
                                            "Ok"
                                    );
                                    // Carrega as mensagens de erros nos widgets
                                    loadErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS));
                                }
                            } else {
                                Usuario usuario = BiblivirtiParser.parseToUsuario(response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA));
                               // Toast.makeText(LoginActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                BiblivirtiPreferences.saveProperty(LoginActivity.this, BiblivirtiConstants.PREFERENCE_PROPERTY_EMAIL, usuario.getUscmail());
                                BiblivirtiPreferences.saveProperty(LoginActivity.this, BiblivirtiConstants.PREFERENCE_PROPERTY_SENHA, usuario.getUscsenh());
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Usuario.KEY_USUARIO, usuario);
                                BiblivirtiApplication.getInstance().setLoggedUser(usuario);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
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

