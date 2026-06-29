package com.sentinel.notification.infrastructure.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.port.out.SseEmitterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitterRegistryImpl implements SseEmitterRegistry {

    private static final Logger log = LoggerFactory.getLogger(SseEmitterRegistryImpl.class);

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;

    public SseEmitterRegistryImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void broadcast(Alert alert) {
        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("alert")
                        .data(objectMapper.writeValueAsString(alert)));
            } catch (Exception e) {
                log.debug("Emitter mort, suppression du registry");
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }
}
