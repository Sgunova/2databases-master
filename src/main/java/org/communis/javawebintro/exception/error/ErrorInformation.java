package org.communis.javawebintro.exception.error;

public class ErrorInformation {

    private final ErrorCodeIdentifier code;
    private final String message;

    public ErrorInformation(ErrorCodeIdentifier code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorCodeIdentifier getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFriendlyMessage() {
        return "Ошибка № " + code.getId() + ". " + message;
    }
}
