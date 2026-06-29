package com.sentinel.notification.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.sentinel.notification.domain.model.NotificationAttempt;
import com.sentinel.notification.domain.port.out.AttemptRepository;
import com.sentinel.notification.infrastructure.persistence.entity.AttemptEntity;

@Component
public class AttemptRepositoryAdapter implements AttemptRepository {

    private final AttemptJpaRepository attemptJpaRepository;

    public AttemptRepositoryAdapter(AttemptJpaRepository attemptJpaRepository) {
        this.attemptJpaRepository = attemptJpaRepository;
    }

    @Override
    public NotificationAttempt save(NotificationAttempt attempt) {
        AttemptEntity entity = new AttemptEntity();
        entity.setId(attempt.id());
        entity.setAlertId(attempt.alertId());
        entity.setChannelId(attempt.channelId());
        entity.setSuccess(attempt.success());
        entity.setAttemptedAt(attempt.attemptedAt());
        attemptJpaRepository.save(entity);
        return attempt;
    }

}
