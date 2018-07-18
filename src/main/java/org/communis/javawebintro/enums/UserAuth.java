package org.communis.javawebintro.enums;

public enum UserAuth {
    BD("База данных"), LDAP("LDAP");

    private String stringName;

    UserAuth(String stringName) {
        this.stringName = stringName;
    }

    public String getStringName() {
        return stringName;
    }
}