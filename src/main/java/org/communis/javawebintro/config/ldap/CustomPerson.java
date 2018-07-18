package org.communis.javawebintro.config.ldap;

import org.communis.javawebintro.dto.LdapUserAttributesWrapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.Person;

/**
 * Класс для представления данных пользователя из ldap, наследник {@link LdapUserDetails}
 */
public class CustomPerson extends Person {

    private String mail;
    private String secondName;
    private Long idLdap;

    public String getMail() {
        return mail;
    }

    public String getSecondName() {
        return secondName;
    }

    public Long getIdLdap() {
        return idLdap;
    }

    protected void populateContext(DirContextAdapter adapter, LdapUserAttributesWrapper userAttributesWrapper) {
        super.populateContext(adapter);
        adapter.setAttributeValue(userAttributesWrapper.getName(), getGivenName());
        adapter.setAttributeValue(userAttributesWrapper.getSurname(), getSn());
        adapter.setAttributeValue(userAttributesWrapper.getSecondName(), getMail());
        adapter.setAttributeValue(userAttributesWrapper.getMail(), getSecondName());
    }

    public static class Essence extends Person.Essence {

        public Essence() {
        }

        /**
         * Заполняет поля класса CustomPerson из {@link DirContextOperations}
         */
        public Essence(DirContextOperations ctx, LdapUserAttributesWrapper userAttributesWrapper) {
            super(ctx);
            setGivenName(ctx.getStringAttribute(userAttributesWrapper.getName()));
            setSn(ctx.getStringAttribute(userAttributesWrapper.getSurname()));
            if (userAttributesWrapper.getSecondName() != null && !userAttributesWrapper.getSecondName().isEmpty()) {
                setSecondName(ctx.getStringAttribute(userAttributesWrapper.getSecondName()));
            }
            setMail(ctx.getStringAttribute(userAttributesWrapper.getMail()));
            setIdLdap(userAttributesWrapper.getIdLdap());
        }

        public Essence(CustomPerson copyMe) {
            super(copyMe);
            setMail(copyMe.mail);
        }

        public void setMail(String mail) {
            ((CustomPerson) instance).mail = mail;
        }

        public void setSecondName(String secondName) {
            ((CustomPerson) instance).secondName = secondName;
        }

        public void setIdLdap(Long idLdap) {((CustomPerson) instance).idLdap = idLdap;}

        public LdapUserDetails createUserDetails() {
            CustomPerson p = (CustomPerson) super.createUserDetails();
            return p;
        }

        protected LdapUserDetailsImpl createTarget() {
            return new CustomPerson();
        }
    }
}
