package org.communis.javawebintro.exception.error;


public class ErrorInformationBuilder {

    public static ErrorInformation build(ErrorCodeIdentifier objectIdentifier) {
        String message = ErrorCodeConstants.messages.get(objectIdentifier);
        return build(objectIdentifier, message);
    }

    public static ErrorInformation build(ErrorCodeIdentifier objectIdentifier, String message) {
        return new ErrorInformation(objectIdentifier, (message == null) ? "" : message);
    }

    public static ErrorInformation buildConcat(ErrorCodeIdentifier objectIdentifier, String messageToConcat) {
        return build(objectIdentifier, String.format("%s %s", ErrorCodeConstants.messages.get(objectIdentifier), messageToConcat));
    }
}
