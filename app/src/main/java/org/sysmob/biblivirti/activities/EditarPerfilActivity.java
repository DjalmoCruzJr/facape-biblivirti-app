package org.sysmob.biblivirti.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.application.BiblivirtiApplication;
import org.sysmob.biblivirti.business.AccountBO;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

public class EditarPerfilActivity extends AppCompatActivity {

    private static final int REQUEST_LOAD_IMAGE_FROM_EXTERNAL_STORAGE = 1;

    private View activityEditarPerfil;
    private ProgressBar progressBar;
    private ImageView imageUSCFOTO;
    private TextView textImageInfo;
    private EditText editUSCNOME;
    private EditText editUSCMAIL;
    private EditText editUSCLOGN;
    private EditText editUSCSENH;
    private Usuario usuario;
    private String imageMimeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Habilita o botao voltar na actionar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Carrega os widgets da tela
        this.loadWidgets();

        // Carrega os listeners dos widgets
        this.loadListeners();

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                this.usuario = (Usuario) getIntent().getExtras().getSerializable(Usuario.KEY_USUARIO);
                loadFields();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_activity_editar_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                finish();
                break;
            case R.id.activity_editar_perfil_menu_salvar:
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(EditarPerfilActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (new AccountBO(this).validateProfileEdit()) {
                            Bundle fields = new Bundle();
                            if (!editUSCNOME.getText().toString().trim().equals("")) {
                                fields.putString(Usuario.FIELD_USCNOME, editUSCNOME.getText().toString().trim());
                            }
                            if (imageMimeType != null) {
                                fields.putString(Usuario.FIELD_USCFOTO, BiblivirtiUtils.encondImage(((BitmapDrawable) imageUSCFOTO.getDrawable()).getBitmap(), imageMimeType));
                            }
                            fields.putString(Usuario.FIELD_USCMAIL, editUSCMAIL.getText().toString().trim());
                            fields.putString(Usuario.FIELD_USCLOGN, editUSCLOGN.getText().toString().trim());
                            fields.putString(Usuario.FIELD_USCSENH, editUSCSENH.getText().toString().trim());
                            actionEditarPerfil(fields);
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOAD_IMAGE_FROM_EXTERNAL_STORAGE) {
                imageUSCFOTO.setImageURI(data.getData());
                imageMimeType = getContentResolver().getType(data.getData());
                textImageInfo.setText(getResources().getString(R.string.activity_editar_perfil_text_image_info_unset));
                Toast.makeText(this, "Imagem carregada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*****************************************************
     * PRIVATE METHODS
     *****************************************************/
    private void enableWidgets(boolean status) {
        this.activityEditarPerfil.setEnabled(status);
        this.progressBar.setEnabled(status);
        this.imageUSCFOTO.setEnabled(status);
        this.textImageInfo.setEnabled(status);
        this.editUSCNOME.setEnabled(status);
        this.editUSCMAIL.setEnabled(status);
        this.editUSCLOGN.setEnabled(status);
        this.editUSCSENH.setEnabled(status);
    }

    private void loadLiteners() {
        this.imageUSCFOTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageMimeType == null) {
                    actionCarregarFoto(null);
                } else {
                    imageMimeType = null;
                    imageUSCFOTO.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_app_user_80px));
                    textImageInfo.setText(getResources().getString(R.string.activity_novo_grupo_text_image_info_set));
                }
            }
        });
    }

    private void loadFields() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.activityEditarPerfil.setVisibility(View.INVISIBLE);

        if (this.usuario != null) {
            this.imageUSCFOTO.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_app_user_80px));
            if (this.usuario.getUscfoto() != null && !this.usuario.getUscfoto().toString().equalsIgnoreCase("null")) {
                Picasso.with(this).load(this.usuario.getUscfoto().toString()).into(this.imageUSCFOTO);
            }
            this.editUSCNOME.setText(this.usuario.getUscnome() != null ? this.usuario.getUscnome().toString() : "");
            this.editUSCMAIL.setText(this.usuario.getUscmail().toString());
            this.editUSCLOGN.setText(this.usuario.getUsclogn().toString());
            this.editUSCSENH.setText("");
        } else {
            String message = "Não foi possível carregar as informações do seu perfil! Por favor, tente novamente.\n" +
                    "Caso o erro persista, entre em contato com a equipe de suporte do Biblivirti AVAM.";
            BiblivirtiDialogs.showMessageDialog(
                    this,
                    "Mensagem",
                    message,
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BiblivirtiApplication.getInstance().cancelPendingRequests(this.getClass().getSimpleName());
                            dialog.dismiss();
                            EditarPerfilActivity.this.finish();
                        }
                    }
            );
        }

        this.progressBar.setVisibility(View.GONE);
        this.activityEditarPerfil.setVisibility(View.VISIBLE);
    }

    private void loadWidgets() {
        this.activityEditarPerfil = findViewById(R.id.activityEditarPerfil);
        this.activityEditarPerfil.setVisibility(View.INVISIBLE);

        this.progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.imageUSCFOTO = (ImageView) this.findViewById(R.id.imageUSCFOTO);
        this.textImageInfo = (TextView) this.findViewById(R.id.textImageInfo);
        this.editUSCNOME = (EditText) this.findViewById(R.id.editUSCNOME);

        this.editUSCMAIL = (EditText) this.findViewById(R.id.editUSCMAIL);
        this.editUSCMAIL.setEnabled(false); // Somente Leitura

        this.editUSCLOGN = (EditText) this.findViewById(R.id.editUSCLOGN);
        this.editUSCSENH = (EditText) this.findViewById(R.id.editUSCSENH);
    }

    private void loadListeners() {
        this.imageUSCFOTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCarregarFoto(null);
            }
        });
    }

    private void loadErrors(JSONObject errors) {
        try {

            this.editUSCNOME.setError(errors.opt(Usuario.FIELD_USCNOME) != null ? errors.getString(Usuario.FIELD_USCNOME) : null);
            this.editUSCMAIL.setError(errors.opt(Usuario.FIELD_USCMAIL) != null ? errors.getString(Usuario.FIELD_USCMAIL) : null);
            this.editUSCLOGN.setError(errors.opt(Usuario.FIELD_USCLOGN) != null ? errors.getString(Usuario.FIELD_USCLOGN) : null);
            this.editUSCSENH.setError(errors.opt(Usuario.FIELD_USCSENH) != null ? errors.getString(Usuario.FIELD_USCSENH) : null);
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }

    /*****************************************************
     * ACTION METHODS
     *****************************************************/
    public void actionCarregarFoto(Bundle fields) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_LOAD_IMAGE_FROM_EXTERNAL_STORAGE);
    }

    public void actionEditarPerfil(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            if (fields.containsKey(Usuario.FIELD_USCNOME)) {
                params.put(Usuario.FIELD_USCNOME, fields.getString(Usuario.FIELD_USCNOME));
            }
            params.put(Usuario.FIELD_USCMAIL, fields.getString(Usuario.FIELD_USCMAIL));
            params.put(Usuario.FIELD_USCLOGN, fields.getString(Usuario.FIELD_USCLOGN));
            params.put(Usuario.FIELD_USCSENH, fields.getString(Usuario.FIELD_USCSENH));
            if (fields.containsKey(Usuario.FIELD_USCFOTO)) {
                params.put(Usuario.FIELD_USCFOTO, fields.getString(Usuario.FIELD_USCFOTO));
            }
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_ACCOUNT_PROFILE_EDIT,
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
                        Toast.makeText(EditarPerfilActivity.this, message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        EditarPerfilActivity.this,
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE),
                                                BiblivirtiUtils.createStringErrors(response.opt(BiblivirtiConstants.RESPONSE_ERRORS) != null ? response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS) : null)
                                        ),
                                        "Ok"
                                );
                                // Carrega as mensagens de erros nos widgets
                                loadErrors(response.getJSONObject(BiblivirtiConstants.RESPONSE_ERRORS));
                            } else {
                                Toast.makeText(EditarPerfilActivity.this, response.getString(BiblivirtiConstants.RESPONSE_MESSAGE), Toast.LENGTH_SHORT).show();
                                PerfilActivity.hasDataChanged = true;
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
        } catch (JSONException e) {
            Log.e(String.format("%s:", getClass().getSimpleName().toString()), e.getMessage());
            e.printStackTrace();
        }
    }
}
