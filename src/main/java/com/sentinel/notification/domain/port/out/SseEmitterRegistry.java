package com.sentinel.notification.domain.port.out;

import com.sentinel.notification.domain.model.Alert;

public interface SseEmitterRegistry {
    void broadcast(Alert alert);
}
