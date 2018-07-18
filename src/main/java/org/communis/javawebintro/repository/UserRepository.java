package org.communis.javawebintro.repository;

import org.communis.javawebintro.entity.User;
import org.communis.javawebintro.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
    Optional<User> findFirstByLogin(String login);

    Optional<User> findByRole(UserRole role);

    Optional<User> findById(Long id);

}
