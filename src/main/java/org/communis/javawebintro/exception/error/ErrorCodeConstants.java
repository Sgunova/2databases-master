package org.communis.javawebintro.exception.error;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodeConstants {

    public static final Map<ErrorCodeIdentifier, String> messages = new HashMap<>();

    public static final ErrorCodeIdentifier BASE = new ErrorCodeIdentifier("0");
    public static final ErrorCodeIdentifier DATA_NOT_FOUND = BASE.branch("1");
    public static final ErrorCodeIdentifier ACCESS_ERROR = BASE.branch("2");
    public static final ErrorCodeIdentifier DATA_VALIDATE_ERROR = BASE.branch("3");

    public static final ErrorCodeIdentifier LDAP = new ErrorCodeIdentifier("1");
    public static final ErrorCodeIdentifier LDAP_LIST_ERROR = LDAP.branch("1");
    public static final ErrorCodeIdentifier LDAP_INFO_ERROR = LDAP.branch("2");
    public static final ErrorCodeIdentifier LDAP_ADD_ERROR = LDAP.branch("3");
    public static final ErrorCodeIdentifier LDAP_UPDATE_ERROR = LDAP.branch("4");

    public static final ErrorCodeIdentifier LDAP_GROUPS_ERROR = LDAP.branch("5");
    public static final ErrorCodeIdentifier LDAP_GET_GROUPS_ERROR = LDAP_GROUPS_ERROR.branch("1");

    public static final ErrorCodeIdentifier LDAP_ACTIVATE_ERROR = LDAP.branch("6");
    public static final ErrorCodeIdentifier LDAP_DEACTIVATE_ERROR = LDAP.branch("7");

    public static final ErrorCodeIdentifier USER = new ErrorCodeIdentifier("2");
    public static final ErrorCodeIdentifier USER_LIST_ERROR = USER.branch("1");
    public static final ErrorCodeIdentifier USER_INFO_ERROR = USER.branch("2");

    public static final ErrorCodeIdentifier USER_ADD_ERROR = USER.branch("3");
    public static final ErrorCodeIdentifier USER_LOGIN_ALREADY_EXIST = USER_ADD_ERROR.branch("1");

    public static final ErrorCodeIdentifier USER_PASSWORD_ERROR = USER.branch("4");
    public static final ErrorCodeIdentifier USER_PASSWORD_LENGTH_ERROR = USER_PASSWORD_ERROR.branch("2");
    public static final ErrorCodeIdentifier USER_PASSWORD_COMPARE_ERROR = USER_PASSWORD_ERROR.branch("2");

    public static final ErrorCodeIdentifier USER_UPDATE_ERROR = USER.branch("5");
    public static final ErrorCodeIdentifier LDAP_GROUP_NOT_SPECIFIED = USER_UPDATE_ERROR.branch("1");

    public static final ErrorCodeIdentifier USER_BLOCK_ERROR = USER.branch("6");
    public static final ErrorCodeIdentifier USER_BLOCK_SELF_ERROR = USER_BLOCK_ERROR.branch("1");
    public static final ErrorCodeIdentifier USER_LDAP_NOT_EXIST = USER_BLOCK_ERROR.branch("2");

    public static final ErrorCodeIdentifier USER_UNBLOCK_ERROR = USER.branch("7");

    public static final ErrorCodeIdentifier USER_LDAP_EXIST_BD = USER.branch("8");

    static {
        messages.put(DATA_NOT_FOUND, "Ошибка при получении реестра ldap-серверов");
        messages.put(ACCESS_ERROR, "Доступ запрещен");
        messages.put(DATA_VALIDATE_ERROR, "Отправленные данные некорректны");

        messages.put(LDAP_LIST_ERROR, "Ошибка при получении реестра ldap-серверов");
        messages.put(LDAP_INFO_ERROR, "Ошибка при получении ldap-сервера");
        messages.put(LDAP_ADD_ERROR, "Ошибка при добавлении ldap-сервера");
        messages.put(LDAP_UPDATE_ERROR, "Ошибка при изменении ldap-сервера");

        messages.put(LDAP_GET_GROUPS_ERROR, "Ошибка при получении групп пользователей");

        messages.put(LDAP_ACTIVATE_ERROR, "Ошибка при активации ldap-сервера");
        messages.put(LDAP_DEACTIVATE_ERROR, "Ошибка при деактивации ldap-сервера");

        messages.put(USER_LIST_ERROR, "Ошибка при получении реестра пользователей");
        messages.put(USER_INFO_ERROR, "Ошибка при получении пользователя");

        messages.put(USER_ADD_ERROR, "Ошибка при добавлении пользователя");
        messages.put(USER_LOGIN_ALREADY_EXIST, "Логин занят другим пользователем");

        messages.put(USER_PASSWORD_ERROR, "Ошибка при изменении пароля пользователя");
        messages.put(USER_PASSWORD_LENGTH_ERROR, "Некорректная длина пароля");
        messages.put(USER_PASSWORD_COMPARE_ERROR, "Пароли не совпадают");

        messages.put(USER_UPDATE_ERROR, "Ошибка при изменении пользователя");
        messages.put(LDAP_GROUP_NOT_SPECIFIED, "Для указанной роли не установлено соответсвий LDAP-группам");

        messages.put(USER_BLOCK_ERROR, "Ошибка при блокировке пользователя");
        messages.put(USER_BLOCK_SELF_ERROR, "Нельзя заблокировать себя");
        messages.put(USER_LDAP_NOT_EXIST, "Не найден сервер LDAP связанный с пользователем");

        messages.put(USER_UNBLOCK_ERROR, "Ошибка при разблокировке пользователя");

        messages.put(USER_LDAP_EXIST_BD, "Невозможно добавить пользователя из ldap, " +
                "так как в БД уже сущесвует пользователь с другим типом авторизации");
    }
}
