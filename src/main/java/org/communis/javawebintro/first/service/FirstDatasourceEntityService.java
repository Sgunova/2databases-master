package org.communis.javawebintro.first.service;

import java.util.List;
import org.communis.javawebintro.first.entity.FirstDataSourceEntity;
import org.communis.javawebintro.first.entity.FirstDataSourceWiredEntity;
import org.communis.javawebintro.first.repository.FirstDataSourceWiredEntityRepository;
import org.communis.javawebintro.first.repository.FirstDatabaseEntityRepository;
import org.communis.javawebintro.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to work with {@link FirstDataSourceEntity}
 */
@Service
public class FirstDatasourceEntityService implements CrudService<FirstDataSourceEntity, Long> {

    private final FirstDatabaseEntityRepository repository;
    private final FirstDataSourceWiredEntityRepository wiredRepository;

    public FirstDatasourceEntityService(
            FirstDatabaseEntityRepository repository,
            FirstDataSourceWiredEntityRepository wiredRepository) {
        this.repository = repository;
        this.wiredRepository = wiredRepository;
    }


    @Override
    @Transactional
    public Long save(FirstDataSourceEntity entity) {
        if (entity != null) {
            final FirstDataSourceWiredEntity wiredEntity = entity.getFirstDataSourceWiredEntity();
            if (wiredEntity != null &&
                    wiredEntity.getFirstDataSourceEntity() != null &&
                    !wiredEntity.getFirstDataSourceEntity().equals(entity)) {
                final FirstDataSourceWiredEntity toUpdate = wiredRepository.findOne(wiredEntity.getId());
                if (toUpdate.getFirstDataSourceEntity() != null) {
                    toUpdate.getFirstDataSourceEntity().setFirstDataSourceWiredEntity(null);
                    repository.save(toUpdate.getFirstDataSourceEntity());
                }
                toUpdate.setFirstDataSourceEntity(null);
                wiredRepository.save(toUpdate);
            }
            final FirstDataSourceEntity saved = repository.save(entity);
            if (wiredEntity != null &&
                    wiredEntity.getFirstDataSourceEntity() == null) {
                wiredEntity.setFirstDataSourceEntity(saved);
            }
            saved.setFirstDataSourceWiredEntity(wiredEntity);
            repository.save(saved);
        }
        return null;
    }

    @Override
    public FirstDataSourceEntity findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<FirstDataSourceEntity> findAll() {
        return repository.findAll();
    }


    @Override
    public Long update(FirstDataSourceEntity entity) {
        if (entity != null) {
            final FirstDataSourceEntity toUpdate = repository.findOne(entity.getId());
            //check if rebinding entity from one to another
            if (entity.getFirstDataSourceWiredEntity() != null) {
                final FirstDataSourceWiredEntity wired = entity.getFirstDataSourceWiredEntity();
                if (wired.getFirstDataSourceEntity() != null) {
                    wired.getFirstDataSourceEntity().setFirstDataSourceWiredEntity(null);
                    repository.save(wired.getFirstDataSourceEntity());
                }
                wired.setFirstDataSourceEntity(null);
                wiredRepository.save(wired);
            }
            // check if entity was already binded to another parent
            if (toUpdate.getFirstDataSourceWiredEntity() != null) {
                final FirstDataSourceWiredEntity wired = toUpdate.getFirstDataSourceWiredEntity();
                if (wired.getFirstDataSourceEntity() != null) {
                    wired.getFirstDataSourceEntity().setFirstDataSourceWiredEntity(null);
                    repository.save(wired.getFirstDataSourceEntity());
                }
                wired.setFirstDataSourceEntity(null);
                wiredRepository.save(wired);
            }
            final FirstDataSourceEntity saved = repository.save(entity);
            if (entity.getFirstDataSourceWiredEntity() != null) {
                entity.getFirstDataSourceWiredEntity().setFirstDataSourceEntity(saved);
            }
            saved.setFirstDataSourceWiredEntity(entity.getFirstDataSourceWiredEntity());
            return repository.save(saved).getId();
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(FirstDataSourceEntity entity) {
        if (entity != null && entity.getFirstDataSourceWiredEntity() != null) {
            entity.getFirstDataSourceWiredEntity().setFirstDataSourceEntity(null);
            wiredRepository.save(entity.getFirstDataSourceWiredEntity());
        }

        repository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        delete(repository.findOne(id));
    }

    public List<FirstDataSourceWiredEntity> findAllAvaliableBindings() {
        return wiredRepository.findAll();
    }

    public List<FirstDataSourceEntity> filterByName(String query) {
        return repository.findAllByNameContainsOrderByName(query);
    }

}
