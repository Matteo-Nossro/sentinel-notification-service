package com.sentinel.notification.infrastructure.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sentinel.notification.application.command.DispatchAlertHandler;
import com.sentinel.notification.infrastructure.sse.SseEmitterRegistryImpl;
import com.sentinel.notification.infrastructure.web.dto.AlertResponse;

import java.util.List;

@RestController
public class AlertStreamController {

    private final SseEmitterRegistryImpl sseEmitterRegistry;
    private final DispatchAlertHandler dispatchAlertHandler;

    public AlertStreamController(SseEmitterRegistryImpl sseEmitterRegistry, DispatchAlertHandler dispatchAlertHandler) {
        this.sseEmitterRegistry = sseEmitterRegistry;
        this.dispatchAlertHandler = dispatchAlertHandler;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return sseEmitterRegistry.subscribe();
    }

    @GetMapping("/alerts")
    public List<AlertResponse> getAlerts() {
        return dispatchAlertHandler.getHistory().stream()
                .map(a -> new AlertResponse(a.id(), a.ruleId(), a.sourceId(), a.value(), a.severity(), a.triggeredAt(), a.message()))
                .toList();
    }

}
