package org.sysmob.biblivirti.business;

import android.app.Activity;

import org.sysmob.biblivirti.business.interfaces.IGroupBO;
import org.sysmob.biblivirti.exceptions.ValidationException;

/**
 * Created by djalmocruzjr on 01/02/2017.
 */
public class GroupBO implements IGroupBO {

    private Activity activity;

    public GroupBO(Activity activity) {
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

    @Override
    public boolean validateEdit() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateDelete() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateInfo() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateSearch() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateSubscribe() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateUnsubscribe() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }
}
