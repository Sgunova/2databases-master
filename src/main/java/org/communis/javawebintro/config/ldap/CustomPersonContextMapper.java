package org.communis.javawebintro.config.ldap;

import org.communis.javawebintro.dto.LdapUserAttributesWrapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Класс для заполнения {@link CustomPerson}
 */
public class CustomPersonContextMapper implements UserDetailsContextMapper {

    private LdapUserAttributesWrapper userAttributesWrapper;

    public CustomPersonContextMapper(LdapUserAttributesWrapper userAttributesWrapper) {
        this.userAttributesWrapper = userAttributesWrapper;
    }

    /**
     * Создает класс CustomPerson из ldap-контекста
     */
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                          Collection<? extends GrantedAuthority> authorities) {
        CustomPerson.Essence p = new CustomPerson.Essence(ctx, userAttributesWrapper);

        p.setUsername(username);
        p.setAuthorities(authorities);

        return p.createUserDetails();

    }

    /**
     * Создает контекст из класса CustomPerson
     */
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        Assert.isInstanceOf(CustomPerson.class, user, "UserDetails must be a CustomPerson instance");

        CustomPerson p = (CustomPerson) user;
        p.populateContext(ctx, userAttributesWrapper);
    }

    public LdapUserAttributesWrapper getUserAttributesWrapper() {
        return userAttributesWrapper;
    }

    public void setUserAttributesWrapper(LdapUserAttributesWrapper userAttributesWrapper) {
        this.userAttributesWrapper = userAttributesWrapper;
    }
}