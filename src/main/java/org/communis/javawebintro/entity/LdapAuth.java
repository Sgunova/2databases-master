package org.communis.javawebintro.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ldap_auth")
public class LdapAuth 
{  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    
    @Column(name = "date_open")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpen;
    
    @Column
    private String address;

    @Column
    private String port;

    @Column
    private String name;

    @Column(name = "groups_directory")
    private String groupsDirectory;

    @Column(name = "users_directory")
    private String usersDirectory;
    
    @Column(name = "active")
    private Boolean active;

    @Column(name = "role_from_group")
    private Boolean roleFromGroup;

    @Column(name = "role_references")
    private String rolesReferences;

    @Column(name = "domain")
    private String domain;

    @Column(name = "group_class")
    private String groupClass;

    @Column(name = "user_class")
    private String userClass;

    @Column(name = "ldap_login")
    private String ldapLogin;

    @Column(name = "ldap_password")
    private String ldapPassword;

    @Column(name = "credentials_auth")
    private boolean credentialsAuth;

    @Column
    private boolean readonly;

    @JoinColumn(name = "user_attributes")
    @OneToOne
    private LdapUserAttributes userAttributes;
}
