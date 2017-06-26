package org.sysmob.biblivirti.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.PesquisarGruposPagerAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.List;

public class PesquisarGruposActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ViewPager viewPager;
    private List<Grupo> grupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_grupos);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();

        // Processa a requsicao de pesquisa
        handleSearch(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_pesquisa, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.activity_pesquisar_menu_pesquisar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.activity_pesquisar_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.length() <= 0) {
                    return false;
                }
                Log.i(String.format("%s:", getClass().getSimpleName().toString()), query);
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(PesquisarGruposActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Bundle fields = new Bundle();
                    fields.putString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE, query);
                    actionPesquisar(fields);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.i(String.format("%s:", getClass().getSimpleName().toString()), query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                finish();
                break;
        }
        return true;
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void enableWidgets(boolean status) {
    }

    private void loadFields() {
        this.viewPager.setAdapter(new PesquisarGruposPagerAdapter(getSupportFragmentManager(), this.grupos));
    }

    private void loadWidgets() {
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);

        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }

    private void loadListeners() {
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void handleSearch(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            if (intent.getAction().equalsIgnoreCase(BiblivirtiConstants.INTENT_ACTION_PESQUISAR) && intent.hasCategory(BiblivirtiConstants.INTENT_CATEGORY_PESQUISAR_GRUPO)) {
                String query = intent.getExtras().getString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE);
                getSupportActionBar().setTitle(query);
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Bundle fields = new Bundle();
                    fields.putString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE, query);
                    actionPesquisar(fields);
                }
            }
        }
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    private void actionPesquisar(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(BiblivirtiConstants.FIELD_SEARCH_REFERENCE, fields.getString(BiblivirtiConstants.FIELD_SEARCH_REFERENCE));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_SEARCH,
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
                        Toast.makeText(PesquisarGruposActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        PesquisarGruposActivity.this,
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                        ),
                                        "Ok"
                                );
                                grupos = null;
                                loadFields();
                            } else {
                                grupos = BiblivirtiParser.parseToGrupos(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
                                Toast.makeText(PesquisarGruposActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                                loadFields();
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
