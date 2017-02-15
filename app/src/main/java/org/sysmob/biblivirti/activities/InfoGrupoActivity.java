package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.InfoMembrosAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;

import java.text.SimpleDateFormat;

public class InfoGrupoActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerMembros;
    private ImageView imageGRCFOTO;
    private ImageView imageGrupoPrivado;
    private TextView editGRCNOME;
    private TextView editAICDESC;
    private TextView editGRDCADT;
    private Button buttonParticiparGrupo;
    private Grupo grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_grupo);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                Bundle fields = new Bundle();
                fields.putInt(Grupo.FIELD_GRNID, getIntent().getExtras().getInt(Grupo.FIELD_GRNID));
            }
        }
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
     * PRIVATE METHODS
     *******************************************************/
    private void enableWidgets(boolean status) {
        this.progressBar.setEnabled(status);
        this.recyclerMembros.setEnabled(status);
        this.imageGRCFOTO.setEnabled(status);
        this.imageGrupoPrivado.setEnabled(status);
        this.editGRCNOME.setEnabled(status);
        this.editAICDESC.setEnabled(status);
        this.editGRDCADT.setEnabled(status);
        this.buttonParticiparGrupo.setEnabled(status);
    }

    private void loadWidgets() {
        this.layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.recyclerMembros = (RecyclerView) findViewById(R.id.recyclerMembros);
        this.imageGRCFOTO = (ImageView) findViewById(R.id.imageGRCFOTO);
        this.imageGrupoPrivado = (ImageView) findViewById(R.id.imageGrupoPrivado);
        this.editGRCNOME = (TextView) findViewById(R.id.editGRCNOME);
        this.editGRDCADT = (TextView) findViewById(R.id.editGRDCADT);
        this.editAICDESC = (TextView) findViewById(R.id.editAICDESC);
        this.buttonParticiparGrupo = (Button) findViewById(R.id.buttonParticiparGrupo);
    }

    private void loadListeners() {
        this.buttonParticiparGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoGrupoActivity.this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFields() {
        this.editGRCNOME.setText(this.grupo.getGrcnome().toString());
        this.editAICDESC.setText(this.grupo.getAreaInteresse().getAicdesc().toString());
        this.editGRDCADT.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.grupo.getGrdcadt()));
        this.recyclerMembros.setAdapter(new InfoMembrosAdapter(this, grupo.getUsuarios()));
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/

    public void actionCarregarGrupo(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_INFO,
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
                        Toast.makeText(InfoGrupoActivity.this, message, Toast.LENGTH_LONG).show();
                        layoutEmpty.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        InfoGrupoActivity.this,
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                        ),
                                        "Ok"
                                );
                            } else {
                                grupo = BiblivirtiParser.parseToGrupo(response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA));
                                Toast.makeText(InfoGrupoActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), String.format("%s (ID %d)", response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), grupo.getGrnid()));
                                loadFields();
                            }
                        } catch (JSONException e) {
                            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.GONE);
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

    public void actionParticiparGrupo(Bundle fields) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }
}
