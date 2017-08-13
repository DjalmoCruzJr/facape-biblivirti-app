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
import org.sysmob.biblivirti.adapters.PesquisarMateriaisFragmentAdapter;
import org.sysmob.biblivirti.model.Material;

import java.util.List;

/**
 * Created by micro99 on 17/02/2017.
 */
public class PesquisarMateriaisFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerMateriais;
    private List<Material> materiais;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisar_grupos, container, false);

        this.layoutEmpty = (LinearLayout) view.findViewById(R.id.layoutEmpty);
        this.layoutEmpty.setVisibility(this.materiais == null ? View.VISIBLE : View.GONE);
        this.recyclerMateriais = (RecyclerView) view.findViewById(R.id.recyclerGrupos);
        this.recyclerMateriais.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerMateriais.setHasFixedSize(true);
        this.recyclerMateriais.setAdapter(new PesquisarMateriaisFragmentAdapter(getContext(), this.materiais));
        ((PesquisarMateriaisFragmentAdapter) this.recyclerMateriais.getAdapter()).setOnItemClickListener(new PesquisarMateriaisFragmentAdapter.OnItemClickListener() {
            @Override
            public void onCLick(View view, int position) {
                Toast.makeText(getContext(), String.format("recyclerMateriais.onCLick(): %d", position), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /*****************************************************
     * PUBLIC METHODS
     *****************************************************/
    public List<Material> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<Material> materiais) {
        this.materiais = materiais;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
