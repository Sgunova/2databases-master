package org.communis.javawebintro.service;


import org.communis.javawebintro.dto.LdapAuthWrapper;
import org.communis.javawebintro.dto.LdapGroupInfo;
import org.communis.javawebintro.dto.filters.LdapAuthFilterWrapper;
import org.communis.javawebintro.entity.LdapAuth;
import org.communis.javawebintro.exception.ServerException;
import org.communis.javawebintro.exception.error.ErrorCodeConstants;
import org.communis.javawebintro.exception.error.ErrorInformationBuilder;
import org.communis.javawebintro.repository.LdapAuthRepository;
import org.communis.javawebintro.repository.LdapUserAttributesRepository;
import org.communis.javawebintro.repository.specifications.LdapAuthSpecification;
import org.communis.javawebintro.utils.LdapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = ServerException.class)
public class LdapService {

    private final LdapAuthRepository ldapAuthRepository;
    private final LdapUserAttributesRepository ldapUserAttributesRepository;

    @Autowired
    public LdapService(LdapAuthRepository ldapAuthRepository, LdapUserAttributesRepository ldapUserAttributesRepository) {
        this.ldapAuthRepository = ldapAuthRepository;
        this.ldapUserAttributesRepository = ldapUserAttributesRepository;
    }

    /**
     * Получает из базы страницу объектов {@link LdapAuthWrapper} в зависимости от информации о пагинаторе и параметрах фильтра
     *
     * @param filter   информация о фильтре
     * @param pageable информация о пагинаторе
     * @return страница объектов
     */
    public Page getPageByFilter(Pageable pageable, LdapAuthFilterWrapper filter) throws ServerException {
        try {
            return ldapAuthRepository.findAll(LdapAuthSpecification.build(filter), pageable)
                    .map(LdapAuthWrapper::new);
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_LIST_ERROR), ex);
        }
    }

    /**
     * Добавляет в базу информациб о новом ldap-сервере из объекта {@link LdapAuthWrapper}
     *
     * @param ldapAuthWrapper информация о новом ldap-сервере
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public Long add(LdapAuthWrapper ldapAuthWrapper) throws ServerException {
        try {
            ldapAuthWrapper.clearEmptyGroups();
            LdapAuth ldapAuth = new LdapAuth();
            ldapAuthWrapper.fromWrapper(ldapAuth);

            ldapAuth.setDateOpen(new Date());
            ldapAuth.setActive(true);
            ldapUserAttributesRepository.save(ldapAuth.getUserAttributes());
            return ldapAuthRepository.save(ldapAuth).getId();
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_ADD_ERROR), ex);
        }
    }

    /**
     * Обновляет информацию о ldap-сервере в базу из объекта {@link LdapAuthWrapper}
     *
     * @param ldapAuthWrapper информация о ldap-сервере
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void edit(LdapAuthWrapper ldapAuthWrapper) throws ServerException {
        try {
            LdapAuth ldapAuth = ldapAuthRepository.findOne(ldapAuthWrapper.getId());
            ldapAuthWrapper.fromWrapper(ldapAuth);
            System.out.println("ldapAuthWrapper:");
            System.out.println(ldapAuthWrapper);
            ldapUserAttributesRepository.save(ldapAuth.getUserAttributes());
            ldapAuthRepository.save(ldapAuth);
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_UPDATE_ERROR), ex);
        }
    }

    /**
     * Получает информацию о ldap-сервере по идентификатору из базы
     *
     * @param id идентификатор ldap-сервера
     * @return информация о ldap-сервере
     */
    public LdapAuthWrapper getForEdit(Long id) throws ServerException {
        try {
            return new LdapAuthWrapper(ldapAuthRepository.findOne(id));
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_INFO_ERROR), ex);
        }
    }

    public List<String> getGroups(LdapAuthWrapper wrapper) throws ServerException {
        try {
            LdapQuery query = LdapQueryBuilder.query()
                    .base(wrapper.getGroupsDirectory())
                    .attributes(LdapUtil.COMMON_NAME_ATTR)
                    .where(LdapUtil.OBJECT_CLASS_ATTR).is(wrapper.getGroupClass());

            return LdapUtil.executeLdapQuery(wrapper, query, LdapUtil.stringGroupsMapper);

        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_GET_GROUPS_ERROR), ex);
        }
    }

    public List<LdapGroupInfo> getGroupsInfo(LdapAuthWrapper wrapper) throws ServerException {
        try {
            LdapQuery query = LdapQueryBuilder.query()
                    .attributes(LdapUtil.COMMON_NAME_ATTR, LdapUtil.USER_ATTR_IN_GROUP)
                    .base(wrapper.getGroupsDirectory())
                    .where(LdapUtil.OBJECT_CLASS_ATTR).is(wrapper.getGroupClass());

            ContextMapper<LdapGroupInfo> contextMapper = ctx -> {
                DirContextAdapter dca = (DirContextAdapter) ctx;
                Attributes attributes = dca.getAttributes();

                return new LdapGroupInfo((String) attributes.get(LdapUtil.COMMON_NAME_ATTR).get(),
                        Optional.ofNullable(attributes.get(LdapUtil.USER_ATTR_IN_GROUP)).map(Attribute::size).orElse(0));
            };

            return LdapUtil.executeLdapQuery(wrapper, query, contextMapper);
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_GROUPS_ERROR), ex);
        }
    }

    /**
     * Получает из базы список всех активных ldap-серверов
     *
     * @return список всех активных ldap-серверов в виде объектов класса {@link LdapAuthWrapper}
     */
    public List<LdapAuthWrapper> getAllActive() throws ServerException {
        try {
            return ldapAuthRepository.findByActive(true).stream().map(LdapAuthWrapper::new).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_LIST_ERROR), ex);
        }
    }

    /**
     * Устанавливает ldap-серверу во флаг активности значение true и сохраняет изменения в базе
     *
     * @param id идентификатор ldap-сервера
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void activate(Long id) throws ServerException {
        try {
            LdapAuth auth = ldapAuthRepository.findOne(id);
            auth.setActive(true);
            ldapAuthRepository.save(auth);
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_ACTIVATE_ERROR), ex);
        }
    }

    /**
     * Устанавливает ldap-серверу во флаг активности значение false и сохраняет изменения в базе
     *
     * @param id идентификатор ldap-сервера
     * @return CustomHttpObject с кодом "OK" или с кодом "ERROR" и сообщением об ошибке
     */
    public void deactivate(Long id) throws ServerException {
        try {
            LdapAuth auth = ldapAuthRepository.findOne(id);
            auth.setActive(false);
            ldapAuthRepository.save(auth);
        } catch (Exception ex) {
            throw new ServerException(ErrorInformationBuilder.build(ErrorCodeConstants.LDAP_DEACTIVATE_ERROR), ex);
        }
    }
}