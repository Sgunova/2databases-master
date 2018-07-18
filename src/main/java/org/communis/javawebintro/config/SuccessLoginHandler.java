package org.communis.javawebintro.config;

import org.communis.javawebintro.config.ldap.CustomPerson;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс для обработки события удачной аутентификации
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SuccessLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private ClassicAuthenticationProvider classicAuthenticationProvider;
    @Autowired
    private SessionRegistry sessionRegistry;

    public SuccessLoginHandler(String defaultTargetUrl) {
        super(defaultTargetUrl);
    }

    /**
     * Обработчик события успешной авторизации.
     * Определяется тип аутентифицированного пользователя(ldap или БД).
     * Если тип ldap, происходит обновлениие или внесение данных пользователя в БД
     * и преобразование CustomPerson к UserDerailsImp.
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (!(authentication.getPrincipal() instanceof UserDetailsImp)) {
            UserDetails principal = (CustomPerson) authentication.getPrincipal();
            UserDetailsImp userDetailsImp;
            try {
                try {
                    UserDetailsImp userDetails = (UserDetailsImp) userService.loadUserByUsername(principal.getUsername());

                    if(userDetails.getUser().getIdLdap() == null) {
                        throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LDAP_EXIST_BD));
                    }

                    userDetailsImp = userService.updateUserFromLdap(userDetails.getUser().getId(), (CustomPerson) principal);
                } catch (UsernameNotFoundException ex) {
                    userDetailsImp = userService.addUserFromLdap((CustomPerson) principal);
                }
                authentication = new UsernamePasswordAuthenticationToken(userDetailsImp,
                        authentication.getCredentials(), userDetailsImp.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String sessionId = sessionRegistry.getAllSessions(principal, false).get(0).getSessionId();

                sessionRegistry.getAllSessions(principal, false).forEach(SessionInformation::expireNow);

                sessionRegistry.registerNewSession(sessionId, userDetailsImp);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }

        userService.updateLastTimeOnline(((UserDetailsImp) authentication.getPrincipal()).getUser().getId());

        response.setStatus(HttpStatus.OK.value());
    }
}
