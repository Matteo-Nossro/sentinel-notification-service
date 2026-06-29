package com.sentinel.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public record NotificationAttempt(UUID id, UUID alertId, UUID channelId, boolean success, Instant attemptedAt) {
}
