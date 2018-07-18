package org.communis.javawebintro.config.ldap;

import org.communis.javawebintro.dto.LdapAuthWrapper;
import org.communis.javawebintro.dto.LdapGroupWrapper;
import org.communis.javawebintro.service.LdapService;
import org.communis.javawebintro.utils.CredentialsUtil;
import org.communis.javawebintro.utils.LdapUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.ppolicy.PasswordPolicyException;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Провайдер для аутентификации через ldap
  */
@Configuration
public class CustomLdapAuthenticationProvider extends AbstractLdapAuthenticationProvider {

    /*
    Сервис для получения информации о ldap-серверах из базы данных
     */
    private LdapService ldapServiceBase;
    private List<LdapAuthWrapper> authWrapperList;
    private int currentIndex = 0;

    public CustomLdapAuthenticationProvider(LdapService ldapServiceBase) {
        this.ldapServiceBase = ldapServiceBase;
    }

    /**
     * Метод аутентификации.
     * Из базы в список загружаются ldap-сервера, на которых нужно искать пользователя.
     * Вызывает основной метод аутентификации.
     *
     * @param auth аутентификационные данные, введенные пользователем
     * @return
     */
    @Override
    protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
        try {
            authWrapperList = ldapServiceBase.getAllActive();
            if (authWrapperList.isEmpty() || !CredentialsUtil.isCredentialsValid(auth)) {
                throw new UsernameNotFoundException("Bad credentials");
            } else {
                currentIndex = 0;
                return authenticate(auth);
            }
        } catch (Exception ex) {
            throw new UsernameNotFoundException("Bad credentials");
        }
    }

    /**
     * Метод для поиска пользователя на серверах ldap по списку.
     *
     * @param auth аутентификационные данные, введенные пользователем
     * @return
     */
    private DirContextOperations authenticate(UsernamePasswordAuthenticationToken auth) {
        try {
            LdapAuthWrapper ldapAuthWrapper = authWrapperList.get(currentIndex);

            setUserDetailsContextMapper(new CustomPersonContextMapper(ldapAuthWrapper.getUserAttributes()));

            LdapContextSource ldapContextSource = LdapUtil.getLdapContextSource(ldapAuthWrapper);
            BindAuthenticator authenticator = new BindAuthenticator(ldapContextSource);

            authenticator.setUserSearch(username -> {
                SpringSecurityLdapTemplate template = new SpringSecurityLdapTemplate(ldapContextSource);
                LdapQuery query = LdapQueryBuilder.query()
                        .base(ldapAuthWrapper.getUsersDirectory())
                        .where(ldapAuthWrapper.getUserAttributes().getLogin()).is(username);

                return template.searchForContext(query);
            });

            return authenticator.authenticate(auth);
        } catch (PasswordPolicyException ppe) {
            throw new LockedException(this.messages.getMessage(
                    ppe.getStatus().getErrorCode(), ppe.getStatus().getDefaultMessage()));
        } catch (Exception ldapAccessFailure) {
            currentIndex++;
            if (currentIndex >= authWrapperList.size()) {
                throw new UsernameNotFoundException("Bad credentials");
            } else {
                return authenticate(auth);
            }
        }
    }

    /**
     * Назначает пользователю роль в соотвествии с правилами, указанными в ldapAuthWrapper.
     * Роли назначаются в таком порядке, в каком они указаны в {@link org.communis.javawebintro.enums.UserRole}
     *
     * @param userData данные пользователя из ldap
     * @param username логин
     * @param password пароль
     * @return
     */
    @Override
    protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        LdapAuthWrapper ldapAuthWrapper = authWrapperList.get(currentIndex);

        LdapQuery query = LdapQueryBuilder.query()
                .base(ldapAuthWrapper.getGroupsDirectory())
                .where(LdapUtil.OBJECT_CLASS_ATTR).is(ldapAuthWrapper.getGroupClass())
                .and(LdapUtil.USER_ATTR_IN_GROUP).is(username);

        List<String> groups = LdapUtil.executeLdapQuery(ldapAuthWrapper, query, LdapUtil.stringGroupsMapper);

        for (LdapGroupWrapper groupWrapper : ldapAuthWrapper.getUserGroups()) {
            for (String group : groupWrapper.getGroups()) {
                if (groups.stream().anyMatch(g -> g.contains(group))) {
                    authorities.add(new SimpleGrantedAuthority(groupWrapper.getRole()));
                    break;
                }
            }
            if (!authorities.isEmpty()) {
                break;
            }
        }

        return authorities;
    }
}
