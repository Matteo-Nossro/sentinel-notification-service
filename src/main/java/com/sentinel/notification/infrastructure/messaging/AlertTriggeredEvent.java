package com.sentinel.notification.infrastructure.messaging;

import java.time.Instant;
import java.util.UUID;

import com.sentinel.notification.domain.model.AlertSeverity;

public record AlertTriggeredEvent(UUID id, UUID ruleId, UUID sourceId, double value, AlertSeverity severity, Instant triggeredAt, String message) {
}
