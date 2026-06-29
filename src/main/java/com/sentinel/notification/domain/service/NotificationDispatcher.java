package com.sentinel.notification.domain.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.model.NotificationAttempt;
import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.domain.port.out.AttemptRepository;
import com.sentinel.notification.domain.port.out.SseEmitterRegistry;
import com.sentinel.notification.domain.port.out.WebhookSender;

public class NotificationDispatcher {

    private static final long[] RETRY_DELAYS_MS = {1_000, 5_000, 30_000};
    private static final int MAX_ATTEMPTS = 3;

    private final SseEmitterRegistry sseEmitterRegistry;
    private final WebhookSender webhookSender;
    private final AttemptRepository attemptRepository;

    public NotificationDispatcher(SseEmitterRegistry sseEmitterRegistry, WebhookSender webhookSender, AttemptRepository attemptRepository) {
        this.sseEmitterRegistry = sseEmitterRegistry;
        this.webhookSender = webhookSender;
        this.attemptRepository = attemptRepository;
    }

    public void dispatch(Alert alert, List<NotificationChannel> channels) {
        sseEmitterRegistry.broadcast(alert);
        for (NotificationChannel channel : channels) {
            sendWithRetry(alert, channel);
        }
    }

    private void sendWithRetry(Alert alert, NotificationChannel channel) {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            if (attempt > 0) {
                sleep(RETRY_DELAYS_MS[attempt - 1]);
            }
            try {
                webhookSender.send(alert, channel);
                persistAttempt(alert.id(), channel.id(), true);
                return;
            } catch (Exception e) {
                persistAttempt(alert.id(), channel.id(), false);
            }
        }
    }

    private void persistAttempt(UUID alertId, UUID channelId, boolean success) {
        attemptRepository.save(new NotificationAttempt(UUID.randomUUID(), alertId, channelId, success, Instant.now()));
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
