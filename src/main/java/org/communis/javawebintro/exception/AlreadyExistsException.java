package org.communis.javawebintro.exception;

import org.communis.javawebintro.exception.error.ErrorInformation;

public class AlreadyExistsException extends ServerException {

    public AlreadyExistsException(ErrorInformation errorInformation)
    {
        super(errorInformation);
    }
}
