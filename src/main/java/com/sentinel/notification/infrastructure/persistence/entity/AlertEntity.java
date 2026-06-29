package com.sentinel.notification.infrastructure.persistence.entity;

import java.time.Instant;
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

import com.sentinel.notification.domain.model.AlertSeverity;

@Entity
@Table(name = "alerts", schema = "notification")
public class AlertEntity implements Persistable<UUID> {

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

    private UUID ruleId;
    private UUID sourceId;
    private double value;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    private Instant triggeredAt;
    private String message;

    public AlertEntity() {
    }

    public AlertEntity(UUID id, UUID ruleId, UUID sourceId, double value, AlertSeverity severity, Instant triggeredAt, String message) {
        this.id = id;
        this.ruleId = ruleId;
        this.sourceId = sourceId;
        this.value = value;
        this.severity = severity;
        this.triggeredAt = triggeredAt;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public void setSourceId(UUID sourceId) {
        this.sourceId = sourceId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
    }

    public Instant getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(Instant triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
