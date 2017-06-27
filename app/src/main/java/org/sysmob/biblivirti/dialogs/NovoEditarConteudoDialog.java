package org.sysmob.biblivirti.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;
import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.business.ContentBO;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.fragments.ConteudosFragment;
import org.sysmob.biblivirti.model.Conteudo;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.network.ITransaction;
import org.sysmob.biblivirti.network.NetworkConnection;
import org.sysmob.biblivirti.network.RequestData;
import org.sysmob.biblivirti.utils.BiblivirtiConstants;
import org.sysmob.biblivirti.utils.BiblivirtiDialogs;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

/**
 * Created by micro99 on 07/02/2017.
 */

public class NovoEditarConteudoDialog extends DialogFragment {

    private int dialogMode;
    private ProgressBar progressBar;
    private TextView textDescricaoTela;
    private EditText editCOCDESC;
    private Button buttonCancelar;
    private Button buttonSalvar;
    private Conteudo conteudo;
    private Grupo grupo;

    public NovoEditarConteudoDialog() {
        this.grupo = null;
        this.dialogMode = BiblivirtiConstants.DIALOG_MODE_INSERTING;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_novo_editar_conteudo, container, false);

        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.textDescricaoTela = (TextView) view.findViewById(R.id.textDescricaoTela);
        this.editCOCDESC = (EditText) view.findViewById(R.id.editCOCDESC);
        this.buttonSalvar = (Button) view.findViewById(R.id.buttonSalvar);
        this.buttonCancelar = (Button) view.findViewById(R.id.buttonCancelar);
        this.buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiblivirtiUtils.isNetworkConnected()) {
                    String message = "Você não está conectado a internet.\nPor favor, verifique sua conexão e tente novamente!";
                    Toast.makeText(NovoEditarConteudoDialog.this.getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    if (dialogMode == BiblivirtiConstants.DIALOG_MODE_INSERTING) {
                        try {
                            if (new ContentBO(NovoEditarConteudoDialog.this.getView()).validateAdd()) {
                                Bundle fields = new Bundle();
                                fields.putInt(Grupo.FIELD_GRNID, grupo.getGrnid());
                                fields.putString(Conteudo.FIELD_COCDESC, editCOCDESC.getText().toString().trim());
                                actionNovoConteudo(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            if (new ContentBO(NovoEditarConteudoDialog.this.getView()).validateEdit()) {
                                Bundle fields = new Bundle();
                                fields.putInt(Grupo.FIELD_GRNID, grupo.getGrnid());
                                fields.putInt(Conteudo.FIELD_CONID, conteudo.getConid());
                                fields.putString(Conteudo.FIELD_COCDESC, editCOCDESC.getText().toString().trim());
                                actionEditarConteudo(fields);
                            }
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
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
    private void loadErrors(JSONObject errors) {

    }

    private void enableWidgets(boolean status) {
        this.textDescricaoTela.setEnabled(status);
        this.editCOCDESC.setEnabled(status);
        this.buttonCancelar.setEnabled(status);
        this.buttonSalvar.setEnabled(status);
    }

    private void loadFields() {
        if (this.dialogMode == BiblivirtiConstants.DIALOG_MODE_INSERTING) {
            this.textDescricaoTela.setText(getResources().getString(R.string.dialog_novo_editar_conteudo_text_descricao_tela_novo));
            this.editCOCDESC.setText("");
        } else {
            this.textDescricaoTela.setText(getResources().getString(R.string.dialog_novo_editar_conteudo_text_descricao_tela_editar));
            this.editCOCDESC.setText(this.conteudo.getCocdesc().toString());
        }
    }

    /********************************************************
     * ACTION METHODS
     *******************************************************/
    public void actionNovoConteudo(Bundle fields) {
        try {
            JSONObject params = new JSONObject();
            params.put(Grupo.FIELD_GRNID, fields.getInt(Grupo.FIELD_GRNID));
            RequestData requestData = new RequestData(
                    this.getClass().getSimpleName(),
                    Request.Method.POST,
                    BiblivirtiConstants.API_CONTENT_ADD,
                    params
            );
            new NetworkConnection(getActivity()).execute(requestData, new ITransaction() {
                @Override
                public void onBeforeRequest() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAfterRequest(JSONObject response) {
                    if (response == null) {
                        String message = "Não houve resposta do servidor.\nTente novamente e em caso de falha entre em contato com a equipe de suporte do Biblivirti.";
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            if (response.getInt(BiblivirtiConstants.RESPONSE_CODE) != BiblivirtiConstants.RESPONSE_CODE_OK) {
                                BiblivirtiDialogs.showMessageDialog(
                                        getActivity(),
                                        "Mensagem",
                                        String.format(
                                                "Código: %d\n%s",
                                                response.getInt(BiblivirtiConstants.RESPONSE_CODE),
                                                response.getString(BiblivirtiConstants.RESPONSE_MESSAGE)
                                        ),
                                        "Ok"
                                );
                            } else {
                                Toast.makeText(NovoEditarConteudoDialog.this.getContext(), response.getString(BiblivirtiConstants.RESPONSE_DATA), Toast.LENGTH_SHORT).show();
                                ConteudosFragment.hasDataChanged = true;
                                dismiss();
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

    public void actionEditarConteudo(Bundle fields) {
        Toast.makeText(this.getContext(), "Esta fucionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
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

    public int getDialogMode() {
        return dialogMode;
    }

    public void setDialogMode(int dialogMode) {
        this.dialogMode = dialogMode;
    }
}
