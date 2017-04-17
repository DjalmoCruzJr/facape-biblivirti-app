package org.sysmob.biblivirti.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.InfoMembrosAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.GroupBO;
import org.sysmob.biblivirti.enums.ETipoGrupo;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.text.SimpleDateFormat;

public class InfoGrupoActivity extends AppCompatActivity {

    private LinearLayout activityLayout;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerMembros;
    private ImageView imageGRCFOTO;
    private ImageView imageGrupoPrivado;
    private TextView textGRCNOME;
    private TextView textAICDESC;
    private TextView textGRDCADT;
    private TextView textQtdMembros;
    private Button buttonParticiparGrupo;
    private Grupo grupo;
    private Usuario loggedUser;
    private boolean showMenuOptions;

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
                actionCarregarGrupo(fields);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_activity_info_grupo, menu);
        menu.findItem(R.id.activity_info_grupo_menu_editar).setEnabled(this.showMenuOptions);
        menu.findItem(R.id.activity_info_grupo_menu_editar).setVisible(this.showMenuOptions);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                finish();
                break;

            case R.id.activity_info_grupo_menu_editar:
                Bundle fields;
                Intent intent;
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    fields = new Bundle();
                    fields.putInt(Grupo.FIELD_GRNID, this.grupo.getGrnid());
                    fields.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_EDITING);
                    fields.putString(BiblivirtiConstants.ACTIVITY_TITLE, getString(R.string.activity_editar_grupo_label));
                    intent = new Intent(this, NovoEditarGrupoActivity.class);
                    intent.putExtras(fields);
                    startActivity(intent);
                    finish();
                }
                break;
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
        this.textGRCNOME.setEnabled(status);
        this.textAICDESC.setEnabled(status);
        this.textGRDCADT.setEnabled(status);
        this.buttonParticiparGrupo.setEnabled(status);
    }

    private void loadWidgets() {
        this.loggedUser = BiblivirtiApplication.getInstance().getLoggedUser();
        this.activityLayout = (LinearLayout) findViewById(R.id.activityLayout);
        this.activityLayout.setVisibility(View.GONE);

        this.layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.recyclerMembros = (RecyclerView) findViewById(R.id.recyclerMembros);
        this.imageGRCFOTO = (ImageView) findViewById(R.id.imageGRCFOTO);
        this.imageGrupoPrivado = (ImageView) findViewById(R.id.imageGrupoPrivado);
        this.textGRCNOME = (TextView) findViewById(R.id.textGRCNOME);
        this.textGRDCADT = (TextView) findViewById(R.id.textGRDCADT);
        this.textAICDESC = (TextView) findViewById(R.id.textAICDESC);
        this.textQtdMembros = (TextView) findViewById(R.id.textQtdMembros);
        this.buttonParticiparGrupo = (Button) findViewById(R.id.buttonParticiparGrupo);

        this.showMenuOptions = false;
    }

    private void loadListeners() {
        this.buttonParticiparGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(InfoGrupoActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (new GroupBO(InfoGrupoActivity.this).validateSubscribe()) {
                            Bundle fields = new Bundle();
                            fields.putInt(Grupo.FIELD_GRNID, grupo.getGrnid());
                            fields.putInt(Usuario.FIELD_USNID, loggedUser.getUsnid());
                            actionParticiparGrupo(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadFields() {
        if (this.loggedUser.getUsnid() == this.grupo.getAdmin().getUsnid()) {
            this.showMenuOptions = true;
            this.invalidateOptionsMenu();
        }

        this.imageGrupoPrivado.setVisibility(this.grupo.getGrctipo().equals(ETipoGrupo.FECHADO) ? View.VISIBLE : View.GONE);
        this.imageGRCFOTO.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_app_group_80px));
        if (grupo.getGrcfoto() != null && !grupo.getGrcfoto().equals("null")) {
            Picasso.with(this).load(grupo.getGrcfoto()).into(this.imageGRCFOTO);
        }

        this.textGRCNOME.setText(this.grupo.getGrcnome().toString());
        this.textAICDESC.setText(this.grupo.getAreaInteresse().getAicdesc().toString());
        this.textGRDCADT.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.grupo.getGrdcadt()));
        this.textQtdMembros.setText(this.textQtdMembros.getText().toString().replace("xx", String.valueOf(this.grupo.getUsuarios().size())));

        this.recyclerMembros.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerMembros.setHasFixedSize(true);
        this.recyclerMembros.setAdapter(new InfoMembrosAdapter(this, grupo.getUsuarios(), grupo.getAdmin()));

        this.activityLayout.setVisibility(View.VISIBLE);
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
                                //Toast.makeText(InfoGrupoActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
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
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_SUBSCRIBE,
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
                                Toast.makeText(InfoGrupoActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), String.format("%s (ID %d)", response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), grupo.getGrnid()));
                                buttonParticiparGrupo.setVisibility(View.GONE);
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
}
