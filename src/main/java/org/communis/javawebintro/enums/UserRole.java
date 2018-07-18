package org.communis.javawebintro.enums;

public enum UserRole {
    ROLE_ADMIN {
    },
    ROLE_OPERATOR {
    };

    public String getStringName() {
        switch (this) {
            case ROLE_ADMIN:
                return "Администратор";
            case ROLE_OPERATOR:
                return "Оператор";
            default:
                return null;
        }
    }

}