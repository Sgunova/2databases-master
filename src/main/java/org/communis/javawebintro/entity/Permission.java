package org.communis.javawebintro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.communis.javawebintro.enums.UserAction;
import org.communis.javawebintro.enums.UserRole;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private UserAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    public Permission(UserAction action, UserRole role) {
        this.action = action;
        this.role = role;
    }
}
