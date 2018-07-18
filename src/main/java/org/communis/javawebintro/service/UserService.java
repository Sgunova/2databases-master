package org.communis.javawebintro.service;

import org.communis.javawebintro.config.UserDetailsImp;
import org.communis.javawebintro.config.ldap.CustomPerson;
import org.communis.javawebintro.dto.LdapAuthWrapper;
import org.communis.javawebintro.dto.UserPasswordWrapper;
import org.communis.javawebintro.dto.UserWrapper;
import org.communis.javawebintro.dto.filters.UserFilterWrapper;
import org.communis.javawebintro.entity.LdapAuth;
import org.communis.javawebintro.entity.LdapUserAttributes;
import org.communis.javawebintro.entity.User;
import org.communis.javawebintro.enums.UserRole;
import org.communis.javawebintro.enums.UserStatus;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.repository.LdapAuthRepository;
import org.communis.javawebintro.repository.PermissionRepository;
import org.communis.javawebintro.repository.UserRepository;
import org.communis.javawebintro.repository.specifications.UserSpecification;
import org.communis.javawebintro.utils.CredentialsUtil;
import org.communis.javawebintro.utils.LdapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ldap.AttributeInUseException;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import java.util.*;


@Service
@Transactional(rollbackFor = ServerException.class)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionRegistry sessionRegistry;
    private final LdapAuthRepository ldapAuthRepository;

    @Autowired
    public UserService(UserRepository userRepository, PermissionRepository permissionRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       @Qualifier("sessionRegistry") SessionRegistry sessionRegistry,
                       LdapAuthRepository ldapAuthRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sessionRegistry = sessionRegistry;
        this.ldapAuthRepository = ldapAuthRepository;
    }

    /**
     * Получает из базы пользователя по логину, его разрешения и формирует объект класса {@link UserDetailsImp}
     *
     * @param login логин пользователя
     * @return объект класса {@link UserDetailsImp}
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findFirstByLogin(login)
                .map(UserWrapper::new)
                .map(user -> new UserDetailsImp(user, permissionRepository.findActionsByRole(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCodeConstants.messages.get(ErrorCodeConstants.DATA_NOT_FOUND)));
    }

    /**
     * Обновляет инфомарцию о дате последнего входа в систему пользователя в базе
     *
     * @param id id пользователя
     */
    public void updateLastTimeOnline(Long id) {
        User user = userRepository.findOne(id);
        user.setDateLastOnline(new Date());
        userRepository.save(user);
    }

    /**
     * Получает пользователя из текущей сессии
     *
     * @return сущность пользователя
     */
    @Transactional(noRollbackFor = ServerException.class)
    public User getCurrentUser() throws ServerException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
            UserWrapper user = userDetails.getUser();
            return getUser(user.getId());
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_INFO_ERROR), ex);
        }
    }

    /**
     * Добавляет пользователя из ldap-сервера в базу
     *
     * @param person информация об атрибутах пользователя на ldap-сервере
     * @return информация о добавленном пользователе
     */
    public UserDetailsImp addUserFromLdap(CustomPerson person) throws ServerException {
        try {
            User user = new User();

            user = userFromLdap(user, person);

            user.setDateCreate(new Date());
            user.setStatus(UserStatus.ACTIVE);

            LdapAuth ldapAuth = ldapAuthRepository.findById(person.getIdLdap())
                    .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_ADD_ERROR)));
            user.setLdapAuth(ldapAuth);

            if(ldapAuth.getRoleFromGroup()){
                if(!person.getAuthorities().isEmpty())
                    user.setRole(UserRole.valueOf(((List<GrantedAuthority>) person.getAuthorities()).get(0).getAuthority()));
            }

            userRepository.save(user);

            return new UserDetailsImp(new UserWrapper(user),
                    permissionRepository.findActionsByRole(user.getRole()));
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_ADD_ERROR), ex);
        }
    }

    /**
     * Обновляет инфомарцию о пользователе из ldap-сервера в базе
     *
     * @param person информация об атрибутах пользователя на ldap-сервере
     * @return информация о обновленном пользователе
     */
    public UserDetailsImp updateUserFromLdap(Long id, CustomPerson person) throws ServerException {
        try {
            User user = userRepository.findOne(id);

            user = userFromLdap(user, person);

            LdapAuth ldapAuth = ldapAuthRepository.findById(person.getIdLdap())
                    .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_ADD_ERROR)));
            if(ldapAuth.getRoleFromGroup()){
                if(!person.getAuthorities().isEmpty())
                    user.setRole(UserRole.valueOf(((List<GrantedAuthority>) person.getAuthorities()).get(0).getAuthority()));
            }

            userRepository.save(user);

            return new UserDetailsImp(new UserWrapper(user),
                    permissionRepository.findActionsByRole(user.getRole()));
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UPDATE_ERROR), ex);
        }
    }

    /**
     * Добавляет информацию о новом пользователе в базу из {@link UserWrapper}
     *
     * @param userWrapper инфомарция о новом пользователе
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void add(UserWrapper userWrapper) throws ServerException {
        try {
            User user = new User();
            userWrapper.fromWrapper(user);
            if (userRepository.findFirstByLogin(user.getLogin()).isPresent()) {
                throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LOGIN_ALREADY_EXIST));
            }

            if (userWrapper.getIdLdap() != null) {
                LdapAuth ldapAuth = ldapAuthRepository.findById(userWrapper.getIdLdap())
                        .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_NOT_FOUND)));
                user.setLdapAuth(ldapAuth);
                if(!ldapAuth.isReadonly())
                    if (validatePassword(new UserPasswordWrapper(userWrapper))) {
                        user.setLdapAuth(ldapAuth);
                        String password = addUserInLdap(user, ldapAuth, userWrapper.getPassword());
                        user.setPassword(password);
                    }
            } else {
                changePassword(user, new UserPasswordWrapper(userWrapper));
            }

            user.setDateCreate(new Date());
            user.setStatus(UserStatus.ACTIVE);

            userRepository.save(user);
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_ADD_ERROR), ex);
        }
    }

    /**
     * Обновляет информацию о пользователе в базе из {@link UserWrapper}
     *
     * @param userWrapper инфомарция о пользователе
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void edit(UserWrapper userWrapper) throws ServerException {
        try {
            User user = getUser(userWrapper.getId());
            userWrapper.fromWrapper(user);
            userRepository.save(user);

            if (userWrapper.getIdLdap() != null) {
                LdapAuth ldapAuth = user.getLdapAuth()
                        .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UPDATE_ERROR)));

                user.setLdapAuth(ldapAuth);
                if(!ldapAuth.isReadonly())
                    editLdapUser(user, ldapAuth);
            }
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UPDATE_ERROR), ex);
        }
    }

    /**
     * Устанавливает пользователю с заданным идентификатором статус "BLOCK(Заблокирован)", если это не текущий пользователь системы
     *
     * @param id идентификатор пользователя
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void block(Long id) throws ServerException {
        try {
            User user = getUser(id);

            if (user == getCurrentUser()) {
                throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_BLOCK_SELF_ERROR));
            }

            user.setDateBlock(new Date());
            user.setStatus(UserStatus.BLOCK);
            userRepository.save(user);
            Optional<Object> userPrincipal = sessionRegistry.getAllPrincipals().stream()
                    .filter(p -> Objects.equals(((UserDetailsImp) p).getUser().getId(), user.getId()))
                    .findFirst();
            userPrincipal.ifPresent(o -> sessionRegistry.getAllSessions(o, false)
                    .forEach(SessionInformation::expireNow));

            if (user.getLdapAuth().isPresent()) {
                LdapAuth ldapAuth = user.getLdapAuth()
                        .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LDAP_NOT_EXIST)));
                blockUserInLdap(user.getLogin(), new LdapAuthWrapper(ldapAuth));
            }
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_BLOCK_ERROR), ex);
        }
    }

    /**
     * Устанавливает пользователю с заданным идентификатором статус "ACTIVE(Активен)"
     *
     * @param id идентификатор пользователя
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void unblock(Long id) throws ServerException {
        try {
            User user = getUser(id);
            user.setDateBlock(null);
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);

            if (user.getLdapAuth().isPresent()) {
                LdapAuth ldapAuth = user.getLdapAuth()
                        .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LDAP_NOT_EXIST)));
                unblockUserInLdap(user.getLogin(), new LdapAuthWrapper(ldapAuth));
            }
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UNBLOCK_ERROR), ex);
        }
    }

    /**
     * Изменяет пароль пользователя в соответсвии с информацией из {@link UserWrapper}
     *
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void changePassword(UserPasswordWrapper passwordWrapper)
            throws ServerException {
        try {
            User user = getUser(passwordWrapper.getId());

            if (user.getLdapAuth().isPresent()) {
                if (validatePassword(passwordWrapper)) {
                    LdapAuth ldapAuth = user.getLdapAuth()
                            .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LDAP_NOT_EXIST)));
                    editLdapUserPassword(user, passwordWrapper.getPassword(), ldapAuth);
                }
            } else {
                changePassword(user, passwordWrapper);
            }
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_PASSWORD_ERROR), ex);
        }
    }

    /**
     * Изменяет пароль пользователя в соответсвии с информацией из {@link UserWrapper}.
     * Пароль не меняется если задан пароль короче 8 символом или пароль не совпадает с паролем подтвеждения
     *
     * @param passwordWrapper информация о пароле пользователя
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void changePassword(User user, UserPasswordWrapper passwordWrapper) throws ServerException {
        if (validatePassword(passwordWrapper)) {
            user.setPassword(bCryptPasswordEncoder.encode(passwordWrapper.getPassword()));
        }
    }

    public boolean validatePassword(UserPasswordWrapper passwordWrapper) throws ServerException {
        if (passwordWrapper.getPassword().length() < CredentialsUtil.PASSWORD_MIN_LENGTH) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_PASSWORD_LENGTH_ERROR));
        }
        if (!Objects.equals(passwordWrapper.getPassword(), passwordWrapper.getConfirmPassword())) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_PASSWORD_COMPARE_ERROR));
        }
        return true;
    }

    /**
     * Получает информацию о пользователе с заданным идентификатором из базы и пребразует ее в объект класса {@link UserWrapper}
     *
     * @param id идентификатор пользователя
     * @return объект, содержащий информацию о пользователе
     */
    public UserWrapper getById(Long id) throws ServerException {
        try {
            return new UserWrapper(getUser(id));
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_INFO_ERROR), ex);
        }
    }

    /**
     * Получает из базы страницу объектов {@link UserWrapper} в зависимости от информации о пагинаторе и параметрах фильтра
     *
     * @param pageable          информация о пагинаторе
     * @param filterUserWrapper информация о фильтре
     * @return страница объектов
     */
    public Page getPageByFilter(Pageable pageable, UserFilterWrapper filterUserWrapper) throws ServerException {
        try {
            return userRepository.findAll(UserSpecification.build(filterUserWrapper), pageable)
                    .map(UserWrapper::new);
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LIST_ERROR), ex);
        }
    }

    private User getUser(Long id) throws ServerException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.DATA_NOT_FOUND)));
    }

    private User userFromLdap(User user, CustomPerson person) {
        user.setLogin(person.getUsername());
        user.setName(person.getGivenName());
        user.setPassword(person.getPassword() != null ? person.getPassword() : "");
        user.setSurname(person.getSn());
        user.setMail(person.getMail());

        return user;
    }

    private String addUserInLdap(User user, LdapAuth ldapAuth, String password) throws InvalidNameException, ServerException {
        throw new UnsupportedOperationException();
    }

    private void blockUserInLdap(String login, LdapAuthWrapper wrapper) throws NamingException {
        throw new UnsupportedOperationException();
    }

    private void unblockUserInLdap(String login, LdapAuthWrapper wrapper) throws NamingException {
        throw new UnsupportedOperationException();
    }

    private DirContextOperations getLdapUserData(String login, LdapAuthWrapper wrapper) {
        LdapQuery query = LdapQueryBuilder.query()
                .base(wrapper.getUsersDirectory())
                .attributes(LdapUtil.USER_PASSWORD_ATTR)
                .where(wrapper.getUserAttributes().getLogin()).is(login);

        LdapTemplate ldapTemplate = new LdapTemplate(LdapUtil.getLdapContextSource(wrapper));

        return ldapTemplate.searchForContext(query);
    }

    private DirContextAdapter createLdapUser(User user, String password, LdapAuth ldapAuth)
            throws InvalidNameException {
        throw new UnsupportedOperationException();
    }

    private void addUserToGroup(LdapTemplate template, String login, String group, LdapAuth ldapAuth) throws InvalidNameException {
        BasicAttribute userLink = new BasicAttribute(LdapUtil.USER_ATTR_IN_GROUP, login);
        String objectDn = LdapUtil.getObjectDn(group, ldapAuth.getGroupsDirectory());

        try {
            LdapUtil.modifyObject(template, objectDn, Collections.singletonList(userLink), DirContext.ADD_ATTRIBUTE);
        } catch (AttributeInUseException ignored) {

        }
    }

    private void removeUserFromGroup(LdapTemplate template, String login, String group, LdapAuth ldapAuth) throws InvalidNameException {
        try {
            BasicAttribute userLink = new BasicAttribute(LdapUtil.USER_ATTR_IN_GROUP, login);
            String objectDn = LdapUtil.getObjectDn(group, ldapAuth.getGroupsDirectory());

            LdapUtil.modifyObject(template, objectDn, Collections.singletonList(userLink), DirContext.REMOVE_ATTRIBUTE);
        } catch (org.springframework.ldap.NoSuchAttributeException ignored) {

        }
    }

    public void editLdapUser(User user, LdapAuth ldapAuth) throws InvalidNameException, ServerException {
        throw new UnsupportedOperationException();
    }

    public void editLdapUserPassword(User user, String password, LdapAuth ldapAuth) throws InvalidNameException {
        throw new UnsupportedOperationException();
    }

    private List<Attribute> createLdapUser(User user, LdapUserAttributes userAttributes) {
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new BasicAttribute(LdapUtil.COMMON_NAME_ATTR, user.getLogin()));
        attributes.add(new BasicAttribute(userAttributes.getLogin(), user.getLogin()));
        attributes.add(new BasicAttribute(userAttributes.getName(), user.getName()));
        attributes.add(new BasicAttribute(userAttributes.getSurname(), user.getSurname()));
        userAttributes.getSecondName().ifPresent(sn -> {
            if (!sn.isEmpty())
                attributes.add(new BasicAttribute(sn, user.getSecondName()));
        });
        attributes.add(new BasicAttribute(userAttributes.getMail(), user.getMail()));

        return attributes;
    }


}
