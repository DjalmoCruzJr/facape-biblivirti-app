package org.sysmob.biblivirti.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.PesquisaUsuariosFragmentAdapter;
import org.sysmob.biblivirti.model.Grupo;
import org.sysmob.biblivirti.model.Usuario;

import java.util.List;

/**
 * Created by micro99 on 17/02/2017.
 */
public class PesquisarUsuariosFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerUsuarios;
    private List<Usuario> usuarios;
    private Grupo grupo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisar_usuarios, container, false);

        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.layoutEmpty.setVisibility(this.usuarios == null ? View.VISIBLE : View.GONE);
        this.recyclerUsuarios = (RecyclerView) view.findViewById(R.id.recyclerUsuarios);
        this.recyclerUsuarios.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerUsuarios.setHasFixedSize(true);
        this.recyclerUsuarios.setAdapter(new PesquisaUsuariosFragmentAdapter(getContext(), this.usuarios, this.grupo));
        ((PesquisaUsuariosFragmentAdapter) this.recyclerUsuarios.getAdapter()).setOnItemClickListener(new PesquisaUsuariosFragmentAdapter.OnItemClickListener() {
            @Override
            public void onCLick(View view, int position) {
                Toast.makeText(getActivity(), String.format("recyclerUsuarios.onCLick(): %d", position), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /*****************************************************
     * PUBLIC METHODS
     *****************************************************/
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuaios) {
        this.usuarios = usuaios;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
