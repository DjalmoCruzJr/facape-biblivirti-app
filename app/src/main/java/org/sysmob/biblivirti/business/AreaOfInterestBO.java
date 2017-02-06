package org.sysmob.biblivirti.business;

import android.app.Activity;

import org.sysmob.biblivirti.business.interfaces.IAreaOfInterestBO;
import org.sysmob.biblivirti.exceptions.ValidationException;

public class AreaOfInterestBO implements IAreaOfInterestBO {

    private Activity activity;

    public AreaOfInterestBO(Activity activity) {
        this.activity = activity;
    }


    @Override
    public boolean validateListAll() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateAdd() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }
}
