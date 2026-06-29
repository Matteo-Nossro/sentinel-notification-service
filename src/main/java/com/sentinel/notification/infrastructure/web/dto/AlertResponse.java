package com.sentinel.notification.infrastructure.web.dto;

import java.time.Instant;
import java.util.UUID;

import com.sentinel.notification.domain.model.AlertSeverity;

public record AlertResponse(
    UUID id,
    UUID ruleId,
    UUID sourceId,
    double value,
    AlertSeverity severity,
    Instant triggeredAt,
    String message
) {
}
