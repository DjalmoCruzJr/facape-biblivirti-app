package org.sysmob.biblivirti.business.interfaces;

import org.sysmob.biblivirti.exceptions.ValidationException;

public interface IAccountBO {

    public boolean validateLogin() throws ValidationException;

    public boolean validateLoginFacebook() throws ValidationException;

    public boolean validateRegister() throws ValidationException;

    public boolean validateRecovery() throws ValidationException;

    public boolean validatePasswordReset() throws ValidationException;

    public boolean validateEmailConfirmation() throws ValidationException;

    public boolean validatePasswordEdit() throws ValidationException;

    public boolean validateProfile() throws ValidationException;

    public boolean validateProfileEdit() throws ValidationException;

    public boolean validateSearch() throws ValidationException;

}
