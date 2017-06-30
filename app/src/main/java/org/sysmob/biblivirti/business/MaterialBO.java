package org.sysmob.biblivirti.business;

import android.view.View;
import android.widget.EditText;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.business.interfaces.IMaterialBO;
import org.sysmob.biblivirti.exceptions.ValidationException;

public class MaterialBO implements IMaterialBO {

    private View activity;

    public MaterialBO(View activity) {
        this.activity = activity;
    }


    @Override
    public boolean validateListAll() throws ValidationException {
        boolean status = true;

        // Falta implementar as regras de validacao

        return status;
    }

    @Override
    public boolean validateAdd() throws ValidationException {
        boolean status = true;

        if (((EditText) activity.findViewById(R.id.editMACURL)).getText().toString().trim().equals("")) {
            status = false;
            ((EditText) activity.findViewById(R.id.editMACURL)).setError("A URL do material deve ser informada!");
        }

        return status;
    }

    @Override
    public boolean validateEdit() throws ValidationException {
        boolean status = true;

        // Falta implementar as regras de validacao

        return status;
    }
}
