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
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiPreferences;

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

        String email = BiblivirtiPreferences.getProperty(this, BiblivirtiPreferences.PREFERENCE_PROPERTY_EMAIL);
        String senha = BiblivirtiPreferences.getProperty(this, BiblivirtiPreferences.PREFERENCE_PROPERTY_SENHA);

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
                Bundle fields = new Bundle();
                fields.putString("uscmail", editEmail.getText().toString().trim());
                fields.putString("uscsenh", editSenha.getText().toString().trim());
                actionLogin(fields);
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
            params.put("uscmail", fields.getString("uscmail"));
            params.put("uscsenh", fields.getString("uscsenh"));
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
                    progressBar.setVisibility(View.GONE);
                    enableWidgets(true);
                    Log.i("Resposta", response != null ? response.toString() : "SEM RESPOSTA");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

