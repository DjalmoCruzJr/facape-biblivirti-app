package org.sysmob.biblivirti.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sysmob.biblivirti.model.Grupo;

import java.util.List;

/**
 * Created by micro99 on 17/02/2017.
 */
public class PesquisarGruposFragment extends Fragment {

    private List<Grupo> grupos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.adapter_list_pesquisar_grupos, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*****************************************************
     * PUBLIC METHODS
     *****************************************************/
    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }
}
