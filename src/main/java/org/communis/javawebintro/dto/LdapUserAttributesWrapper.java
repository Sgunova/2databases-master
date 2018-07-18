package org.communis.javawebintro.dto;

import lombok.Getter;
import lombok.Setter;
import org.communis.javawebintro.entity.LdapUserAttributes;

@Getter
@Setter
public class LdapUserAttributesWrapper implements ObjectWrapper<LdapUserAttributes> {

    private Long idLdap;
    private String name = "givenName";
    private String surname = "sn";
    private String secondName;
    private String mail = "mail";
    private String login = "uid";

    public LdapUserAttributesWrapper() {

    }

    public LdapUserAttributesWrapper(LdapUserAttributes item) {
        this();
        toWrapper(item);
    }

    @Override
    public void toWrapper(LdapUserAttributes item) {
        if (item != null) {
            name = item.getName();
            surname = item.getSurname();
            secondName = item.getSecondName().orElse(null);
            mail = item.getMail();
            login = item.getLogin();
        }
    }

    @Override
    public void fromWrapper(LdapUserAttributes item) {
        if (item != null) {
            item.setName(name);
            item.setSurname(surname);
            item.setSecondName(secondName);
            item.setMail(mail);
            item.setLogin(login);
        }
    }
}
