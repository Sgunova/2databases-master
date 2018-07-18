package org.communis.javawebintro.utils;

import org.springframework.security.core.Authentication;

public class CredentialsUtil {

    private static final int LOGIN_MAX_LENGTH = 20;
    private static final int PASSWORD_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 8;

    public static boolean isCredentialsValid(Authentication auth) {
        return ((String) auth.getPrincipal()).length() <= LOGIN_MAX_LENGTH
                && ((String) auth.getCredentials()).length() <= PASSWORD_MAX_LENGTH;
    }
}
