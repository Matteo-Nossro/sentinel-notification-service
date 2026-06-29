package com.sentinel.notification.domain.model;

import java.util.UUID;

public record NotificationChannel(UUID id, ChannelType type, String name, String webhookUrl, boolean active) {
}
