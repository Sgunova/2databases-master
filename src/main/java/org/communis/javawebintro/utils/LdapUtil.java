package org.communis.javawebintro.utils;

import org.communis.javawebintro.dto.LdapAuthWrapper;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;

import javax.naming.InvalidNameException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapName;
import java.util.List;

public class LdapUtil {

    public static final String USER_ATTR_IN_GROUP = "memberUid";
    public static final String COMMON_NAME_ATTR = "cn";
    public static final String OBJECT_CLASS_ATTR = "objectClass";
    public static final String USER_PASSWORD_ATTR = "userpassword";
    public static final String BLOCKED_SYMBOL = "*";
    public static final String LDAP_PASSWORD_ALGORITHM = "SHA";

    public static final ContextMapper<String> stringGroupsMapper = ctx -> {
        DirContextAdapter dca = (DirContextAdapter) ctx;
        Attributes attributes = dca.getAttributes();

        return (String) attributes.get(COMMON_NAME_ATTR).get();
    };

    public static <T> List<T> executeLdapQuery(LdapAuthWrapper wrapper, LdapQuery query, ContextMapper<T> mapper) {
        ContextSource ldapContextSource = getLdapContextSource(wrapper);
        LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource);

        return ldapTemplate.search(query, mapper);
    }

    public static LdapContextSource getLdapContextSource(LdapAuthWrapper wrapper) {
        String url = String.format("ldap://%s:%s/", wrapper.getAddress(), wrapper.getPort());

        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(url);
        contextSource.setBase(wrapper.getDomain());
        contextSource.setCacheEnvironmentProperties(false);

        if (wrapper.isCredentialsAuth()) {
            String principal = String.format("%s=%s,%s",
                    COMMON_NAME_ATTR, wrapper.getLdapLogin(), wrapper.getDomain());
            String password = wrapper.getLdapPassword();
            contextSource.setAuthenticationSource(new AuthenticationSource() {
                @Override
                public String getPrincipal() {
                    return principal;
                }

                @Override
                public String getCredentials() {
                    return password;
                }
            });
        } else {
            contextSource.setAnonymousReadOnly(true);
        }

        return contextSource;
    }

//    public static String hashPasswordForLdap(String password) throws CAException {
//        byte[] passwordHash = DigestUtil.digest(DigestAlgorithm.SHA1, password.getBytes());
//        return String.format("{%s}%s", LDAP_PASSWORD_ALGORITHM, new String(Base64.getEncoder().encode(passwordHash)));
//    }

    public static String getObjectDn(String cn, String base) {
        return String.format("%s=%s,%s", COMMON_NAME_ATTR, cn, base);
    }

    public static void modifyObject(LdapTemplate ldapTemplate, String dn, List<Attribute> attributes, int option) throws InvalidNameException {
        ModificationItem[] modificationItems = new ModificationItem[attributes.size()];
        int i = 0;

        for (Attribute attr:attributes) {
            modificationItems[i] = new ModificationItem(option, attr);
            i = i + 1;
        }

        ldapTemplate.modifyAttributes(new LdapName(dn), modificationItems);
    }
}
