package org.communis.javawebintro.mapper.second;

import org.communis.javawebintro.dto.second.SecondDataSourceEntityDTO;
import org.communis.javawebintro.mapper.Mapper;
import org.communis.javawebintro.second.entity.SecondDataSourceEntity;
import org.communis.javawebintro.second.repository.SecondDatabaseEntityRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * {@link SecondDataSourceEntity} Entity mapper DTO <-> Entity
 */
@Component
public class SecondDatasourceEntityMapper implements Mapper<SecondDataSourceEntity, SecondDataSourceEntityDTO> {
    private final SecondDatabaseEntityRepository repository;

    public SecondDatasourceEntityMapper(SecondDatabaseEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public SecondDataSourceEntityDTO toDto(SecondDataSourceEntity entity) {
        if (entity == null) {
            return null;
        }
        final SecondDataSourceEntityDTO dto = new SecondDataSourceEntityDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public SecondDataSourceEntity toEntity(SecondDataSourceEntityDTO dto) {
        if (dto == null) {
            return null;
        }
        final SecondDataSourceEntity entity = new SecondDataSourceEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
