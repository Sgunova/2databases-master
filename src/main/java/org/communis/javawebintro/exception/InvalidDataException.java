package org.communis.javawebintro.exception;

import org.communis.javawebintro.exception.error.ErrorInformation;

public class InvalidDataException extends ServerException {

    public InvalidDataException(ErrorInformation errorInformation) {
        super(errorInformation);
    }
}

