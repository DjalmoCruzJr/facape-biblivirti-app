package org.sysmob.biblivirti.business.interfaces;

import org.sysmob.biblivirti.exceptions.ValidationException;

/**
 * Created by djalmocruzjr on 01/02/2017.
 */

public interface IGroupBO {

    public boolean validateListAll() throws ValidationException;

    public boolean validateAdd() throws ValidationException;

    public boolean validateEdit() throws ValidationException;

    public boolean validateDelete() throws ValidationException;

    public boolean validateInfo() throws ValidationException;

    public boolean validateSearch() throws ValidationException;

    public boolean validateSubscribe() throws ValidationException;

    public boolean validateUnsubscribe() throws ValidationException;

}
