package org.communis.javawebintro.entity;

import lombok.Data;
import org.communis.javawebintro.enums.UserRole;
import org.communis.javawebintro.enums.UserStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "surname")
    private String surname;

    @Column(name = "name")
    private String name;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "password")
    private String password;

    @Column(name = "mail")
    private String mail;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_create")
    private Date dateCreate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_block")
    private Date dateBlock;

    @Column(name = "DATE_LAST_ONLINE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLastOnline;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column
    private UserStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ldap")
    private LdapAuth ldapAuth;

    public Optional<LdapAuth> getLdapAuth() {
        return Optional.ofNullable(ldapAuth);
    }

}
