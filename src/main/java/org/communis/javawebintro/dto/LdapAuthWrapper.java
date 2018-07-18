package org.communis.javawebintro.dto;


import lombok.Data;
import org.communis.javawebintro.entity.LdapAuth;
import org.communis.javawebintro.entity.LdapUserAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class LdapAuthWrapper implements ObjectWrapper<LdapAuth> {

    private Long id;
    private Date dateOpen;
    private String name;
    private String address;
    private String port = "389";
    private String groupsDirectory;
    private String usersDirectory;
    private List<LdapGroupWrapper> userGroups = new ArrayList<>();
    private LdapUserAttributesWrapper userAttributes = new LdapUserAttributesWrapper();
    private boolean active = false;
    private String domain;
    private String groupClass = "posixGroup";
    private String userClass = "inetOrgPerson";
    private String ldapLogin;
    private String ldapPassword;
    private boolean credentialsAuth = false;
    private boolean roleFromGroup = false;
    private boolean readonly = true;

    public LdapAuthWrapper() {}

    public LdapAuthWrapper(LdapAuth log)
    {
        toWrapper(log);
    }
    
    @Override
    public void toWrapper(LdapAuth item)
    {
        if (item != null)
        {
            id = item.getId();
            dateOpen = item.getDateOpen();
            name = item.getName();
            address = item.getAddress();
            port = item.getPort();
            groupsDirectory = item.getGroupsDirectory();
            usersDirectory = item.getUsersDirectory();
            active = item.getActive();
            domain = item.getDomain();
            userGroups = parseUserGroups(item.getRolesReferences());
            groupClass = item.getGroupClass();
            userClass = item.getUserClass();
            ldapLogin = item.getLdapLogin();
            ldapPassword = item.getLdapPassword();
            credentialsAuth = item.isCredentialsAuth();
            roleFromGroup = item.getRoleFromGroup();
            readonly = item.isReadonly();
            userAttributes = new LdapUserAttributesWrapper(item.getUserAttributes());
            userAttributes.setIdLdap(id);
        }
    }

    @Override
    public void fromWrapper(LdapAuth item)
    {
        if (item != null) {
            item.setName(name);
            item.setAddress(address);
            item.setPort(port);
            item.setGroupsDirectory(groupsDirectory);
            item.setUsersDirectory(usersDirectory);
            item.setDomain(domain);
            item.setGroupClass(groupClass);
            item.setUserClass(userClass);
            item.setLdapLogin(ldapLogin);
            item.setLdapPassword(ldapPassword);
            item.setCredentialsAuth(credentialsAuth);
            item.setRolesReferences(getRolesReferences());
            item.setRoleFromGroup(roleFromGroup);
            item.setReadonly(readonly);

            LdapUserAttributes attrs=new LdapUserAttributes();
            attrs.setId(id);
            userAttributes.fromWrapper(attrs);
            item.setUserAttributes(attrs);
        }
    }

    public static List<LdapGroupWrapper> parseUserGroups(String rolesReferences) {
        List<LdapGroupWrapper> groupWrapperList = new ArrayList<>();

        if (!rolesReferences.isEmpty()) {
            String[] rolesGroups = rolesReferences.split(";");
            for (String roleGroup : rolesGroups) {
                String[] split = roleGroup.split("=");
                List<String> groups = new ArrayList<>();
                if(split.length > 1) {
                    groups = new ArrayList<>(Arrays.asList(split[1].length() > 1 ?
                            split[1].split(",") : new String[]{split[1]}));
                }

                LdapGroupWrapper groupWrapper = new LdapGroupWrapper();
                groupWrapper.setRole(split[0]);
                groupWrapper.setGroups(groups);

                groupWrapperList.add(groupWrapper);
            }
        }
        return groupWrapperList;
    }

    private String getRolesReferences() {
        return userGroups.stream().map(ldapGroupWrapper -> ldapGroupWrapper.getGroups() != null ?
                ldapGroupWrapper.getRole() + "=" +
                        ldapGroupWrapper.getGroups().stream().collect(Collectors.joining(",")) +
                        ";" : "").collect(Collectors.joining());
    }

    public Set<String> getGroups() {
        Set<String> groups = new HashSet<>();
        for (LdapGroupWrapper groupWrapper : userGroups) {
            groups.addAll(groupWrapper.getGroups());
        }
        return groups;
    }

    public void clearEmptyGroups() {
        userGroups.removeIf(group -> group.getGroups().isEmpty());
    }

}