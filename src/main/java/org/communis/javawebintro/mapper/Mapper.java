package org.communis.javawebintro.mapper;


/**
 * Properties mapper entity <-> dto
 *
 * @param <E>
 * @param <D>
 */
public interface Mapper<E, D> {
    D toDto(E entity);

    E toEntity(D dto);
}
