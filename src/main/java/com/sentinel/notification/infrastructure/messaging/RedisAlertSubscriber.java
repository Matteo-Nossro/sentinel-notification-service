package com.sentinel.notification.infrastructure.messaging;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.notification.application.command.DispatchAlertHandler;
import com.sentinel.notification.domain.model.Alert;

@Component
public class RedisAlertSubscriber implements MessageListener {

    private final DispatchAlertHandler dispatchAlertHandler;
    private final ObjectMapper objectMapper;

    public RedisAlertSubscriber(DispatchAlertHandler dispatchAlertHandler, ObjectMapper objectMapper) {
        this.dispatchAlertHandler = dispatchAlertHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            AlertTriggeredEvent event = objectMapper.readValue(message.getBody(), AlertTriggeredEvent.class);
            Alert alert = new Alert(event.id(), event.ruleId(), event.sourceId(), event.value(), event.severity(), event.triggeredAt(), event.message());
            dispatchAlertHandler.dispatch(alert);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process alert.triggered message", e);
        }
    }

}
