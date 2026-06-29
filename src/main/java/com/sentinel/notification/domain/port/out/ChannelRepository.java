package com.sentinel.notification.domain.port.out;

import com.sentinel.notification.domain.model.NotificationChannel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    NotificationChannel save(NotificationChannel channel);
    Optional<NotificationChannel> findById(UUID id);
    List<NotificationChannel> findAll();
    List<NotificationChannel> findAllActive();
    void deleteById(UUID id);
}
