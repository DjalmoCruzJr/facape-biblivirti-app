package org.sysmob.biblivirti.activities;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.ConteudosRelacionadosAdapter;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.MaterialBO;
import org.sysmob.biblivirti.comparators.ConteudoComparatorByUsnid;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Conteudo;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiParser;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NovoEditarMaterialActivity extends AppCompatActivity {

    private int activityMode;
    View layoutEmpty;
    private View viewNovoMaterial;
    private ProgressBar progressBar;
    private ImageView imageIconeMaterial;
    private TextView textMensagem;
    private EditText editMACDESC;
    private RecyclerView recyclerConteudosRelacionados;
    private List<Conteudo> conteudosRelacionados;
    private List<Conteudo> conteudosSelecionados;
    private Grupo grupo;
    private Material material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_editar_material);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                this.activityMode = getIntent().getExtras().getInt(BiblivirtiConstants.ACTIVITY_MODE_KEY);
                getSupportActionBar().setTitle(getIntent().getExtras().getString(BiblivirtiConstants.ACTIVITY_TITLE));
                this.grupo = (Grupo) getIntent().getExtras().getSerializable(Grupo.KEY_GRUPO);
                this.material = (Material) getIntent().getExtras().getSerializable(Material.KEY_MATERIAL);
                Bundle fields = new Bundle();
                fields.putInt(Grupo.FIELD_GRNID, this.grupo.getGrnid());
                actionCarregarConteudosRelacionados(fields);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_activity_novo_material, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiDialogs.showConfirmationDialog(
                        this,
                        "Confirmação!",
                        "Deseja realmente cancelar a operação atual ?",
                        "Sim",
                        "Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BiblivirtiApplication.getInstance().cancelPendingRequests(NovoEditarMaterialActivity.this.getClass().getSimpleName());
                                NovoEditarMaterialActivity.this.finish();
                            }
                        }
                );
                break;

            case R.id.activity_novo_material_menu_salvar:
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(NovoEditarMaterialActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    if (!BiblivirtiUtils.isNetworkConnected()) {
                        String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                        Toast.makeText(NovoEditarMaterialActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (new MaterialBO(this).validateAdd()) {
                                Toast.makeText(this, String.format("Esta funcionalida ainda não foi implementada!"), Toast.LENGTH_SHORT).show();
                                Bundle fields = new Bundle();
                                fields.putInt(Usuario.FIELD_USNID, BiblivirtiApplication.getInstance().getLoggedUser().getUsnid());
                                fields.putInt(Grupo.FIELD_GRNID, this.grupo.getGrnid());
                                fields.putString(Material.FIELD_MACDESC, this.editMACDESC.getText().toString());
                                fields.putString(Material.FIELD_MACTIPO, String.valueOf(this.material.getMactipo().getValue()));
                                if (material.getMacurl() != null) {
                                    Uri fileUri = Uri.parse(this.material.getMacurl());
                                    String fileType = getContentResolver().getType(Uri.parse(this.material.getMacurl()));
                                    fields.putString(Material.FIELD_MACURL, BiblivirtiUtils.encondFile(this, fileUri, fileType));
                                }
                                fields.putSerializable(Material.FIELD_CONTENTS, (Serializable) BiblivirtiUtils.createContentsJson(this.conteudosSelecionados));
                                actionNovoMaterial(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
        return true;
    }

    /*****************************************************
     * PRIVATE METHODS
     *****************************************************/
    private void enableWidgets(boolean status) {
        this.viewNovoMaterial.setEnabled(status);
        this.progressBar.setEnabled(status);
        this.imageIconeMaterial.setEnabled(status);
        this.textMensagem.setEnabled(status);
        this.editMACDESC.setEnabled(status);
        this.recyclerConteudosRelacionados.setEnabled(status);
    }

    private void loadWidgets() {
        this.layoutEmpty = this.findViewById(R.id.layoutEmpty);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        this.viewNovoMaterial = this.findViewById(R.id.viewNovoMaterial);
        this.viewNovoMaterial.setVisibility(View.INVISIBLE);

        this.imageIconeMaterial = (ImageView) this.findViewById(R.id.imageIconeMaterial);
        this.textMensagem = (TextView) this.findViewById(R.id.textMensagem);
        this.editMACDESC = (EditText) this.findViewById(R.id.editMACDESC);
        this.recyclerConteudosRelacionados = (RecyclerView) this.findViewById(R.id.recyclerConteudosRelacionados);
    }

    private void loadListeners() {
        // Falta implementar
    }

    private void loadFields() {
        if (this.conteudosRelacionados == null) {
            BiblivirtiDialogs.showMessageDialog(
                    this,
                    "Mensagem",
                    String.format(
                            "Código: %d\n%s\n%s",
                            "Adicione os conteúdos no grupo antes de adicionar os materiais!"
                    ),
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            NovoEditarMaterialActivity.this.finish();
                        }
                    }
            );
        } else {
            // Verifica o tipo de material a ser cadastrado para configurar a imagem correspondente no ImangeView
            if (this.material.getMactipo() == ETipoMaterial.APRESENTACAO) {
                this.imageIconeMaterial.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_power_point_100px_blue));
            } else if (this.material.getMactipo() == ETipoMaterial.EXERCICIO) {
                this.imageIconeMaterial.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pdf_100px_blue));
            } else if (this.material.getMactipo() == ETipoMaterial.FORMULA) {
                this.imageIconeMaterial.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_sigma_100px_blue));
            } else if (this.material.getMactipo() == ETipoMaterial.JOGO) {
                this.imageIconeMaterial.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_game_100px_blue));
            } else if (this.material.getMactipo() == ETipoMaterial.LIVRO) {
                this.imageIconeMaterial.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_100px_blue));
            } else if (this.material.getMactipo() == ETipoMaterial.SIMULADO) {
                this.imageIconeMaterial.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_simulate_100px_blue));
            } else if (this.material.getMactipo() == ETipoMaterial.VIDEO) {
                this.imageIconeMaterial.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_100px_blue));
            }
            this.viewNovoMaterial.setVisibility(View.VISIBLE);

            this.conteudosSelecionados = new ArrayList<>();
            this.recyclerConteudosRelacionados = (RecyclerView) this.findViewById(R.id.recyclerConteudosRelacionados);
            this.recyclerConteudosRelacionados.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerConteudosRelacionados.setHasFixedSize(true);
            this.recyclerConteudosRelacionados.setAdapter(new ConteudosRelacionadosAdapter(this, this.conteudosRelacionados));
            ((ConteudosRelacionadosAdapter) this.recyclerConteudosRelacionados.getAdapter()).setOnItemClickListener(new ConteudosRelacionadosAdapter.OnItemClickListener() {
                @Override
                public void onCLick(View view, int position) {
                    // Verifica se o conteudo selecionado JA esta na lista de conteudos associados com o material
                    if (Collections.binarySearch(conteudosSelecionados, conteudosRelacionados.get(position), new ConteudoComparatorByUsnid()) >= 0) {
                        // Retira o conteudo da lista de conteudos associados
                        conteudosSelecionados.remove(conteudosRelacionados.get(position));
                    } else {
                        // Adiciona o conteudo na lista de conteudos associados
                        conteudosSelecionados.add(conteudosRelacionados.get(position));
                    }
                }
            });
        }
    }

    private void loadErrors(JSONObject errors) {
        // Falta implementar
    }

    /*****************************************************
     * ACTION METHODS
     *****************************************************/
    public void actionCarregarConteudosRelacionados(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_CONTENT_LIST,
                    params
            );
            new NetworkConnection(this).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        layoutEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(NovoEditarMaterialActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                layoutEmpty.setVisibility(View.VISIBLE);
                                BiblivirtiDialogs.showMessageDialog(
                                        NovoEditarMaterialActivity.this,
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                        ),
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                NovoEditarMaterialActivity.this.finish();
                                            }
                                        }
                                );
                            } else {
                                layoutEmpty.setVisibility(View.GONE);
                                conteudosRelacionados = BiblivirtiParser.parseToConteudos(response.getJSONArray(BiblivirtiConstants.RESPONSE_DATA));
                                loadFields();
                            }
                        } catch (JSONException e) {
                            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
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

    public void actionNovoMaterial(Bundle fields) {
        Toast.makeText(this, "Esta funcionalida ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }
}
