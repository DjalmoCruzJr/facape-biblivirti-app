package org.sysmob.biblivirti.business;

import android.app.Activity;

import org.sysmob.biblivirti.business.interfaces.IMaterialBO;
import org.sysmob.biblivirti.exceptions.ValidationException;

public class MaterialBO implements IMaterialBO {

    private Activity activity;

    public MaterialBO(Activity activity) {
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

        // Falta implementar as regras de validacao

        return status;
    }

    @Override
    public boolean validateEdit() throws ValidationException {
        boolean status = true;

        // Falta implementar as regras de validacao

        return status;
    }
}
