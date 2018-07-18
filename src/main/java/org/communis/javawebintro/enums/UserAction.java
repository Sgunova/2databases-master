package org.communis.javawebintro.enums;

public enum UserAction {
    ACTION_1("Действие 1"), ACTION_2("Действие 2");

    private String friendlyName;

    UserAction(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
