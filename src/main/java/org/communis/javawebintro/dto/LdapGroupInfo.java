package org.communis.javawebintro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LdapGroupInfo {

    private String name;
    private int usersCount;
}
