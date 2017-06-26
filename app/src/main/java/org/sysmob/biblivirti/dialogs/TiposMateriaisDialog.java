package org.sysmob.biblivirti.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.adapters.TiposMateriaisDialogAdapter;
import org.sysmob.biblivirti.enums.ETipoMaterial;

import java.util.Arrays;

/**
 * Created by micro99 on 07/02/2017.
 */

public class TiposMateriaisDialog extends BottomSheetDialogFragment {

    private RecyclerView recyclerTiposMaterias;
    private TiposMateriaisDialogAdapter.OnItemClickListener onOptionsClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_tipo_materiais, container, false);

        this.recyclerTiposMaterias = (RecyclerView) view.findViewById(R.id.recyclerTiposMaterias);
        this.recyclerTiposMaterias.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        this.recyclerTiposMaterias.setHasFixedSize(true);
        this.recyclerTiposMaterias.setAdapter(new TiposMateriaisDialogAdapter(getActivity(), Arrays.asList(ETipoMaterial.values())));
        ((TiposMateriaisDialogAdapter) this.recyclerTiposMaterias.getAdapter()).setOnItemClickListener(this.onOptionsClickListener);
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
    public void setOnOptionsClickListener(TiposMateriaisDialogAdapter.OnItemClickListener onOptionsClickListener) {
        this.onOptionsClickListener = onOptionsClickListener;
    }

}
