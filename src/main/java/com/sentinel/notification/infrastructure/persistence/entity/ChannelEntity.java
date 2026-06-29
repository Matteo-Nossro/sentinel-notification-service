package com.sentinel.notification.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import org.springframework.data.domain.Persistable;

import com.sentinel.notification.domain.model.ChannelType;

@Entity
@Table(name = "notification_channels", schema = "notification")
public class ChannelEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Transient
    private boolean isNew = true;

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    private String name;
    private String webhookUrl;
    private boolean active;

    public ChannelEntity() {
    }

    public ChannelEntity(UUID id, ChannelType type, String name, String webhookUrl, boolean active) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.webhookUrl = webhookUrl;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
