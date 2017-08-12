package org.sysmob.biblivirti.dialogs;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.OpcoesMembrosAdapter;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

/**
 * Created by micro99 on 07/02/2017.
 */

public class OpcoesMembrosDialog extends DialogFragment {

    public static final int OPTION_VER_PERFIL = 0;
    public static final int OPTION_ENVIAR_EMAIL = 1;
    public static final int OPTION_REMOVER_DO_GRUPO = 2;

    private RecyclerView recyclerOpcoes;
    private OpcoesMembrosAdapter.OnItemClickListener onOptionsClickListener;
    private String[] textOpcoes;
    private TypedArray imageOpcoesAtivas;
    private TypedArray imageOpcoesInativas;
    private Grupo grupo;
    private Usuario membro;

    public OpcoesMembrosDialog() {
        this.membro = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_opcoes_membros, container, false);

        this.textOpcoes = getActivity().getResources().getStringArray(R.array.dialog_opcoes_membros_texts);
        this.imageOpcoesAtivas = getActivity().getResources().obtainTypedArray(R.array.dialog_opcoes_membros_images_ativo);
        this.imageOpcoesInativas = getActivity().getResources().obtainTypedArray(R.array.dialog_opcoes_membros_images_inativo);

        this.recyclerOpcoes = (RecyclerView) view.findViewById(R.id.recyclerOpcoes);
        this.recyclerOpcoes.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerOpcoes.setHasFixedSize(true);
        this.recyclerOpcoes.setAdapter(new OpcoesMembrosAdapter(getActivity(), this.textOpcoes, this.imageOpcoesAtivas, this.imageOpcoesInativas, this.grupo));
        ((OpcoesMembrosAdapter) this.recyclerOpcoes.getAdapter()).setOnItemClickListener(this.onOptionsClickListener);
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
     * PUBLIC METHODS
     *******************************************************/
    public void setOnOptionsClickListener(OpcoesMembrosAdapter.OnItemClickListener onOptionsClickListener) {
        this.onOptionsClickListener = onOptionsClickListener;
    }

    public Usuario getMembro() {
        return membro;
    }

    public void setMembro(Usuario membro) {
        this.membro = membro;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
