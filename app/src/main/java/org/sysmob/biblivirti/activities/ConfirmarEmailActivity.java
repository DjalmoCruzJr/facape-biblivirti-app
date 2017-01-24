package org.sysmob.biblivirti.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.AccountBO;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.ConfirmarEmail;
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

public class ConfirmarEmailActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editCACTOKN;
    private Button buttonValidar;
    private Button buttonReenviarToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_email);

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
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public EditText getEditCACTOKN() {
        return editCACTOKN;
    }

    public void setEditCACTOKN(EditText editCACTOKN) {
        this.editCACTOKN = editCACTOKN;
    }

    public Button getButtonValidar() {
        return buttonValidar;
    }

    public void setButtonValidar(Button buttonValidar) {
        this.buttonValidar = buttonValidar;
    }

    public Button getButtonReenviarToken() {
        return buttonReenviarToken;
    }

    public void setButtonReenviarToken(Button buttonReenviarToken) {
        this.buttonReenviarToken = buttonReenviarToken;
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadWidgets() {
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.editCACTOKN = (EditText) this.findViewById(R.id.editCACTOKN);
        this.buttonValidar = (Button) this.findViewById(R.id.buttonValidar);
        this.buttonReenviarToken = (Button) this.findViewById(R.id.buttonReenviarToken);
    }

    private void enableWidgets(boolean status) {
        this.progressBar.setEnabled(status);
        this.editCACTOKN.setEnabled(status);
        this.buttonValidar.setEnabled(status);
        this.buttonReenviarToken.setEnabled(status);
    }

    private void loadListeners() {
        this.buttonValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(ConfirmarEmailActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {


                        if (new AccountBO(ConfirmarEmailActivity.this).validateEmailConfirmation()) {
                            Bundle fields = new Bundle();
                            fields.putString(ConfirmarEmail.FIELD_CACTOKN, editCACTOKN.getText().toString().trim());
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
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(ConfirmarEmailActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    BiblivirtiDialogs.showInputDialog(ConfirmarEmailActivity.this, R.layout.dialog_confirmar_email, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                TextView editEmail = (TextView) ((AlertDialog) dialog).findViewById(R.id.editEmail);
                                if (new AccountBO(ConfirmarEmailActivity.this).validateRecovery()) {
                                    Bundle fields = new Bundle();
                                    fields.putString(Usuario.FIELD_USCMAIL, editEmail.getText().toString().trim());
                                    actionReenviarToken(fields);
                                }
                            } catch (ValidationException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadErrors(JSONObject errors) {
        try {
            getEditCACTOKN().setError(errors.opt(ConfirmarEmail.FIELD_CACTOKN) != null ? errors.getString(ConfirmarEmail.FIELD_CACTOKN) : null);
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionReenviarToken(Bundle fields) {
        Log.i(getClass().getSimpleName().toString(), String.format("E-mail=%s:", fields.get(Usuario.FIELD_USCMAIL)));
        Toast.makeText(ConfirmarEmailActivity.this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }

    public void actionValidarToken(Bundle fields) {
        Map<String, String> params = new HashMap<>();
        params.put(ConfirmarEmail.FIELD_CACTOKN, fields.getString(ConfirmarEmail.FIELD_CACTOKN));
        RequestData requestData = new RequestData(
                this.getClass().getSimpleName(),
                Request.Method.GET,
                BiblivirtiConstants.API_ACCOUNT_EMAIL_CONFIRMATION,
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
                    Toast.makeText(ConfirmarEmailActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                            BiblivirtiDialogs.showMessageDialog(
                                    ConfirmarEmailActivity.this,
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
                        } else {
                            Usuario usuario = BiblivirtiParser.parseToUsuario(response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA));
                            Toast.makeText(ConfirmarEmailActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                            Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Usuario.KEY_USUARIO, usuario);
                            /*Intent intent = new Intent(ConfirmarEmailActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtras(bundle);
                            startActivity(intent);*/
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
    }
}
