package org.communis.javawebintro.config;

import org.communis.javawebintro.dto.UserWrapper;
import org.communis.javawebintro.enums.UserAction;
import org.communis.javawebintro.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Класс для представления данных пользователя из текущей сессии
 */
public class UserDetailsImp implements UserDetails {

    private final UserWrapper user;

    private Set<GrantedAuthority> authorities;

    public UserDetailsImp(UserWrapper user, List<UserAction> actions) {
        if(user==null) {
            throw new NullPointerException("UserWrapper is NULL");
        }
        this.user = user;

        authorities = new HashSet<>();
        if(user.getRole() != null)
            authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        actions.forEach(a -> authorities.add(new SimpleGrantedAuthority(a.name())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername () {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.BLOCK;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }

    public UserWrapper getUser() {
        return user;
    }

}