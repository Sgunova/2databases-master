package org.communis.javawebintro.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс для обработки события неудачной аутентификации
 */
public class FailureLoginHandler extends SimpleUrlAuthenticationFailureHandler
{
    /**
     * Обрабатывает событие неудачной аутентификации
     * Устанавливает необходимые параметры в {@link HttpServletResponse}
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}