package com.sentinel.notification.application.command;

import java.util.List;
import java.util.UUID;

import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.domain.port.in.ManageChannelUseCase;
import com.sentinel.notification.domain.port.out.ChannelRepository;

public class ManageChannelHandler implements ManageChannelUseCase {

    private final ChannelRepository channelRepository;

    public ManageChannelHandler(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public NotificationChannel create(NotificationChannel channel) {
        return channelRepository.save(new NotificationChannel(UUID.randomUUID(), channel.type(), channel.name(), channel.webhookUrl(), channel.active()));
    }

    @Override
    public NotificationChannel update(UUID id, NotificationChannel channel) {
        channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Canal introuvable : " + id));
        return channelRepository.save(new NotificationChannel(id, channel.type(), channel.name(), channel.webhookUrl(), channel.active()));
    }

    @Override
    public void delete(UUID id) {
        channelRepository.deleteById(id);
    }

    @Override
    public List<NotificationChannel> findAll() {
        return channelRepository.findAll();
    }

}
