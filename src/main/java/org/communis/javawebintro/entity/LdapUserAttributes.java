package org.communis.javawebintro.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Optional;

@Data
@Entity
@Table(name = "ldap_user_attributes")
public class LdapUserAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "mail")
    private String mail;

    @Column(name = "login")
    private String login;

    public Optional<String> getSecondName() {
        return Optional.ofNullable(secondName);
    }
}
