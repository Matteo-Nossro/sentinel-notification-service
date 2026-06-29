package com.sentinel.notification.infrastructure.web.dto;

import java.util.UUID;

import com.sentinel.notification.domain.model.ChannelType;

public record ChannelResponse(
    UUID id,
    ChannelType type,
    String name,
    String webhookUrl,
    boolean active
) {
}
