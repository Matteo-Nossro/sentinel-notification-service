package com.sentinel.notification.domain.service;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.model.AlertSeverity;
import com.sentinel.notification.domain.model.ChannelType;
import com.sentinel.notification.domain.model.NotificationAttempt;
import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.domain.port.out.AttemptRepository;
import com.sentinel.notification.domain.port.out.SseEmitterRegistry;
import com.sentinel.notification.domain.port.out.WebhookSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationDispatcherTest {

    @Mock
    private SseEmitterRegistry sseEmitterRegistry;

    @Mock
    private WebhookSender webhookSender;

    @Mock
    private AttemptRepository attemptRepository;

    private NotificationDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = new NotificationDispatcher(sseEmitterRegistry, webhookSender, attemptRepository);
    }

    private Alert buildAlert() {
        return new Alert(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                90.0, AlertSeverity.CRITICAL, Instant.now(), "Rule triggered");
    }

    private NotificationChannel buildChannel() {
        return new NotificationChannel(UUID.randomUUID(), ChannelType.DISCORD,
                "alerts-discord", "https://discord.com/webhook/xxx", true);
    }

    @Test
    void dispatch_shouldAlwaysBroadcastViaSse() {
        Alert alert = buildAlert();

        dispatcher.dispatch(alert, List.of());

        verify(sseEmitterRegistry).broadcast(alert);
    }

    @Test
    void dispatch_shouldNotCallWebhookWhenNoChannels() {
        dispatcher.dispatch(buildAlert(), List.of());

        verify(webhookSender, never()).send(any(), any());
    }

    @Test
    void dispatch_shouldCallWebhookAndPersistSuccessOnFirstAttempt() {
        Alert alert = buildAlert();
        NotificationChannel channel = buildChannel();

        dispatcher.dispatch(alert, List.of(channel));

        verify(webhookSender).send(alert, channel);

        ArgumentCaptor<NotificationAttempt> captor = ArgumentCaptor.forClass(NotificationAttempt.class);
        verify(attemptRepository).save(captor.capture());
        assertThat(captor.getValue().success()).isTrue();
        assertThat(captor.getValue().alertId()).isEqualTo(alert.id());
        assertThat(captor.getValue().channelId()).isEqualTo(channel.id());
    }

    @Test
    void dispatch_shouldPersistFailureAndRetryOnWebhookException() {
        Alert alert = buildAlert();
        NotificationChannel channel = buildChannel();
        // échoue au 1er appel, réussit au 2e (1 seconde de sleep entre les deux)
        doThrow(new RuntimeException("timeout"))
                .doNothing()
                .when(webhookSender).send(alert, channel);

        dispatcher.dispatch(alert, List.of(channel));

        verify(webhookSender, times(2)).send(alert, channel);

        ArgumentCaptor<NotificationAttempt> captor = ArgumentCaptor.forClass(NotificationAttempt.class);
        verify(attemptRepository, times(2)).save(captor.capture());
        List<NotificationAttempt> attempts = captor.getAllValues();
        assertThat(attempts.get(0).success()).isFalse();
        assertThat(attempts.get(1).success()).isTrue();
    }

    @Test
    void dispatch_shouldCallWebhookForEachChannel() {
        Alert alert = buildAlert();
        NotificationChannel discord = buildChannel();
        NotificationChannel telegram = new NotificationChannel(UUID.randomUUID(), ChannelType.TELEGRAM,
                "alerts-telegram", "https://api.telegram.org/botXXX/sendMessage", true);

        dispatcher.dispatch(alert, List.of(discord, telegram));

        verify(webhookSender).send(alert, discord);
        verify(webhookSender).send(alert, telegram);
    }

    @Test
    void dispatch_shouldPersistThreeFailuresWhenAllRetriesExhausted() {
        Alert alert = buildAlert();
        NotificationChannel channel = buildChannel();
        doThrow(new RuntimeException("service unavailable"))
                .when(webhookSender).send(alert, channel);

        dispatcher.dispatch(alert, List.of(channel));

        verify(webhookSender, times(3)).send(alert, channel);

        ArgumentCaptor<NotificationAttempt> captor = ArgumentCaptor.forClass(NotificationAttempt.class);
        verify(attemptRepository, times(3)).save(captor.capture());
        assertThat(captor.getAllValues()).allMatch(a -> !a.success());
    }
}
