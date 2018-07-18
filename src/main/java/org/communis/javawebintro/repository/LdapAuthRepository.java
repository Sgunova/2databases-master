package org.communis.javawebintro.repository;

import org.communis.javawebintro.entity.LdapAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LdapAuthRepository extends JpaRepository<LdapAuth, Long>, JpaSpecificationExecutor<LdapAuth>
{
    List<LdapAuth> findByActive(Boolean active);

    Optional<LdapAuth> findById(Long id);
}
