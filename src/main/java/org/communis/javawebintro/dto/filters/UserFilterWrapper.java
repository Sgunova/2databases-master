package org.communis.javawebintro.dto.filters;


import org.communis.javawebintro.enums.UserRole;
import org.communis.javawebintro.enums.UserStatus;

public class UserFilterWrapper extends ObjectFilter
{
    private String mail;
    private UserRole role;
    private UserStatus status;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
