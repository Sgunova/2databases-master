package org.communis.javawebintro.repository;

import org.communis.javawebintro.entity.Permission;
import org.communis.javawebintro.enums.UserAction;
import org.communis.javawebintro.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(value = "SELECT perm.action FROM Permission perm WHERE perm.role=:role")
    List<UserAction> findActionsByRole(@Param("role") UserRole role);

    Permission findByActionAndRole(UserAction action, UserRole role);
}
