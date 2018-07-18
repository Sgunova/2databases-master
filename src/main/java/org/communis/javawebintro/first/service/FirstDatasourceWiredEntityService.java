package org.communis.javawebintro.first.service;

import java.util.List;
import org.communis.javawebintro.first.entity.FirstDataSourceEntity;
import org.communis.javawebintro.first.entity.FirstDataSourceWiredEntity;
import org.communis.javawebintro.first.repository.FirstDataSourceWiredEntityRepository;
import org.communis.javawebintro.first.repository.FirstDatabaseEntityRepository;
import org.communis.javawebintro.service.CrudService;
import org.springframework.stereotype.Service;

/**
 * Service to work with {@link FirstDataSourceEntity} entities
 */
@Service
public class FirstDatasourceWiredEntityService implements CrudService<FirstDataSourceWiredEntity, Long> {

    /**
     * {@link FirstDataSourceWiredEntityRepository}
     */
    private final FirstDataSourceWiredEntityRepository repository;
    /**
     * {@link FirstDatabaseEntityRepository}
     */
    private final FirstDatabaseEntityRepository parentRepository;

    /**
     * c-tor
     */
    public FirstDatasourceWiredEntityService(
            FirstDataSourceWiredEntityRepository repository,
            FirstDatabaseEntityRepository parentRepository) {
        this.repository = repository;
        this.parentRepository = parentRepository;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Long save(FirstDataSourceWiredEntity entity) {
        if (entity != null) {
            final FirstDataSourceEntity parentEntity = entity.getFirstDataSourceEntity();
            if (parentEntity != null &&
                    parentEntity.getFirstDataSourceWiredEntity() != null &&
                    !parentEntity.getFirstDataSourceWiredEntity().equals(entity)) {
                final FirstDataSourceEntity toUpdate = parentRepository.findOne(parentEntity.getId());
                if (toUpdate.getFirstDataSourceWiredEntity() != null) {
                    toUpdate.getFirstDataSourceWiredEntity().setFirstDataSourceEntity(null);
                    repository.save(toUpdate.getFirstDataSourceWiredEntity());
                }
                toUpdate.setFirstDataSourceWiredEntity(null);
                parentRepository.save(toUpdate);
            }
            final FirstDataSourceWiredEntity saved = repository.save(entity);
            if (parentEntity != null) {
                parentEntity.setFirstDataSourceWiredEntity(saved);
            }
            saved.setFirstDataSourceEntity(parentEntity);
            return repository.save(saved).getId();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FirstDataSourceWiredEntity findById(Long aLong) {
        return repository.findOne(aLong);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FirstDataSourceWiredEntity> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long update(FirstDataSourceWiredEntity entity) {
        if (entity != null) {
            final FirstDataSourceWiredEntity toUpdate = repository.findOne(entity.getId());
            // check if entity was already binded to child and unwire
            if (toUpdate.getFirstDataSourceEntity() != null) {
                final FirstDataSourceEntity parent = toUpdate.getFirstDataSourceEntity();
                if (parent.getFirstDataSourceWiredEntity() != null) {
                    parent.getFirstDataSourceWiredEntity().setFirstDataSourceEntity(null);
                    repository.save(parent.getFirstDataSourceWiredEntity());
                }
                parent.setFirstDataSourceWiredEntity(null);
                parentRepository.save(parent);
            }
            // check if newly binded parent is already binded and unbind
            if (entity.getFirstDataSourceEntity() != null) {
                final FirstDataSourceEntity parent = entity.getFirstDataSourceEntity();
                if (parent.getFirstDataSourceWiredEntity() != null) {
                    parent.getFirstDataSourceWiredEntity().setFirstDataSourceEntity(null);
                    repository.save(parent.getFirstDataSourceWiredEntity());
                }
                parent.setFirstDataSourceWiredEntity(null);
                parentRepository.save(parent);
            }
            final FirstDataSourceWiredEntity saved = repository.save(entity);
            if (entity.getFirstDataSourceEntity() != null) {
                entity.getFirstDataSourceEntity().setFirstDataSourceWiredEntity(saved);
                parentRepository.save(entity.getFirstDataSourceEntity());

            }
            saved.setFirstDataSourceEntity(entity.getFirstDataSourceEntity());
            repository.save(saved);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(FirstDataSourceWiredEntity entity) {
        if (entity != null && entity.getFirstDataSourceEntity() != null) {
            entity.getFirstDataSourceEntity().setFirstDataSourceWiredEntity(null);
            parentRepository.save(entity.getFirstDataSourceEntity());
        }
        repository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long aLong) {
        delete(repository.findOne(aLong));
    }

    /**
     * Return list of all avaliable {@link FirstDataSourceEntity}es to bind
     */
    public List<FirstDataSourceEntity> findAllAvaliableBindings() {
        return parentRepository.findAll();
    }

    /**
     * Filter entities by name
     */
    public List<FirstDataSourceWiredEntity> filterByName(String query) {
        return repository.findAllByNameContainsOrderByName(query);
    }
}
