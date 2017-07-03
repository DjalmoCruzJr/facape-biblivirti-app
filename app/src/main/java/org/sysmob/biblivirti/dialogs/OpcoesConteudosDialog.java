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
import org.sysmob.biblivirti.adapters.OpcoesConteudosAdapter;
import org.sysmob.biblivirti.model.Grupo;

/**
 * Created by micro99 on 07/02/2017.
 */

public class OpcoesConteudosDialog extends DialogFragment {

    private RecyclerView recyclerOpcoes;
    private OpcoesConteudosAdapter.OnItemClickListener onOptionsClickListener;
    private String[] textOpcoes;
    private TypedArray imageOpcoesAtivas;
    private Grupo grupo;

    public OpcoesConteudosDialog() {
        this.grupo = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_opcoes_conteudos, container, false);

        this.textOpcoes = getActivity().getResources().getStringArray(R.array.dialog_opcoes_conteudos_texts);
        this.imageOpcoesAtivas = getActivity().getResources().obtainTypedArray(R.array.dialog_opcoes_conteudos_images_ativo);

        this.recyclerOpcoes = (RecyclerView) view.findViewById(R.id.recyclerOpcoes);
        this.recyclerOpcoes.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerOpcoes.setHasFixedSize(true);
        this.recyclerOpcoes.setAdapter(new OpcoesConteudosAdapter(getActivity(), this.textOpcoes, this.imageOpcoesAtivas));
        ((OpcoesConteudosAdapter) this.recyclerOpcoes.getAdapter()).setOnItemClickListener(this.onOptionsClickListener);
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
    public void setOnOptionsClickListener(OpcoesConteudosAdapter.OnItemClickListener onOptionsClickListener) {
        this.onOptionsClickListener = onOptionsClickListener;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
