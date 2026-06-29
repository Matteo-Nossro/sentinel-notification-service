package com.sentinel.notification.infrastructure.webhook;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.domain.port.out.WebhookSender;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class RestClientWebhookSender implements WebhookSender {

    private final RestClient restClient;

    public RestClientWebhookSender(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Override
    public void send(Alert alert, NotificationChannel channel) {
        Object payload = buildPayload(alert, channel);
        restClient.post()
                .uri(channel.webhookUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }

    private Object buildPayload(Alert alert, NotificationChannel channel) {
        String message = "[SENTINEL | %s] %s".formatted(alert.severity(), alert.message());
        return switch (channel.type()) {
            case DISCORD -> Map.of("content", message);
            case TELEGRAM -> Map.of("text", message);
            case CUSTOM -> Map.of(
                    "alertId", alert.id().toString(),
                    "ruleId", alert.ruleId().toString(),
                    "sourceId", alert.sourceId().toString(),
                    "severity", alert.severity().name(),
                    "message", alert.message(),
                    "triggeredAt", alert.triggeredAt().toString()
            );
        };
    }
}
