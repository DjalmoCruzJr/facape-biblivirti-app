package org.sysmob.biblivirti.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.model.Material;

import java.util.List;

public class MateriaisFragment extends Fragment {

    public static boolean hasDataChanged;

    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerMateriais;
    private FloatingActionButton buttonNovoMaterial;
    private List<Material> materiais;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita as opcoes de menu no fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materiais, container, false);

        // Falta implementar o carregamento dos widgets da view

        return view;
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/

    /********************************************************
     * PUBLIC METHODS
     *******************************************************/

    /********************************************************
     * ACTION METHODS
     *******************************************************/

}
