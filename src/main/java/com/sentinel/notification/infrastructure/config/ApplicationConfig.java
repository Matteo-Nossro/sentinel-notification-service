package com.sentinel.notification.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sentinel.notification.application.command.DispatchAlertHandler;
import com.sentinel.notification.application.command.ManageChannelHandler;
import com.sentinel.notification.domain.port.out.AlertRepository;
import com.sentinel.notification.domain.port.out.AttemptRepository;
import com.sentinel.notification.domain.port.out.ChannelRepository;
import com.sentinel.notification.domain.port.out.SseEmitterRegistry;
import com.sentinel.notification.domain.port.out.WebhookSender;
import com.sentinel.notification.domain.service.NotificationDispatcher;

@Configuration
public class ApplicationConfig {

    @Bean
    public NotificationDispatcher notificationDispatcher(SseEmitterRegistry sseEmitterRegistry,WebhookSender webhookSender,AttemptRepository attemptRepository) {
        return new NotificationDispatcher(sseEmitterRegistry, webhookSender, attemptRepository);
    }

    @Bean
    public DispatchAlertHandler dispatchAlertHandler(AlertRepository alertRepository,ChannelRepository channelRepository,NotificationDispatcher dispatcher) {
        return new DispatchAlertHandler(alertRepository, channelRepository, dispatcher);
    }

    @Bean
    public ManageChannelHandler manageChannelHandler(ChannelRepository channelRepository) {
        return new ManageChannelHandler(channelRepository);
    }

}
