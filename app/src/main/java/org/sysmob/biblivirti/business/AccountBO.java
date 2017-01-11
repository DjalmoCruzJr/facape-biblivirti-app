package org.sysmob.biblivirti.business;

import android.app.Activity;

import org.sysmob.biblivirti.activities.LoginActivity;
import org.sysmob.biblivirti.business.interfaces.IAccountBO;
import org.sysmob.biblivirti.exceptions.ValidationException;

import java.util.regex.Pattern;

public class AccountBO implements IAccountBO {

    private Activity activity;

    public AccountBO(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean validateLogin() throws ValidationException {
        boolean status = true;

        // Validando o campo USCMAIL (email)
        if (((LoginActivity) activity).getEditEmail().getText() == null ||
                ((LoginActivity) activity).getEditEmail().getText().toString().trim().equals("")) {
            status = false;
            String ErrorMessage = "O E-MAIL é obrigatório!";
            ((LoginActivity) activity).getEditEmail().setError(ErrorMessage);
            throw new ValidationException(ErrorMessage);
        } else if (Pattern.compile("^(.+)@(.+)$").matcher(((LoginActivity) activity).getEditEmail().getText().toString().trim()).matches()) {
            status = false;
            String ErrorMessage = "Informe um E-MAIL válido!";
            ((LoginActivity) activity).getEditEmail().setError(ErrorMessage);
            throw new ValidationException(ErrorMessage);
        }

        // Validando o campo USCSENH (senha)
        if (((LoginActivity) activity).getEditSenha().getText() == null ||
                ((LoginActivity) activity).getEditSenha().getText().toString().trim().equals("")) {
            status = false;
            String ErrorMessage = "A SENHA é obrigatória!";
            ((LoginActivity) activity).getEditSenha().setError(ErrorMessage);
            throw new ValidationException(ErrorMessage);
        } else if (((LoginActivity) activity).getEditSenha().getText().toString().trim().contains(" ")) {
            status = false;
            String ErrorMessage = "A SENHA não pode conter espaço(s) em branco(s)!";
            ((LoginActivity) activity).getEditSenha().setError(ErrorMessage);
            throw new ValidationException(ErrorMessage);
        }

        return status;
    }

    @Override
    public boolean validateLoginFacebook() throws ValidationException {
        return false;
    }

    @Override
    public boolean validateRegister() throws ValidationException {
        return false;
    }

    @Override
    public boolean validateRecovery() throws ValidationException {
        return false;
    }

    @Override
    public boolean validatePasswordReset() throws ValidationException {
        return false;
    }

    @Override
    public boolean validateEmailConfirmation() throws ValidationException {
        return false;
    }

    @Override
    public boolean validatePasswordEdit() throws ValidationException {
        return false;
    }

    @Override
    public boolean validateProfile() throws ValidationException {
        return false;
    }

    @Override
    public boolean validateProfileEdit() throws ValidationException {
        return false;
    }

    @Override
    public boolean validateSearch() throws ValidationException {
        return false;
    }
}
