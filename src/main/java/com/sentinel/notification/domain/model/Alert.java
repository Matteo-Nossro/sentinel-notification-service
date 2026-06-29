package com.sentinel.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Alert(UUID id, UUID ruleId, UUID sourceId, double value, AlertSeverity severity, Instant triggeredAt, String message) {
}
