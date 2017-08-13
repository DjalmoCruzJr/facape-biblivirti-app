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
import org.sysmob.biblivirti.adapters.PerfilGruposAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.text.SimpleDateFormat;

public class PerfilActivity extends AppCompatActivity {

    private View activityPerfil;
    private View layoutEmpty;
    private ProgressBar progressBar;
    private RecyclerView recyclerGrupos;
    private ImageView imageUSCFOTO;
    private TextView textUSCNOME;
    private TextView textUSCLOGN;
    private TextView textUSCMAIL;
    private TextView textUSDCADT;
    private TextView textQtdGrupos;
    private Usuario usuario;
    private Usuario usuarioLogado;
    private boolean showMenuOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                Bundle fields = new Bundle();
                fields.putInt(Usuario.FIELD_USNID, getIntent().getExtras().getInt(Usuario.FIELD_USNID));
                actionCarregarUsuario(fields);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_activity_perfil, menu);
        menu.findItem(R.id.activity_perfil_menu_editar).setEnabled(this.showMenuOptions);
        menu.findItem(R.id.activity_perfil_menu_editar).setVisible(this.showMenuOptions);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                finish();
                break;

            case R.id.activity_perfil_menu_editar:
                Bundle fields;
                Intent intent;
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    fields = new Bundle();
                    fields.putInt(Usuario.FIELD_USNID, this.usuario.getUsnid());
                    fields.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_EDITING);
                    fields.putString(BiblivirtiConstants.ACTIVITY_TITLE, getString(R.string.activity_editar_perfil_label));
                    intent = new Intent(this, EditarPerfilActivity.class);
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
        this.recyclerGrupos.setEnabled(status);
        this.imageUSCFOTO.setEnabled(status);
        this.textUSCNOME.setEnabled(status);
        this.textUSCLOGN.setEnabled(status);
        this.textUSCMAIL.setEnabled(status);
        this.textUSDCADT.setEnabled(status);
        this.textQtdGrupos.setEnabled(status);
        this.activityPerfil.setEnabled(status);
    }

    private void loadWidgets() {
        this.activityPerfil = findViewById(R.id.activityPerfil);
        this.activityPerfil.setVisibility(View.GONE);

        this.layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.recyclerGrupos = (RecyclerView) findViewById(R.id.recyclerGrupos);
        this.imageUSCFOTO = (ImageView) findViewById(R.id.imageUSCFOTO);
        this.textUSCNOME = (TextView) findViewById(R.id.textUSCNOME);
        this.textUSCLOGN = (TextView) findViewById(R.id.textUSCLOGN);
        this.textUSCMAIL = (TextView) findViewById(R.id.textUSCMAIL);
        this.textUSDCADT = (TextView) findViewById(R.id.textUSDCADT);
        this.textQtdGrupos = (TextView) findViewById(R.id.textQtdGrupos);

        this.usuarioLogado = BiblivirtiApplication.getInstance().getLoggedUser();
        this.showMenuOptions = false;
    }

    private void loadListeners() {
    }

    private void loadFields() {
        if (this.usuario.getUsnid() == this.usuarioLogado.getUsnid()) {
            this.showMenuOptions = true;
            this.invalidateOptionsMenu();
        }

        this.imageUSCFOTO.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_app_user_80px));
        if (usuario.getUscfoto() != null && !usuario.getUscfoto().equals("null")) {
            Picasso.with(this).load(usuario.getUscfoto()).into(this.imageUSCFOTO);
        }

        this.textUSCNOME.setText(this.usuario.getUscnome() != null && !this.usuario.getUscnome().equalsIgnoreCase("null") ? this.usuario.getUscnome().toString() : this.usuario.getUsclogn().toString());
        this.textUSCLOGN.setText(this.usuario.getUsclogn().toString());
        this.textUSCMAIL.setText(this.usuario.getUscmail().toString());
        this.textUSDCADT.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.usuario.getUsdcadt()));
        this.textQtdGrupos.setText(this.textQtdGrupos.getText().toString().replace("xx", String.valueOf(this.usuario.getGrupos().size())));

        this.recyclerGrupos.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerGrupos.setHasFixedSize(true);
        this.recyclerGrupos.setAdapter(new PerfilGruposAdapter(this, this.usuario.getGrupos(), this.usuario));
        ((PerfilGruposAdapter) this.recyclerGrupos.getAdapter()).setOnItemClickListener(new PerfilGruposAdapter.OnItemClickListener() {
            @Override
            public void onCLick(View view, int position) {
                Toast.makeText(PerfilActivity.this, String.format("PerfilGruposAdapter.onCLick(): %d", position), Toast.LENGTH_SHORT).show();
            }
        });

        this.activityPerfil.setVisibility(View.VISIBLE);
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionCarregarUsuario(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_ACCOUNT_PROFILE,
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
                        Toast.makeText(PerfilActivity.this, message, Toast.LENGTH_LONG).show();
                        layoutEmpty.setVisibility(View.VISIBLE);
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        PerfilActivity.this,
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE),
                                                BiblivirtiUtils.createStringErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS))
                                        ),
                                        "Ok"
                                );
                            } else {
                                usuario = BiblivirtiParser.parseToUsuario(response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA));
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), String.format("%s (ID %s)", response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), usuario.getUscmail()));
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
}
