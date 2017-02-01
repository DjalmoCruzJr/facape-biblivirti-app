package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.GroupBO;
import org.sysmob.biblivirti.enums.ETipoGrupo;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.AreaInteresse;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;

public class NovoGrupoActivity extends AppCompatActivity {

    private LinearLayout layoutNovoGrupo;
    private LinearLayout progressLayout;
    private ProgressBar progressBar;
    private EditText editGRCNOME;
    private EditText editAreaInteresse;
    private CheckBox checkGRCTIPO;
    private AreaInteresse areaInteresse = new AreaInteresse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_grupo);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_activity_novo_grupo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                finish();
                break;
            case R.id.activity_novo_grupo_menu_salvar:
                try {
                    if (new GroupBO(this).validateAdd()) {
                        Bundle fields = new Bundle();
                        fields.putString(Grupo.FIELD_GRCNOME, editGRCNOME.getText().toString().trim());
                        fields.putInt(Grupo.FIELD_GRNIDAI, areaInteresse.getAinid());
                        fields.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                        fields.putString(Grupo.FIELD_GRCTIPO, checkGRCTIPO.isChecked() ? String.valueOf(ETipoGrupo.FECHADO.getValue()) : String.valueOf(ETipoGrupo.ABERTO.getValue()));
                        actionNovoGrupo(fields);
                    }
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void enableWidgets(boolean status) {
        this.layoutNovoGrupo.setEnabled(status);
        this.editGRCNOME.setEnabled(status);
        this.editAreaInteresse.setEnabled(status);
        this.checkGRCTIPO.setEnabled(status);
    }

    private void loadWidgets() {
        this.layoutNovoGrupo = (LinearLayout) this.findViewById(R.id.containerLayout);
        this.progressLayout = (LinearLayout) this.findViewById(R.id.progressLayout);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.editGRCNOME = (EditText) this.findViewById(R.id.editGRCNOME);
        this.editAreaInteresse = (EditText) this.findViewById(R.id.editAreaInteresse);
        this.checkGRCTIPO = (CheckBox) this.findViewById(R.id.checkGRCTIPO);
    }

    private void loadListeners() {
    }

    private void loadErrors(JSONObject errors) {
        try {
            getEditGRCNOME().setError(errors.opt(Grupo.FIELD_GRCNOME) != null ? errors.getString(Grupo.FIELD_GRCNOME) : null);
            getEditAreaInteresse().setError(errors.opt(Grupo.FIELD_GRNIDAI) != null ? errors.getString(Grupo.FIELD_GRNIDAI) : null);
            getCheckGRCTIPO().setError(errors.opt(Grupo.FIELD_GRCTIPO) != null ? errors.getString(Grupo.FIELD_GRCTIPO) : null);
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/
    public EditText getEditGRCNOME() {
        return editGRCNOME;
    }

    public EditText getEditAreaInteresse() {
        return editAreaInteresse;
    }

    public CheckBox getCheckGRCTIPO() {
        return checkGRCTIPO;
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionNovoGrupo(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRCNOME, fields.getString(Grupo.FIELD_GRCNOME));
            params.put(Grupo.FIELD_GRNIDAI, fields.getInt(Grupo.FIELD_GRNIDAI));
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            params.put(Grupo.FIELD_GRCTIPO, fields.getString(Grupo.FIELD_GRCTIPO));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_ADD,
                    params
            );
            new NetworkConnection(this).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressLayout.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(NovoGrupoActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        NovoGrupoActivity.this,
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
                                int grnid = response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA).getInt(Grupo.FIELD_GRNID);
                                Toast.makeText(NovoGrupoActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), String.format("%s (ID %d)", response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), grnid));
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressLayout.setVisibility(View.GONE);
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

    public void actionNovaAreaInteresse(Bundle fields) {
    }

    public void actionCarregarAreaInteresse(Bundle fields) {
    }

    public void actionCarregarFoto(Bundle fields) {
    }

}
