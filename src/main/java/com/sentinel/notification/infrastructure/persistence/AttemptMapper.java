package com.sentinel.notification.infrastructure.persistence;

import com.sentinel.notification.domain.model.NotificationAttempt;
import com.sentinel.notification.infrastructure.persistence.entity.AttemptEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttemptMapper {
    AttemptEntity toEntity(NotificationAttempt attempt);
    NotificationAttempt toDomain(AttemptEntity entity);
}
