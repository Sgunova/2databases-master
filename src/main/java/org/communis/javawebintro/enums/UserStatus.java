package org.communis.javawebintro.enums;

public enum UserStatus {
    ACTIVE,
    BLOCK;

    public String getStringName() {
        switch(this) {
            case BLOCK:
                return "Заблокирован";
            case ACTIVE:
                return "Активен";
            default:
                return null;
        }
    }
}