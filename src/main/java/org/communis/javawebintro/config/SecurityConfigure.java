package org.communis.javawebintro.config;

import java.util.Arrays;
import org.communis.javawebintro.config.ldap.CustomLdapAuthenticationProvider;
import org.communis.javawebintro.service.LdapService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

    private final LdapService ldapService;

    public SecurityConfigure(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/static/**", "/webjars/**", "/badbrowser").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/logout").authenticated()
                .antMatchers("/login").anonymous().antMatchers("/**").permitAll();
//                .antMatchers("/**").authenticated();
        http.formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // указываем URL при неудачном логине
                .failureHandler(failureLoginHandler())
                .successHandler(successLoginHandler())
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("username")
                .passwordParameter("password")
                // даем доступ к форме логина всем
                .permitAll();
        http.logout()
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                // разрешаем делать логаут всем
                .permitAll()
                // делаем не валидной текущую сессию
                .invalidateHttpSession(true);
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(ldapAuthenticationProvider(), classicAuthenticationProvider()));
    }

    @Bean
    public AuthenticationProvider classicAuthenticationProvider() {
        return new ClassicAuthenticationProvider();
    }

    @Bean
    public AuthenticationSuccessHandler successLoginHandler() {
        return new SuccessLoginHandler("/");
    }

    @Bean
    public AuthenticationFailureHandler failureLoginHandler() {
        return new FailureLoginHandler();
    }


    @Bean
    public CustomLdapAuthenticationProvider ldapAuthenticationProvider() {
        return new CustomLdapAuthenticationProvider(ldapService);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
