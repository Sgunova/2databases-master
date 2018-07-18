package org.communis.javawebintro.service;

import org.communis.javawebintro.dto.UserPasswordWrapper;
import org.communis.javawebintro.dto.UserWrapper;
import org.communis.javawebintro.entity.LdapAuth;
import org.communis.javawebintro.entity.User;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = ServerException.class)
public class PersonalService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public PersonalService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Изменяет информацию о текущем пользователе
     *
     * @param userWrapper новая информация о пользователе
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void edit(UserWrapper userWrapper) throws ServerException {
        try {
            User currentUser = userService.getCurrentUser();
            userWrapper.setRole(currentUser.getRole());
            userWrapper.setStatus(currentUser.getStatus());
            userWrapper.fromWrapper(currentUser);
            userRepository.save(currentUser);

            if (currentUser.getLdapAuth().isPresent()) {
                LdapAuth ldapAuth = currentUser.getLdapAuth()
                        .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UPDATE_ERROR)));

                userService.editLdapUser(currentUser, ldapAuth);
            }

        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UPDATE_ERROR), ex);
        }
    }

    /**
     * Изменяет пароль текущего пользователя
     * @param passwordWrapper инфомарция о пароле пользователе
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void changePassword(UserPasswordWrapper passwordWrapper) throws ServerException {
        try {
            User user = userRepository.findOne(userService.getCurrentUser().getId());

            if (user.getLdapAuth().isPresent()) {
                if (userService.validatePassword(passwordWrapper)) {
                    LdapAuth ldapAuth = user.getLdapAuth()
                            .orElseThrow(() -> new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_LDAP_NOT_EXIST)));
                    userService.editLdapUserPassword(user, passwordWrapper.getPassword(), ldapAuth);
                }
            } else {
                userService.changePassword(user, passwordWrapper);
            }
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.USER_UPDATE_ERROR), ex);
        }
    }
}
