package org.communis.javawebintro.first.repository;

import java.util.List;
import org.communis.javawebintro.first.entity.FirstDataSourceEntity;
import org.communis.javawebintro.first.entity.FirstDataSourceWiredEntity;
import org.communis.javawebintro.repository.BaseEntityRepository;

/**
 * Repository for {@link FirstDataSourceWiredEntity}
 */
public interface FirstDataSourceWiredEntityRepository extends BaseEntityRepository<FirstDataSourceWiredEntity, Long> {

    /**
     * Find {@link FirstDataSourceWiredEntity} by {@link FirstDataSourceEntity}
     */
    FirstDataSourceWiredEntity findFirstByFirstDataSourceEntityIs(FirstDataSourceEntity entity);

    /**
     * Filter {@link FirstDataSourceWiredEntity} by name
     */
    List<FirstDataSourceWiredEntity> findAllByNameContainsOrderByName(String query);

    /**
     * Find all unlinked {@link FirstDataSourceWiredEntity}
     */
    List<FirstDataSourceWiredEntity> findAllByFirstDataSourceEntityIsNull();

    /**
     * Find all avaliable for linkin {@link FirstDataSourceWiredEntity}
     */
    List<FirstDataSourceWiredEntity> findAllByFirstDataSourceEntityIsNot(FirstDataSourceWiredEntity entity);
}
