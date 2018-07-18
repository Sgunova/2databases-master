package org.communis.javawebintro.second.service;

import java.util.List;
import org.communis.javawebintro.second.entity.SecondDataSourceEntity;
import org.communis.javawebintro.second.repository.SecondDatabaseEntityRepository;
import org.communis.javawebintro.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class SecondDatasourceService implements CrudService<SecondDataSourceEntity, Long> {
    private final SecondDatabaseEntityRepository repository;

    public SecondDatasourceService(SecondDatabaseEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Long save(SecondDataSourceEntity entity) {
        return repository.save(entity).getId();
    }

    @Override
    public SecondDataSourceEntity findById(Long aLong) {
        return repository.findOne(aLong);
    }

    @Override
    public List<SecondDataSourceEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Long update(SecondDataSourceEntity entity) {
        return repository.save(entity).getId();
    }

    @Override
    public void delete(SecondDataSourceEntity entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(id);
    }
}
