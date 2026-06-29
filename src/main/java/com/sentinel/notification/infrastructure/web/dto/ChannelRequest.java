package com.sentinel.notification.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.sentinel.notification.domain.model.ChannelType;

public record ChannelRequest(
    @NotNull ChannelType type,
    @NotBlank String name,
    @NotBlank String webhookUrl,
    boolean active
) {
}
