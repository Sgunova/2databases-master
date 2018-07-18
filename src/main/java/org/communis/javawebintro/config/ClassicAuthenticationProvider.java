package org.communis.javawebintro.config;

import org.communis.javawebintro.service.UserService;
import org.communis.javawebintro.utils.CredentialsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

/**
 * Провайдер для аутентификации пользователя из базы данных
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ClassicAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Метод аутентицикации пользователя их базы данных
     *
     * @param a данные для аутентификации
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        try {
            String username = a.getName();
            String password = (String) a.getCredentials();
            if (!CredentialsUtil.isCredentialsValid(a)) {
                throw new BadCredentialsException("Неверное сочетание логина и пароля");
            }

            UserDetails userDetails = userService.loadUserByUsername(username);
            if (!encoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Неверное сочетание логина и пароля");
            }

            if (!userDetails.isAccountNonLocked()) {
                throw new BadCredentialsException("Аккаунт заблокирован");
            }

            if (!userDetails.isEnabled()) {
                throw new BadCredentialsException("Доступ к аккаунту закрыт");
            }

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
        } catch (UsernameNotFoundException ex) {
            throw new BadCredentialsException("Неверное сочетание логина и пароля");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

}
