package org.sysmob.biblivirti.dialogs;

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
import org.sysmob.biblivirti.business.MaterialBO;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Material;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

/**
 * Created by micro99 on 07/02/2017.
 */

public class AnexarLinkarMaterialDialog extends DialogFragment {

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
    private ETipoMaterial tipoMaterial;

    public AnexarLinkarMaterialDialog() {
        this.grupo = null;
        this.tipoMaterial = null;
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
                // Falta implementar
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
                        if (new MaterialBO(getView()).validateAdd()) {
                            Bundle extras = new Bundle();
                            extras.putInt(BiblivirtiConstants.ACTIVITY_MODE_KEY, BiblivirtiConstants.ACTIVITY_MODE_INSERTING);
                            extras.putString(BiblivirtiConstants.ACTIVITY_TITLE, getResources().getString(R.string.activity_novo_editar_material_label_insert));
                            extras.putSerializable(Grupo.KEY_GRUPO, grupo);
                            extras.putSerializable(Material.KEY_MATERIAL, BiblivirtiUtils.createMaterialByTipo(tipoMaterial));
                            Intent intent = new Intent(getContext(), NovoEditarMaterialActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
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

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void loadFields() {
        if (this.tipoMaterial == ETipoMaterial.APRESENTACAO || this.tipoMaterial == ETipoMaterial.EXERCICIO ||
                this.tipoMaterial == ETipoMaterial.FORMULA || this.tipoMaterial == ETipoMaterial.LIVRO) {
            this.viewAnexo.setEnabled(true);
            this.imageAnexo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_attachment_100px_blue));

            this.viewLink.setEnabled(false);
            this.editMACURL.setEnabled(false);
            this.imageLink.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_link_100px_gray));
        } else if (this.tipoMaterial == ETipoMaterial.JOGO || this.tipoMaterial == ETipoMaterial.VIDEO) {
            this.viewAnexo.setEnabled(false);
            this.imageAnexo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_attachment_100px_gray));

            this.viewLink.setEnabled(true);
            this.editMACURL.setEnabled(true);
            this.imageLink.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_link_100px_blue));
        } else if (this.tipoMaterial == ETipoMaterial.SIMULADO) {
            // Ocorreu erro pq nao chamou direto a tela de cadastro de simulado do dialogo de tipos de materiais
        }
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/

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
