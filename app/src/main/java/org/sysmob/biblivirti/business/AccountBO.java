package org.sysmob.biblivirti.business;

import android.app.Activity;

import org.sysmob.biblivirti.business.interfaces.IAccountBO;
import org.sysmob.biblivirti.exceptions.ValidationException;

public class AccountBO implements IAccountBO {

    private Activity activity;

    public AccountBO(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean validateLogin() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateLoginFacebook() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateRegister() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateRecovery() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validatePasswordReset() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateEmailConfirmation() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validatePasswordEdit() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateProfile() throws ValidationException {
        boolean status = true;

        // Implentar regras de validacao desse metodo

        return status;
    }

    @Override
    public boolean validateProfileEdit() throws ValidationException {
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
}
