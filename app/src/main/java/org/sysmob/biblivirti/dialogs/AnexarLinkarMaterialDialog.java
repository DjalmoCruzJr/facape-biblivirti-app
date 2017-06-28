package org.sysmob.biblivirti.dialogs;

import android.app.Dialog;
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

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.enums.ETipoMaterial;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Material;

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

        if (getArguments() != null) {
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
                // Falta implementar
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
}
