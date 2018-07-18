package org.communis.javawebintro.exception;


import org.communis.javawebintro.exception.error.ErrorInformation;

public class NotFoundException extends ServerException {
    public NotFoundException(ErrorInformation errorInformation) {
        super(errorInformation);
    }
}
