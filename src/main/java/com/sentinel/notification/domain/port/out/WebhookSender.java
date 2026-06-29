package com.sentinel.notification.domain.port.out;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.model.NotificationChannel;

public interface WebhookSender {
    void send(Alert alert, NotificationChannel channel);
}
