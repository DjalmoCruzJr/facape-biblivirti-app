package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.AreaOfInterestBO;
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
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.ArrayList;
import java.util.List;

public class NovoGrupoActivity extends AppCompatActivity {

    private int activityMode;
    private LinearLayout layoutNovoGrupo;
    private LinearLayout progressLayout;
    private ProgressBar progressBar;
    private EditText editGRCNOME;
    private ImageView imageGRCFOTO;
    private AutoCompleteTextView editAreaInteresse;
    private CheckBox checkGRCTIPO;
    private int layoutResourceId;
    private AreaInteresse areaInteresse;
    private List<AreaInteresse> areasInteresse;
    private Grupo grupo;

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

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                this.activityMode = getIntent().getExtras().getInt(BiblivirtiConstants.ACTIVITY_MODE_KEY);
                if (this.activityMode == BiblivirtiConstants.ACTIVITY_MODE_EDITING) {
                    Bundle fields = new Bundle();
                    fields.putInt(Grupo.FIELD_GRNID, getIntent().getExtras().getInt(Grupo.FIELD_GRNID));
                    actionCarregarGrupo(fields);
                }
            }
        }
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
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(NovoGrupoActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    if (this.areaInteresse == null) {
                        try {
                            if (new AreaOfInterestBO(NovoGrupoActivity.this).validateAdd()) {
                                Bundle fields = new Bundle();
                                fields.putString(AreaInteresse.FIELD_AICDESC, editAreaInteresse.getText().toString().trim());
                                actionNovaAreaInteresse(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    } else if (this.activityMode == BiblivirtiConstants.ACTIVITY_MODE_INSERTING) {
                        try {
                            if (new GroupBO(this).validateAdd()) {
                                Bundle fields = new Bundle();
                                fields.putString(Grupo.FIELD_GRCNOME, editGRCNOME.getText().toString().trim());
                                fields.putInt(Grupo.FIELD_GRNIDAI, this.areaInteresse.getAinid());
                                fields.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                                fields.putString(Grupo.FIELD_GRCTIPO, checkGRCTIPO.isChecked() ? String.valueOf(ETipoGrupo.FECHADO.getValue()) : String.valueOf(ETipoGrupo.ABERTO.getValue()));
                                actionNovoGrupo(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            if (new GroupBO(this).validateEdit()) {
                                Bundle fields = new Bundle();
                                fields.putInt(Grupo.FIELD_GRNID, this.grupo.getGrnid());
                                fields.putString(Grupo.FIELD_GRCNOME, editGRCNOME.getText().toString().trim());
                                fields.putInt(Grupo.FIELD_GRNIDAI, this.areaInteresse.getAinid());
                                fields.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                                fields.putString(Grupo.FIELD_GRCTIPO, checkGRCTIPO.isChecked() ? String.valueOf(ETipoGrupo.FECHADO.getValue()) : String.valueOf(ETipoGrupo.ABERTO.getValue()));
                                actionEditarGrupo(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }

        return true;
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
        this.layoutResourceId = android.R.layout.simple_list_item_1;
        this.layoutNovoGrupo = (LinearLayout) this.findViewById(R.id.containerLayout);
        this.progressLayout = (LinearLayout) this.findViewById(R.id.progressLayout);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.editGRCNOME = (EditText) this.findViewById(R.id.editGRCNOME);
        this.checkGRCTIPO = (CheckBox) this.findViewById(R.id.checkGRCTIPO);
        this.imageGRCFOTO = (ImageView) this.findViewById(R.id.imageGRCFOTO);

        this.areasInteresse = new ArrayList<>();
        this.editAreaInteresse = (AutoCompleteTextView) this.findViewById(R.id.editAreaInteresse);
        this.editAreaInteresse.setAdapter(new ArrayAdapter<String>(this, this.layoutResourceId, getAreasInteresseStringArray(this.areasInteresse)));
        this.editAreaInteresse.setDropDownHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        this.editAreaInteresse.setThreshold(3);
    }

    private String[] getAreasInteresseStringArray(List<AreaInteresse> areasInteresse) {
        String[] result = new String[areasInteresse != null ? areasInteresse.size() : 0];
        for (int i = 0; i < result.length; i++) {
            result[i] = areasInteresse.get(i).getAicdesc();
        }
        return result;
    }

    private void loadFields() {
        this.editGRCNOME.setText(this.grupo.getGrcnome().toString());
        this.editAreaInteresse.setText(this.areaInteresse.getAicdesc().toString());
    }

    private void loadListeners() {
        this.imageGRCFOTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCarregarFoto(null);
            }
        });

        this.editAreaInteresse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                Log.i(String.format("%s:", getClass().getSimpleName().toString()), "TEXTO: " + text);
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                Log.i(String.format("%s:", getClass().getSimpleName().toString()), "TEXTO: " + text);
                BiblivirtiApplication.getInstance().cancelPendingRequests(NovoGrupoActivity.this.getClass().getSimpleName());
            }

            @Override
            public void afterTextChanged(Editable text) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(NovoGrupoActivity.this, message, Toast.LENGTH_LONG).show();
                } else if (text.toString().trim().length() >= editAreaInteresse.getThreshold()) {
                    try {
                        if (new AreaOfInterestBO(NovoGrupoActivity.this).validateListAll()) {
                            Bundle fields = new Bundle();
                            fields.putString(AreaInteresse.FIELD_AICDESC, text.toString().trim());
                            actionCarregarAreaInteresse(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.editAreaInteresse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaInteresse = areasInteresse.get(position);
            }
        });
    }

    private void loadErrors(JSONObject errors) {
        try {
            editGRCNOME.setError(errors.opt(Grupo.FIELD_GRCNOME) != null ? errors.getString(Grupo.FIELD_GRCNOME) : null);
            getEditAreaInteresse().setError(errors.opt(Grupo.FIELD_GRNIDAI) != null ? errors.getString(Grupo.FIELD_GRNIDAI) : null);
            getEditAreaInteresse().setError(errors.opt(AreaInteresse.FIELD_AICDESC) != null ? errors.getString(AreaInteresse.FIELD_AICDESC) : null);
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
    private void actionEditarGrupo(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            params.put(Grupo.FIELD_GRCNOME, fields.getString(Grupo.FIELD_GRCNOME));
            params.put(Grupo.FIELD_GRNIDAI, fields.getInt(Grupo.FIELD_GRNIDAI));
            params.put(Usuario.FIELD_USNID, fields.getInt(Usuario.FIELD_USNID));
            params.put(Grupo.FIELD_GRCTIPO, fields.getString(Grupo.FIELD_GRCTIPO));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_EDIT,
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

    private void actionCarregarGrupo(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_GROUP_GET,
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
                            } else {
                                grupo = BiblivirtiParser.parseToGrupo(response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA));
                                areaInteresse = grupo.getAreaInteresse();
                                Toast.makeText(NovoGrupoActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), String.format("%s (ID %d)", response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), grupo.getGrnid()));
                                loadFields();
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
        try {
            JSONObject params = new JSONObject();
            params.put(AreaInteresse.FIELD_AICDESC, fields.getString(AreaInteresse.FIELD_AICDESC));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_AREAOFINTEREST_ADD,
                    params
            );
            new NetworkConnection(this).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressLayout.setVisibility(View.VISIBLE);
                    enableWidgets(false);
                }

                @Override
                public void onAfterRequest(final JSONObject response) {
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
                                int areasInteresseId = response.getJSONObject(BiblivirtiConstants.RESPONSE_DATA).getInt(AreaInteresse.FIELD_AINID);
                                try {
                                    if (new GroupBO(NovoGrupoActivity.this).validateAdd()) {
                                        Bundle fields = new Bundle();
                                        fields.putString(Grupo.FIELD_GRCNOME, editGRCNOME.getText().toString().trim());
                                        fields.putInt(Grupo.FIELD_GRNIDAI, areasInteresseId);
                                        fields.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                                        fields.putString(Grupo.FIELD_GRCTIPO, checkGRCTIPO.isChecked() ? String.valueOf(ETipoGrupo.FECHADO.getValue()) : String.valueOf(ETipoGrupo.ABERTO.getValue()));
                                        actionNovoGrupo(fields);
                                    }
                                } catch (ValidationException e) {
                                    e.printStackTrace();
                                }
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

    public void actionCarregarAreaInteresse(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(AreaInteresse.FIELD_AICDESC, fields.getString(AreaInteresse.FIELD_AICDESC));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_AREAOFINTEREST_LIST,
                    params
            );
            new NetworkConnection(this).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    //progressLayout.setVisibility(View.VISIBLE);
                    //enableWidgets(false);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(NovoGrupoActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                Log.i(String.format("%s:", getClass().getSimpleName().toString()), response.getString(BiblivirtiConstants.RESPONSE_MESSAGE));
                            } else {
                                areasInteresse = BiblivirtiParser.parseToAreasinteresse(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
                                editAreaInteresse.setAdapter(new ArrayAdapter<String>(NovoGrupoActivity.this, layoutResourceId, getAreasInteresseStringArray(areasInteresse)));
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

    public void actionCarregarFoto(Bundle fields) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }

}
