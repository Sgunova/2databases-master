package org.communis.javawebintro.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseEntityRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

    @Override
    List<T> findAll();
}
