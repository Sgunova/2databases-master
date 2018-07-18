package org.communis.javawebintro.service;

import java.util.List;

public interface CrudService<E, ID> {

    ID save(E entity);

    E findById(ID id);

    List<E> findAll();

    ID update(E entity);

    void delete(E entity);

    void deleteById(ID id);
}
