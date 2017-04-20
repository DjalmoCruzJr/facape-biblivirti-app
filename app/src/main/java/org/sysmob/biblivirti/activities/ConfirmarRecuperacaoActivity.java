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
import org.sysmob.biblivirti.model.RecuperarSenha;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.HashMap;
import java.util.Map;

public class ConfirmarRecuperacaoActivity extends AppCompatActivity {

    private ProgressBar progressBar;
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
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.editRSCTOKN = (EditText) this.findViewById(R.id.editRSCTOKN);
        this.buttonValidar = (Button) this.findViewById(R.id.buttonValidar);
        this.buttonReenviarToken = (Button) this.findViewById(R.id.buttonReenviarToken);
    }

    private void enableWidgets(boolean status) {
        this.progressBar.setEnabled(status);
        this.editRSCTOKN.setEnabled(status);
        this.buttonValidar.setEnabled(status);
        this.buttonReenviarToken.setEnabled(status);
    }

    private void loadListeners() {
        this.buttonValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(ConfirmarRecuperacaoActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (new AccountBO(ConfirmarRecuperacaoActivity.this).validatePasswordReset()) {
                            Bundle fields = new Bundle();
                            fields.putString(RecuperarSenha.FIELD_RSCTOKN, editRSCTOKN.getText().toString().trim());
                            actionValidarToken(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.buttonReenviarToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfirmarRecuperacaoActivity.this, "Esta funcionalidade ainda não foi implementada (loadErrors)!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadErrors(JSONObject errors) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (loadErrors)!", Toast.LENGTH_SHORT).show();
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionValidarToken(Bundle fields) {
        Map<String, String> params = new HashMap<>();
        params.put(RecuperarSenha.FIELD_RSCTOKN, fields.getString(RecuperarSenha.FIELD_RSCTOKN));
        RequestData requestData = new RequestData(
                this.getClass().getSimpleName(),
                Request.Method.GET,
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
            }

            @Override
            public void onAfterRequest(String response) {
                if (response == null) {
                    String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                    Toast.makeText(ConfirmarRecuperacaoActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                            BiblivirtiDialogs.showMessageDialog(
                                    ConfirmarRecuperacaoActivity.this,
                                    "Mensagem",
                                    String.format(
                                            "Código: %d\n%s",
                                            json.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                            json.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                    ),
                                    "Ok"
                            );
                            // Carrega as mensagens de erros nos widgets
                            loadErrors(json.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS));
                        } else {
                            Usuario usuario = BiblivirtiParser.parseToUsuario(json.getJSONObject(BiblivirtiConstants.RESPONSE_DATA));
                            Toast.makeText(ConfirmarRecuperacaoActivity.this, json.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                            Log.i(String.format("%s:", getClass().getSimpleName().toString()), json.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Usuario.KEY_USUARIO, usuario);
                            Intent intent = new Intent(ConfirmarRecuperacaoActivity.this, EditarSenhaActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        });
    }

    public void actionReenviarToken(Bundle fields) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada (actionReenviarToken)!", Toast.LENGTH_SHORT).show();
    }

}
