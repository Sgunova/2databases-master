package org.communis.javawebintro.repository;

import org.communis.javawebintro.entity.LdapUserAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LdapUserAttributesRepository extends JpaRepository<LdapUserAttributes, Long> {
}
