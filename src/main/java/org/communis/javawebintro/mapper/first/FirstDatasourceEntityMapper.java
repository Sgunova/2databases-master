package org.communis.javawebintro.mapper.first;

import org.communis.javawebintro.dto.first.FirstDataSourceEntityDTO;
import org.communis.javawebintro.first.entity.FirstDataSourceEntity;
import org.communis.javawebintro.first.repository.FirstDatabaseEntityRepository;
import org.communis.javawebintro.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Entity mapper DTO <-> Entity
 */
@Component
public class FirstDatasourceEntityMapper implements Mapper<FirstDataSourceEntity, FirstDataSourceEntityDTO> {
    private final FirstDatabaseEntityRepository repository;

    public FirstDatasourceEntityMapper(FirstDatabaseEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public FirstDataSourceEntityDTO toDto(FirstDataSourceEntity entity) {
        if (entity == null) {
            return null;
        }
        final FirstDataSourceEntityDTO dto = new FirstDataSourceEntityDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public FirstDataSourceEntity toEntity(FirstDataSourceEntityDTO dto) {
        if (dto == null) {
            return null;
        }
        final FirstDataSourceEntity entity = new FirstDataSourceEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
