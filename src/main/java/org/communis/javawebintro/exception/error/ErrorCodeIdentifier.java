package org.communis.javawebintro.exception.error;

public class ErrorCodeIdentifier {
    private final String identifier;

    public ErrorCodeIdentifier(String identifier) {
        this.identifier = identifier;
    }

    private ErrorCodeIdentifier(ErrorCodeIdentifier identifier, String branchID) {
        this.identifier = identifier.getId() + "." + branchID;
    }

    public ErrorCodeIdentifier branch(String branchID) {
        return new ErrorCodeIdentifier(this, branchID);
    }

    public String getId() {
        return identifier;
    }
}
