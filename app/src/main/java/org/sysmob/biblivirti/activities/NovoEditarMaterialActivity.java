package org.sysmob.biblivirti.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.model.Conteudo;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

import java.util.List;

public class NovoEditarMaterialActivity extends AppCompatActivity {

    private int activityMode;
    private View viewNovoMaterial;
    private ProgressBar progressBar;
    private ImageView imageGRCFOTO;
    private TextView textMensagem;
    private EditText editMACDESC;
    private RecyclerView recyclerConteudosRelacionados;
    private List<Conteudo> conteudos;
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
                fields.putInt(Conteudo.FIELD_CONIDGR, this.grupo.getGrnid());
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
                        // Falta implementar essa acao
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
        this.imageGRCFOTO.setEnabled(status);
        this.textMensagem.setEnabled(status);
        this.editMACDESC.setEnabled(status);
        this.recyclerConteudosRelacionados.setEnabled(status);
    }

    private void loadWidgets() {
        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.viewNovoMaterial = this.findViewById(R.id.viewNovoMaterial);
        this.imageGRCFOTO = (ImageView) this.findViewById(R.id.imageGRCFOTO);
        this.textMensagem = (TextView) this.findViewById(R.id.textMensagem);
        this.editMACDESC = (EditText) this.findViewById(R.id.editMACDESC);
        this.recyclerConteudosRelacionados = (RecyclerView) this.findViewById(R.id.recyclerConteudosRelacionados);
    }

    private void loadListeners() {
        // Falta implementar
    }

    private void loadErrors(JSONObject errors) {
        // Falta implementar
    }

    /*****************************************************
     * ACTION METHODS
     *****************************************************/
    public void actionCarregarConteudosRelacionados(Bundle fields) {
        Toast.makeText(this, "Esta funcionalida ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }

    public void actionNovoMaterial(Bundle fields) {
        Toast.makeText(this, "Esta funcionalida ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }
}
