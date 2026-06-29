package com.sentinel.notification.infrastructure.persistence;

import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.infrastructure.persistence.entity.ChannelEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    ChannelEntity toEntity(NotificationChannel channel);
    NotificationChannel toDomain(ChannelEntity entity);
}
