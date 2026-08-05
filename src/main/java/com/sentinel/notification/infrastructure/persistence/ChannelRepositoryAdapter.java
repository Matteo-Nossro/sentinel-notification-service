package com.sentinel.notification.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sentinel.notification.domain.model.NotificationChannel;
import com.sentinel.notification.domain.port.out.ChannelRepository;
import com.sentinel.notification.infrastructure.persistence.entity.ChannelEntity;

@Component
public class ChannelRepositoryAdapter implements ChannelRepository {

    private final ChannelJpaRepository channelJpaRepository;

    public ChannelRepositoryAdapter(ChannelJpaRepository channelJpaRepository) {
        this.channelJpaRepository = channelJpaRepository;
    }

    @Override
    public NotificationChannel save(NotificationChannel channel) {
        // reutilise l'entite chargee : new ChannelEntity() causait un NonUniqueObjectException avec open-in-view
        ChannelEntity entity = channelJpaRepository.findById(channel.id()).orElseGet(ChannelEntity::new);
        entity.setId(channel.id());
        entity.setType(channel.type());
        entity.setName(channel.name());
        entity.setWebhookUrl(channel.webhookUrl());
        entity.setActive(channel.active());
        channelJpaRepository.save(entity);
        return channel;
    }

    @Override
    public Optional<NotificationChannel> findById(UUID id) {
        return channelJpaRepository.findById(id)
                .map(e -> new NotificationChannel(e.getId(), e.getType(), e.getName(), e.getWebhookUrl(), e.isActive()));
    }

    @Override
    public List<NotificationChannel> findAll() {
        return channelJpaRepository.findAll().stream()
                .map(e -> new NotificationChannel(e.getId(), e.getType(), e.getName(), e.getWebhookUrl(), e.isActive()))
                .toList();
    }

    @Override
    public List<NotificationChannel> findAllActive() {
        return channelJpaRepository.findByActiveTrue().stream()
                .map(e -> new NotificationChannel(e.getId(), e.getType(), e.getName(), e.getWebhookUrl(), e.isActive()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        channelJpaRepository.deleteById(id);
    }

}
