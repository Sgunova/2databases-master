package org.communis.javawebintro.dto;

import java.util.List;

public class LdapGroupWrapper {

    private String role;
    private List<String> groups;

    public LdapGroupWrapper() {

    }

    public LdapGroupWrapper(String role, List<String> groups) {
        this();
        this.role = role;
        this.groups = groups;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
