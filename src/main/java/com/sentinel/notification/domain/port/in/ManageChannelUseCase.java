package com.sentinel.notification.domain.port.in;

import com.sentinel.notification.domain.model.NotificationChannel;

import java.util.List;
import java.util.UUID;

public interface ManageChannelUseCase {
    NotificationChannel create(NotificationChannel channel);
    NotificationChannel update(UUID id, NotificationChannel channel);
    void delete(UUID id);
    List<NotificationChannel> findAll();
}
