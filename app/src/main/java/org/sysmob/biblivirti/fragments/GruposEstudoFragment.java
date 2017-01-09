package org.sysmob.biblivirti.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sysmob.biblivirti.R;

public class GruposEstudoFragment extends Fragment {

    public GruposEstudoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grupos_estudo, container, false);
    }

}
