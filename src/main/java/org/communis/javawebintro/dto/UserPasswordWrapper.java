package org.communis.javawebintro.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserPasswordWrapper {

    private Long id;

    @JsonIgnore
    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @JsonIgnore
    @NotNull
    @Size(min = 8, max = 20)
    private String confirmPassword;

    public UserPasswordWrapper() {

    }

    public UserPasswordWrapper(UserWrapper userWrapper) {
        id = userWrapper.getId();
        password = userWrapper.getPassword();
        confirmPassword = userWrapper.getConfirmPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
