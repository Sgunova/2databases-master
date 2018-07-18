package org.communis.javawebintro.second.repository;

import org.communis.javawebintro.repository.BaseEntityRepository;
import org.communis.javawebintro.second.entity.SecondDataSourceEntity;

import java.util.List;

public interface SecondDatabaseEntityRepository extends BaseEntityRepository<SecondDataSourceEntity, Long> {
    List<SecondDataSourceEntity> findAllByNameContainsOrderByName(String name);
}
