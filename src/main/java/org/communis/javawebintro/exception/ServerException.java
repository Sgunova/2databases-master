package org.communis.javawebintro.exception;

import org.communis.javawebintro.exception.error.ErrorInformation;

public class ServerException extends Exception {
    private final ErrorInformation errorInformation;
    private String causeMessage;

    public ServerException(final ErrorInformation errorInformation) {
        super(errorInformation.getMessage());
        this.errorInformation = errorInformation;
    }

    public ServerException(final ErrorInformation errorInformation, Throwable cause) {
        super(errorInformation.getMessage(), cause);
        this.errorInformation = errorInformation;
        if (cause instanceof ServerException) {
            this.causeMessage = cause.getMessage();
        }
    }

    public String getFriendlyMessage() {
        return errorInformation.getFriendlyMessage() + (causeMessage != null ? ". " + causeMessage : "");
    }
}
