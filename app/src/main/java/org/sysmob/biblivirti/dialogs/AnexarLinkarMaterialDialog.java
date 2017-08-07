package org.sysmob.biblivirti.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.activities.NovoEditarMaterialActivity;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

/**
 * Created by micro99 on 07/02/2017.
 */

public class AnexarLinkarMaterialDialog extends DialogFragment {

    private static final String REGEX_URL_VALIDATION = "";

    private static final int REQUEST_LOAD_FILE_FROM_EXTERNAL_STORAGE = 1;

    private View viewLink;
    private View viewAnexo;
    private ImageView imageLink;
    private ImageView imageAnexo;
    private TextView textLink;
    private TextView textAnexo;
    private EditText editMACURL;
    private Button buttonCancelar;
    private Button buttonOk;
    private Grupo grupo;
    private Material material;
    private ETipoMaterial tipoMaterial;
    private String fileMimeType;

    public AnexarLinkarMaterialDialog() {
        this.grupo = null;
        this.tipoMaterial = null;
        this.fileMimeType = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_anexar_linkar_material, container, false);

        this.setCancelable(false);

        if (getArguments() != null) {
            this.grupo = (Grupo) getArguments().getSerializable(Grupo.KEY_GRUPO);
            this.tipoMaterial = (ETipoMaterial) getArguments().getSerializable(Material.FIELD_MACTIPO);
        }

        this.viewLink = view.findViewById(R.id.viewLink);
        this.viewAnexo = view.findViewById(R.id.viewAnexo);
        this.imageLink = (ImageView) view.findViewById(R.id.imageLink);
        this.imageAnexo = (ImageView) view.findViewById(R.id.imageAnexo);
        this.textLink = (TextView) view.findViewById(R.id.textLink);
        this.textAnexo = (TextView) view.findViewById(R.id.textAnexo);
        this.editMACURL = (EditText) view.findViewById(R.id.editMACURL);
        this.buttonOk = (Button) view.findViewById(R.id.buttonOk);
        this.buttonCancelar = (Button) view.findViewById(R.id.buttonCancelar);

        this.viewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Falta implementar
            }
        });
        this.viewAnexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCarregarAnexo(null);
            }
        });
        this.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (validateAdd()) {
                            Material material = BiblivirtiUtils.createMaterialByTipo(tipoMaterial);
                            material.setMacurl(editMACURL.getText().toString().trim());
                            Bundle extras = new Bundle();
                            extras.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_INSERTING);
                            extras.putString(BiblivirtiConstants.ACTIVITY_TITLE, getResources().getString(R.string.activity_novo_editar_material_label_insert));
                            extras.putSerializable(Grupo.KEY_GRUPO, grupo);
                            extras.putSerializable(Material.KEY_MATERIAL, material);
                            Intent intent = new Intent(getContext(), NovoEditarMaterialActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                            dismiss();
                        }
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        loadFields();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_LOAD_FILE_FROM_EXTERNAL_STORAGE) {
                Material material = BiblivirtiUtils.createMaterialByTipo(this.tipoMaterial);
                material.setMacurl(data.getData().toString());
                Bundle extras = new Bundle();
                extras.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_INSERTING);
                extras.putString(BiblivirtiConstants.ACTIVITY_TITLE, getResources().getString(R.string.activity_novo_editar_material_label_insert));
                extras.putSerializable(Grupo.KEY_GRUPO, this.grupo);
                extras.putSerializable(Material.KEY_MATERIAL, material);
                Intent intent = new Intent(this.getContext(), NovoEditarMaterialActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                dismiss();
            }
        }
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private boolean validateAdd() throws ValidationException {
        boolean status = true;

        if (this.editMACURL.getText().toString().trim().equals("")) {
            status = false;
            this.editMACURL.setError("A URL do material deve ser informada!");
        }
        /*else if (!Pattern.matches(REGEX_URL_VALIDATION, this.editMACURL.getText().toString().trim())) {
            status = false;
            this.editMACURL.setError("Informe uma URL válida!");
        }*/

        return status;
    }

    private void loadFields() {
        if (this.tipoMaterial == ETipoMaterial.APRESENTACAO || this.tipoMaterial == ETipoMaterial.EXERCICIO ||
                this.tipoMaterial == ETipoMaterial.FORMULA || this.tipoMaterial == ETipoMaterial.LIVRO) {
            this.viewAnexo.setEnabled(true);
            this.imageAnexo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_attachment_100px_blue));

            this.viewLink.setEnabled(false);
            this.editMACURL.setEnabled(false);
            this.buttonOk.setEnabled(false);
            this.imageLink.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_link_100px_gray));
        } else if (this.tipoMaterial == ETipoMaterial.JOGO || this.tipoMaterial == ETipoMaterial.VIDEO) {
            this.viewAnexo.setEnabled(false);
            this.imageAnexo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_attachment_100px_gray));

            this.viewLink.setEnabled(true);
            this.editMACURL.setEnabled(true);
            this.imageLink.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_link_100px_blue));
        } else if (this.tipoMaterial == ETipoMaterial.SIMULADO) {
            // Ocorreu erro pq nao chamou direto a tela de cadastro de simulado do dialogo de tipos de materiais
            String message = "Ocorreu um erro durante o carregamento da interface. Por favor, tente novamente mais tarde!\n" +
                    "Se o erro persistir entre em contato com a equipe de suporte do Biblivirti AVAM.";
            BiblivirtiDialogs.showMessageDialog(getContext(), "Atenção!", message, "Ok");
            throw new RuntimeException(message);
        }
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionCarregarAnexo(Bundle fields) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(BiblivirtiConstants.MIME_TYPE_FILE_PDF);
        startActivityForResult(intent, REQUEST_LOAD_FILE_FROM_EXTERNAL_STORAGE);
    }

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/
    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public ETipoMaterial getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(ETipoMaterial tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }
}
