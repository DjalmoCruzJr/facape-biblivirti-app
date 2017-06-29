package org.sysmob.biblivirti.business.interfaces;

import org.sysmob.biblivirti.exceptions.ValidationException;

/**
 * Created by micro99 on 06/02/2017.
 */

public interface IMaterialBO {

    public boolean validateListAll() throws ValidationException;

    public boolean validateAdd() throws ValidationException;

    public boolean validateEdit() throws ValidationException;
}
