package org.communis.javawebintro.first.repository;

import java.util.List;
import org.communis.javawebintro.first.entity.FirstDataSourceEntity;
import org.communis.javawebintro.first.entity.FirstDataSourceWiredEntity;
import org.communis.javawebintro.repository.BaseEntityRepository;

/**
 * {@link FirstDataSourceEntity} entity repository
 */
public interface FirstDatabaseEntityRepository extends BaseEntityRepository<FirstDataSourceEntity, Long> {

    /**
     * Find {@link FirstDataSourceEntity} by {@link FirstDataSourceWiredEntity}
     */
    FirstDataSourceEntity findFirstByFirstDataSourceWiredEntityIs(FirstDataSourceWiredEntity entity);

    /**
     * Filter all {@link FirstDataSourceEntity} by {@link FirstDataSourceEntity#name}
     *
     * @param name
     * @return
     */
    List<FirstDataSourceEntity> findAllByNameContainsOrderByName(String name);

    /**
     * Find all unwired {@link FirstDataSourceEntity}
     */
    List<FirstDataSourceEntity> findAllByFirstDataSourceWiredEntityIsNull();
}
