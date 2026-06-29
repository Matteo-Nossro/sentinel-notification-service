package com.sentinel.notification.infrastructure.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.port.out.AlertRepository;
import com.sentinel.notification.infrastructure.persistence.entity.AlertEntity;

@Component
public class AlertRepositoryAdapter implements AlertRepository {

    private final AlertJpaRepository alertJpaRepository;

    public AlertRepositoryAdapter(AlertJpaRepository alertJpaRepository) {
        this.alertJpaRepository = alertJpaRepository;
    }

    @Override
    public Alert save(Alert alert) {
        AlertEntity entity = new AlertEntity();
        entity.setId(alert.id());
        entity.setRuleId(alert.ruleId());
        entity.setSourceId(alert.sourceId());
        entity.setValue(alert.value());
        entity.setSeverity(alert.severity());
        entity.setTriggeredAt(alert.triggeredAt());
        entity.setMessage(alert.message());
        alertJpaRepository.save(entity);
        return alert;
    }

    @Override
    public List<Alert> findAll() {
        return alertJpaRepository.findAll().stream()
                .map(e -> new Alert(e.getId(), e.getRuleId(), e.getSourceId(), e.getValue(), e.getSeverity(), e.getTriggeredAt(), e.getMessage()))
                .toList();
    }

}
