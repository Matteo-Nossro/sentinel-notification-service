package com.sentinel.notification.application.command;

import java.util.List;

import com.sentinel.notification.domain.model.Alert;
import com.sentinel.notification.domain.port.in.DispatchAlertUseCase;
import com.sentinel.notification.domain.port.out.AlertRepository;
import com.sentinel.notification.domain.port.out.ChannelRepository;
import com.sentinel.notification.domain.service.NotificationDispatcher;

public class DispatchAlertHandler implements DispatchAlertUseCase {

    private final AlertRepository alertRepository;
    private final ChannelRepository channelRepository;
    private final NotificationDispatcher dispatcher;

    public DispatchAlertHandler(AlertRepository alertRepository, ChannelRepository channelRepository, NotificationDispatcher dispatcher) {
        this.alertRepository = alertRepository;
        this.channelRepository = channelRepository;
        this.dispatcher = dispatcher;
    }

    @Override
    public void dispatch(Alert alert) {
        alertRepository.save(alert);
        dispatcher.dispatch(alert, channelRepository.findAllActive());
    }

    @Override
    public List<Alert> getHistory() {
        return alertRepository.findAll();
    }

}
