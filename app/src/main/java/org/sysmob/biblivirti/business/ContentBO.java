package org.sysmob.biblivirti.business;

import android.view.View;

import org.sysmob.biblivirti.business.interfaces.IContentBO;
import org.sysmob.biblivirti.exceptions.ValidationException;

public class ContentBO implements IContentBO {

    private View view;

    public ContentBO(View view) {
        this.view = view;
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

    @Override
    public boolean validateEdit() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }
}
