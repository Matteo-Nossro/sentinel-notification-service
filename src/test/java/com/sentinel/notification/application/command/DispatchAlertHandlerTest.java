package com.sentinel.notification.application.command;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.model.AlertSeverity;
import com.sentinel.notification.domain.model.ChannelType;
import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.domain.port.out.AlertRepository;
import com.sentinel.notification.domain.port.out.ChannelRepository;
import com.sentinel.notification.domain.service.NotificationDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispatchAlertHandlerTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private NotificationDispatcher dispatcher;

    private DispatchAlertHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DispatchAlertHandler(alertRepository, channelRepository, dispatcher);
    }

    private Alert buildAlert() {
        return new Alert(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                90.0, AlertSeverity.WARNING, Instant.now(), "Rule triggered");
    }

    @Test
    void dispatch_shouldSaveAlertThenDelegateWithActiveChannels() {
        Alert alert = buildAlert();
        List<NotificationChannel> channels = List.of(
                new NotificationChannel(UUID.randomUUID(), ChannelType.DISCORD, "alerts-discord", "https://discord.com/webhook/xxx", true)
        );
        when(channelRepository.findAllActive()).thenReturn(channels);

        handler.dispatch(alert);

        verify(alertRepository).save(alert);
        verify(dispatcher).dispatch(eq(alert), eq(channels));
    }

    @Test
    void getHistory_shouldReturnAllAlertsFromRepository() {
        Alert a1 = buildAlert();
        Alert a2 = buildAlert();
        when(alertRepository.findAll()).thenReturn(List.of(a1, a2));

        List<Alert> result = handler.getHistory();

        assertThat(result).containsExactly(a1, a2);
    }
}
