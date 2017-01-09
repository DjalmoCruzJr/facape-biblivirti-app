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

public class LoginActivity extends AppCompatActivity implements ITransaction {

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
            try {
                JSONObject params = new JSONObject();
                params.put("uscmail", email);
                params.put("uscsenh", senha);
                RequestData requestData = new RequestData(Request.Method.POST, BiblivirtiConstants.API_ACCOUNT_LOGIN, params);
                new NetworkConnection(this, this).execute(requestData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                actionLogin();
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

    public void actionLogin() {
        try {
            JSONObject params = new JSONObject();
            params.put("uscmail", this.editEmail.getText().toString());
            params.put("uscsenh", this.editSenha.getText().toString());
            RequestData requestData = new RequestData(Request.Method.POST, BiblivirtiConstants.API_ACCOUNT_LOGIN, params);
            new NetworkConnection(this, this).execute(requestData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBeforeRequest() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.enableWidgets(false);
    }

    @Override
    public void onAfterRequest(JSONObject response) {
        this.progressBar.setVisibility(View.GONE);
        this.enableWidgets(true);
        Log.i("Resposta", response != null ?  response.toString() : "SEM RESPOSTA");
    }
}

