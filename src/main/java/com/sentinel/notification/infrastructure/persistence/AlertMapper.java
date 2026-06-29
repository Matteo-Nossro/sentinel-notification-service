package com.sentinel.notification.infrastructure.persistence;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.infrastructure.persistence.entity.AlertEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    AlertEntity toEntity(Alert alert);
    Alert toDomain(AlertEntity entity);
}
