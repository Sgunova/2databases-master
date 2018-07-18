package org.communis.javawebintro.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class BaseCrudController<E, D, ID> {

    public BaseCrudController() {
    }

    public abstract ResponseEntity<ID> createOne(D dto);

    public abstract ResponseEntity<D> findById(ID id);

    public abstract ResponseEntity<List<D>> findAll();

    public abstract ResponseEntity<Void> deleteById(ID id);

    public abstract ResponseEntity<ID> update(D dto);
}
