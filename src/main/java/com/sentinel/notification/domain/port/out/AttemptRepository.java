package com.sentinel.notification.domain.port.out;

import com.sentinel.notification.domain.model.NotificationAttempt;

public interface AttemptRepository {
    NotificationAttempt save(NotificationAttempt attempt);
}
